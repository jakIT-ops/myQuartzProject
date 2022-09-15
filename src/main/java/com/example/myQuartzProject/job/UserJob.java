package com.example.myQuartzProject.job;

import com.example.myQuartzProject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public class UserJob implements Job {

    // implement QuartzJobBean
    //    @Override
//    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//
//    }

    @Autowired
    private UserService userService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
        userService.usersStats();
        log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
    }
}
