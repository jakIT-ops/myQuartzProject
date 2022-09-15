package com.example.myQuartzProject.controller;

import com.example.myQuartzProject.entity.Message;
import com.example.myQuartzProject.entity.SchedulerJobInfo;
import com.example.myQuartzProject.service.SchedulerJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class JobController {

    private final SchedulerJobService scheduleJobService;

    @PostMapping("/saveOrUpdate")
    public Object saveOrUpdate(@RequestBody SchedulerJobInfo job) {
        log.info("params, job = {}", job);
        Message message = Message.failure();
        try {
            scheduleJobService.saveOrupdate(job);
            message = Message.success();
        } catch (Exception e) {
            message.setMsg(e.getMessage());
            log.error("updateCron ex:", e);
        }
        return message;
    }

    @GetMapping("/metaData")
    public Object metaData() throws SchedulerException {
        SchedulerMetaData metaData = scheduleJobService.getMetaData();
        return metaData;
    }

    @GetMapping("/getAllJobs")
    public Object getAllJobs() throws SchedulerException {
        List<SchedulerJobInfo> jobList = scheduleJobService.getAllJobList();
        return jobList;
    }

    @PostMapping("/runJob")
    public Object runJob(@RequestBody SchedulerJobInfo job) {
        log.info("params, job = {}", job);
        Message message = Message.failure();
        try {
            scheduleJobService.startJobNow(job);
            message = Message.success();
        } catch (Exception e) {
            message.setMsg(e.getMessage());
            log.error("runJob ex:", e);
        }
        return message;
    }

    @PostMapping("/pauseJob")
    public Object pauseJob(@RequestBody SchedulerJobInfo job) {
        log.info("params, job = {}", job);
        Message message = Message.failure();
        try {
            scheduleJobService.pauseJob(job);
            message = Message.success();
        } catch (Exception e) {
            message.setMsg(e.getMessage());
            log.error("pauseJob ex:", e);
        }
        return message;
    }

    @PostMapping("/resumeJob")
    public Object resumeJob(@RequestBody SchedulerJobInfo job) {
        log.info("params, job = {}", job);
        Message message = Message.failure();
        try {
            scheduleJobService.resumeJob(job);
            message = Message.success();
        } catch (Exception e) {
            message.setMsg(e.getMessage());
            log.error("resumeJob ex:", e);
        }
        return message;
    }

    @DeleteMapping("/deleteJob")
    public Object deleteJob(@RequestBody SchedulerJobInfo job) {
        log.info("params, job = {}", job);
        Message message = Message.failure();
        try {
            scheduleJobService.deleteJob(job);
            message = Message.success();
        } catch (Exception e) {
            message.setMsg(e.getMessage());
            log.error("deleteJob ex:", e);
        }
        return message;
    }

    @PostMapping("/userStatus")
    public Object userStat(@RequestBody SchedulerJobInfo job) {
        log.info("params, job ={}", job);
        Message message = Message.failure();
        try {
            scheduleJobService.userStatusJob(job);
            message = Message.success();
        } catch (Exception e) {
            message.setMsg(e.getMessage());
            log.error("deletedJob ex:", e);
        }
        return message;
    }
}
