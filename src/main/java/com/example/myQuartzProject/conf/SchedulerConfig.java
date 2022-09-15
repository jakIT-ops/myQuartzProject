package com.example.myQuartzProject.conf;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private QuartzProperties quartzProperties;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerJobFactory jobFactory = new SchedulerJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setQuartzProperties(properties);
        factory.setJobFactory(jobFactory);
        return factory;
    }

//    @Bean
//    public JobFactory jobFactory(ApplicationContext applicationContext)
//    {
//        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
//        jobFactory.setApplicationContext(applicationContext);
//        return jobFactory;
//    }

//    @Bean
//    public Scheduler schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws Exception {
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//        // this allows to update triggers in DB when updating settings in config file
//        factory.setOverwriteExistingJobs(true);
//        factory.setDataSource(dataSource);
//        // use specify jobFactory to create jobDetail
//        factory.setJobFactory(jobFactory);
//
//        factory.setQuartzProperties(quartzProperties());
//        factory.afterPropertiesSet();
//
//        Scheduler scheduler = factory.getScheduler();
//        scheduler.setJobFactory(jobFactory);
//
//        // register all jobs
//        List<SchedulerJobInfo> jobs = QuartzJobFactory.getInitAllJobs();
//        for (SchedulerJobInfo job : jobs) {
//            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
//            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
//            if (null == trigger) {
//                JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
//                        .withIdentity(job.getJobName(), job.getJobGroup())
//                        .withDescription(job.getDesc()).build();
//                jobDetail.getJobDataMap().put("scheduleJob", job);
//                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
//                        .getCronExpression());
//                trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
//                scheduler.scheduleJob(jobDetail, trigger);
//            }else {
//                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
//                        .getCronExpression());
//                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
//                        .withSchedule(scheduleBuilder).build();
//                scheduler.rescheduleJob(triggerKey, trigger);
//            }
//        }
//
//        scheduler.start();
//        return scheduler;
//    }

//    @Bean
//    public Properties quartzProperties() throws IOException {
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
//        propertiesFactoryBean.afterPropertiesSet();
//        return propertiesFactoryBean.getObject();
//    }
}
