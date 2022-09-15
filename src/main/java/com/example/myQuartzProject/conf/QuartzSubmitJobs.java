package com.example.myQuartzProject.conf;

//import com.example.myQuartzProject.conf.QuartzConfig;

import com.example.myQuartzProject.job.UserJob;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;


//@Configuration
//public class QuartzSubmitJobs {
//
//    private static final String CRON_EVERY_TEN_SECONDS = "*/10 * * * * *";
//
//    @Bean(name = "userStats")
//    public JobDetailFactoryBean jobUserStats(){
//        return QuartzConfig.createJobDetail(UserJob.class, "User Statistics Job");
//    }
//
//    @Bean(name = "userStatsTrigger")
//    public SimpleTriggerFactoryBean triggerUserStats(@Qualifier("userStats")JobDetail jobDetail) {
//        return QuartzConfig.createTrigger(jobDetail, 60000, "User Statistics Trigger");
//    }
//
//
//    @Bean(name = "userClassTrigger")
//    public JobDetailFactoryBean jobUserClassStats(){
//        return QuartzConfig.createJobDetail(UserJob.class, "Class Statistic Job");
//    }
//
//    @Bean(name = "userClassStatsTrigger")
//    public CronTriggerFactoryBean triggerUserClassStats(@Qualifier("userClassStats") JobDetail jobDetail) {
//        return QuartzConfig.createCronTrigger(jobDetail, CRON_EVERY_TEN_SECONDS, "Class Statistic Trigger");
//    }
//}