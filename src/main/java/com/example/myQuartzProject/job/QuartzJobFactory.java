package com.example.myQuartzProject.job;

import com.example.myQuartzProject.service.SampleService;
import com.google.common.collect.Lists;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class QuartzJobFactory implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
//
//    @Autowired
//    private SampleService sampleService;
//
//
//    public static List<ScheduleJob> jobList = Lists.newArrayList();
//    static {
//        for(int i = 0; i < 5; i++) {
//            ScheduleJob job = new ScheduleJob();
//            job.setJobId(String.valueOf(i));
//            job.setJobName("job_bane_" + i);
//            if(i%2==0) {
//                job.setJobGroup("job_group_even");
//            } else {
//                job.setJobGroup("job_group_odd");
//            }
//            job.setJobStatus("1");
//            job.setCronExpression(String.format("0%s * * * * ?", (i+1)*5));
//            job.setDesc("i am job number " + i);
//            job.setInterfaceName("interface"+i);
//            jobList.add(job);
//        }
//    }
//
//    public static List<ScheduleJob> getInitAllJobs() { return jobList;}
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        ScheduleJob scheduleJob = (ScheduleJob)jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
//        String jobName = scheduleJob.getJobName();
//
//        // execute task inner quartz system
//        // spring bean can be @Autowired
//        sampleService.hello(jobName);
//
//        // simulate time-consuming task
//        if(jobName.equals("job_name_4") || jobName.equals("addjob")) {
//            try {
//                Thread.sleep(1000*60);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }
}
