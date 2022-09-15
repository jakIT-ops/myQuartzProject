package com.example.myQuartzProject;

//import com.example.myQuartzProject.conf.SchedulerJobFactory;
import com.example.myQuartzProject.job.SimpleJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@SpringBootApplication
public class MyQuartzProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyQuartzProjectApplication.class, args);
	}
//	SchedulerFactory sf = new StdSchedulerFactory("src/main/resources/quartz.properties");
//
//	// Define the job
//	JobDetail job = newJob(SimpleJob.class)
//			.withIdentity("job1", "group1")
//			.build();
//	Scheduler sched = sf.getScheduler();
//
//	// compute a time that is on the next round minute
//	Date runTime = evenMinuteDate(new Date());
//	// Trigger the job to run on the next round minute
//	Trigger trigger = newTrigger()
//			.withIdentity("trigger1", "group1")
//			.startAt(runTime)
//			.build();
//
//	sched.scheduleJob(job, trigger);
//
//	sched.start();
//
//	// sleep 90 sec
//	Thread.sleep(90L * 1000L);
//
//	sched.shutdown(true);
}
