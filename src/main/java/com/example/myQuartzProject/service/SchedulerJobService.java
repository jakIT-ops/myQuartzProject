package com.example.myQuartzProject.service;

import com.example.myQuartzProject.component.JobScheduleCreator;
import com.example.myQuartzProject.entity.SchedulerJobInfo;
import com.example.myQuartzProject.job.QuartzJobFactory;

import com.example.myQuartzProject.job.SampleCronJob;
import com.example.myQuartzProject.job.SimpleJob;
import com.example.myQuartzProject.job.UserJob;
import com.example.myQuartzProject.repository.SchedulerRepository;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Slf4j
@Transactional
@Service
public class SchedulerJobService {

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private SchedulerRepository schedulerRepository;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private JobScheduleCreator scheduleCreator;


	public SchedulerMetaData getMetaData() throws SchedulerException {
		SchedulerMetaData metaData = scheduler.getMetaData();
		return metaData;
	}

	public List<SchedulerJobInfo> getAllJobList() {
		return schedulerRepository.findAll();
	}

	public boolean deleteJob(SchedulerJobInfo jobInfo) {
		try {
			SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
			schedulerRepository.delete(getJobInfo);
			log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " deleted.");
			return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
		} catch (SchedulerException e) {
			log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
			return false;
		}
	}

	public boolean pauseJob(SchedulerJobInfo jobInfo) {
		try {
			SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
			getJobInfo.setJobStatus("PAUSED");
			schedulerRepository.save(getJobInfo);
			schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
			log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " paused.");
			return true;
		} catch (SchedulerException e) {
			log.error("Failed to pause job - {}", jobInfo.getJobName(), e);
			return false;
		}
	}

	public boolean resumeJob(SchedulerJobInfo jobInfo) {
		try {
			SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
			getJobInfo.setJobStatus("RESUMED");
			schedulerRepository.save(getJobInfo);
			schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
			log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " resumed.");
			return true;
		} catch (SchedulerException e) {
			log.error("Failed to resume job - {}", jobInfo.getJobName(), e);
			return false;
		}
	}

	public boolean startJobNow(SchedulerJobInfo jobInfo) {
		try {
			SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
			getJobInfo.setJobStatus("SCHEDULED & STARTED");
			schedulerRepository.save(getJobInfo);
			schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
			log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled and started now.");
			return true;
		} catch (SchedulerException e) {
			log.error("Failed to start new job - {}", jobInfo.getJobName(), e);
			return false;
		}
	}

	/*
	 * The @SuppressWarnings annotation disables certain compiler warnings.
	 * In this case, the warning about deprecated code ("deprecation") and unused
	 * local variables or unused private methods ("unused").
	 * */
	@SuppressWarnings("deprecation")
	public void saveOrupdate(SchedulerJobInfo scheduleJob) throws Exception {
		log.info("test: ",scheduleJob.getJobName());
		if (scheduleJob.getCronExpression().length() > 0) {
			scheduleJob.setJobClass(SampleCronJob.class.getName());
			scheduleJob.setCronJob(true);
		} else {
			scheduleJob.setJobClass(SimpleJob.class.getName());
			scheduleJob.setCronJob(false);
			scheduleJob.setRepeatTime((long) 1);
		}
		if (StringUtils.isEmpty(scheduleJob.getJobId())) {
			log.info("Job Info: {}", scheduleJob);
			scheduleNewJob(scheduleJob);
		} else {
			updateScheduleJob(scheduleJob);
		}
		scheduleJob.setDesc("i am job number " + scheduleJob.getJobId());
		scheduleJob.setInterfaceName("interface_" + scheduleJob.getJobId());
		log.info(">>>>> jobName = [" + scheduleJob.getJobName() + "]" + " created.");
	}

	@SuppressWarnings("unchecked")
	private void scheduleNewJob(SchedulerJobInfo jobInfo) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();

			JobDetail jobDetail = JobBuilder
					.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
					.withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
			if (!scheduler.checkExists(jobDetail.getKey())) {

				jobDetail = scheduleCreator.createJob(
						(Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), false, context,
						jobInfo.getJobName(), jobInfo.getJobGroup());

				Trigger trigger;
				if (jobInfo.getCronJob()) {
					trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
							jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
				} else {
					trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(),
							jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
				}
				scheduler.scheduleJob(jobDetail, trigger);
				jobInfo.setJobStatus("SCHEDULED");
				schedulerRepository.save(jobInfo);
				log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled.");
			} else {
				log.error("scheduleNewJobRequest.jobAlreadyExist");
			}
		} catch (ClassNotFoundException e) {
			log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void updateScheduleJob(SchedulerJobInfo jobInfo) {
		Trigger newTrigger;
		if (jobInfo.getCronJob()) {
			newTrigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
					jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
		} else {
			newTrigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(), jobInfo.getRepeatTime(),
					SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
		}
		try {
			schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
			jobInfo.setJobStatus("EDITED & SCHEDULED");
			schedulerRepository.save(jobInfo);
			log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " updated and scheduled.");
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void userStatusJob(SchedulerJobInfo jobInfo) throws Exception {
		try {
			//
			if (jobInfo.getCronExpression().length() > 0) {
				jobInfo.setJobClass(UserJob.class.getName());
				jobInfo.setCronJob(true);
			} else {
				jobInfo.setJobClass(UserJob.class.getName());
				jobInfo.setCronJob(false);
				jobInfo.setRepeatTime((long) 1);
			}
			Scheduler scheduler = schedulerFactoryBean.getScheduler();

			JobDetail jobDetail = JobBuilder
					.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
					.withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
			if (!scheduler.checkExists(jobDetail.getKey())) {

				jobDetail = scheduleCreator.createJob(
						(Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), false, context,
						jobInfo.getJobName(), jobInfo.getJobGroup());

				Trigger trigger;
				if (jobInfo.getCronJob()) {
					trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
							jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
				} else {
					trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(),
							jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
				}
				scheduler.scheduleJob(jobDetail, trigger);
				jobInfo.setJobStatus("SCHEDULED");
				schedulerRepository.save(jobInfo);
				log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled.");
			} else {
				log.error("scheduleNewJobRequest.jobAlreadyExist");
			}
		} catch (ClassNotFoundException e) {
			log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}


	////////////////////////////////////
//	public List<SchedulerJobInfo> getAllJobList(){
//		List<SchedulerJobInfo> jobList = new ArrayList<>();
//		try {
//			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
//			Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
//			for (JobKey jobKey : jobKeySet){
//				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
//				for (Trigger trigger : triggers){
//					SchedulerJobInfo scheduleJob = new SchedulerJobInfo();
//					this.wrapScheduleJob(scheduleJob,scheduler,jobKey,trigger);
//					jobList.add(scheduleJob);
//				}
//			}
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
//		return jobList;
//	}
//
//	public List<SchedulerJobInfo> getRunningJobList() throws SchedulerException{
//		List<JobExecutionContext> executingJobList = scheduler.getCurrentlyExecutingJobs();
//		List<SchedulerJobInfo> jobList = new ArrayList<>(executingJobList.size());
//		for(JobExecutionContext executingJob : executingJobList){
//			SchedulerJobInfo scheduleJob = new SchedulerJobInfo();
//			JobDetail jobDetail = executingJob.getJobDetail();
//			JobKey jobKey = jobDetail.getKey();
//			Trigger trigger = executingJob.getTrigger();
//			this.wrapScheduleJob(scheduleJob,scheduler,jobKey,trigger);
//			jobList.add(scheduleJob);
//		}
//		return jobList;
//	}
//
//
//	public void saveOrupdate(SchedulerJobInfo scheduleJob) throws Exception {
//		Preconditions.checkNotNull(scheduleJob, "job is null");
//		if (StringUtils.isEmpty(scheduleJob.getJobId())) {
//			addJob(scheduleJob);
//		}else {
//			updateJobCronExpression(scheduleJob);
//		}
//	}
//
//	private void addJob(SchedulerJobInfo scheduleJob) throws Exception{
//		checkNotNull(scheduleJob);
//		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getCronExpression()), "CronExpression is null");
//
//		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
//		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
//		if(trigger != null){
//			throw new Exception("job already exists!");
//		}
//
//		// simulate job info db persist operation
//		scheduleJob.setJobId(String.valueOf(QuartzJobFactory.jobList.size()+1));
//		QuartzJobFactory.jobList.add(scheduleJob);
//
//		JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(scheduleJob.getJobName(),scheduleJob.getJobGroup()).build();
//		jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);
//
//		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
//		trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).withSchedule(cronScheduleBuilder).build();
//
//		scheduler.scheduleJob(jobDetail, trigger);
//
//	}
//
//
//	public void pauseJob(SchedulerJobInfo scheduleJob) throws SchedulerException{
//		checkNotNull(scheduleJob);
//		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
//		scheduler.pauseJob(jobKey);
//	}
//
//	public void resumeJob(SchedulerJobInfo scheduleJob) throws SchedulerException{
//		checkNotNull(scheduleJob);
//		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
//		scheduler.resumeJob(jobKey);
//	}
//
//	public boolean deleteJob(SchedulerJobInfo jobInfo) {
//		try {
//			SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
//			schedulerRepository.delete(getJobInfo);
//			log.info(">>>>>> jobName [" + jobInfo.getJobName() + "]" + "deleted");
//			return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobU))
//		}
//	}
////
////	public void deleteJob(SchedulerJobInfo scheduleJob) throws SchedulerException{
////		checkNotNull(scheduleJob);
////		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
////		scheduler.deleteJob(jobKey);
////	}
//
//	public void runJobOnce(SchedulerJobInfo scheduleJob) throws SchedulerException{
//		checkNotNull(scheduleJob);
//		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
//		scheduler.triggerJob(jobKey);
//	}
//
//
//	private void updateJobCronExpression(SchedulerJobInfo scheduleJob) throws SchedulerException{
//		checkNotNull(scheduleJob);
//		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getCronExpression()), "CronExpression is null");
//
//		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
//		CronTrigger cronTrigger = (CronTrigger)scheduler.getTrigger(triggerKey);
//		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
//		cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
//		scheduler.rescheduleJob(triggerKey, cronTrigger);
//	}
//
//	private void wrapScheduleJob(SchedulerJobInfo scheduleJob, Scheduler scheduler, JobKey jobKey, Trigger trigger){
//		try {
//			scheduleJob.setJobName(jobKey.getName());
//			scheduleJob.setJobGroup(jobKey.getGroup());
//
//			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
//			SchedulerJobInfo job = (SchedulerJobInfo)jobDetail.getJobDataMap().get("scheduleJob");
//			scheduleJob.setDesc(job.getDesc());
//			scheduleJob.setJobId(job.getJobId());
//
//			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
//			scheduleJob.setJobStatus(triggerState.name());
//			if(trigger instanceof CronTrigger){
//				CronTrigger cronTrigger = (CronTrigger)trigger;
//				String cronExpression = cronTrigger.getCronExpression();
//				scheduleJob.setCronExpression(cronExpression);
//			}
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void checkNotNull(SchedulerJobInfo scheduleJob) {
//		Preconditions.checkNotNull(scheduleJob, "job is null");
//		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getJobName()), "jobName is null");
//		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getJobGroup()), "jobGroup is null");
//	}
//
//
//	public SchedulerMetaData getMetaData() throws SchedulerException {
//		SchedulerMetaData metaData = scheduler.getMetaData();
//		return metaData;
//	}
}

















