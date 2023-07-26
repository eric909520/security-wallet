package org.secwallet.schedule.service;


import org.secwallet.schedule.constant.ScheduleStatus;
import org.secwallet.schedule.quartz.QuartzFactory;
import org.secwallet.schedule.model.ScheduleJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuartzService {


    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleJobService scheduleJobService;


    public void timingTask() {

        List<ScheduleJob> scheduleJobs = scheduleJobService.findAll(null);
        if (scheduleJobs != null) {
            scheduleJobs.forEach(this::addJob);
        }
    }

    public void addJob(ScheduleJob job) {
        try {
            //create trigger
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getTaskName())
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getTriggerCronExpression()))
                    .startNow()
                    .build();

            //create task
            JobDetail jobDetail = JobBuilder.newJob(QuartzFactory.class)
                    .withIdentity(job.getTaskName())
                    .build();

            //Incoming scheduling data, which needs to be used in QuartzFactory
            jobDetail.getJobDataMap().put("scheduleJob", job);

            //Schedule jobs
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void operateJob(ScheduleStatus scheduleStatus, ScheduleJob job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getTaskName());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
        }
        switch (scheduleStatus) {
            case START:
                scheduler.resumeJob(jobKey);
                break;
            case PAUSE:
                scheduler.pauseJob(jobKey);
                break;
            case DELETE:
                scheduler.deleteJob(jobKey);
                break;
        }
    }


    public void startAllJob() throws SchedulerException {
        scheduler.start();
    }


    public void pauseAllJob() throws SchedulerException {
        scheduler.standby();
    }
}
