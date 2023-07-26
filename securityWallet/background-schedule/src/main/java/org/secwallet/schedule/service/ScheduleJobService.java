package org.secwallet.schedule.service;



import org.secwallet.core.service.AbstractService;
import org.secwallet.schedule.constant.ScheduleStatus;
import org.secwallet.schedule.dao.ScheduleJobMapper;
import org.secwallet.schedule.model.ScheduleJob;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ScheduleJobService extends AbstractService<ScheduleJob,String> {

    @Autowired
    private QuartzService quartzService;
    @Autowired
    private ScheduleJobMapper scheduleJobMapper;

    /**
     * Add scheduled task
     *
     * @param job
     */
    public void add(ScheduleJob job) {
        this.insert(job);
        try {
            quartzService.addJob(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Start a scheduled task
     *
     * @param id 任务id
     */
    public void start(String id) {
        ScheduleJob job = this.findById(id);
        job.setStatus(ScheduleStatus.START.getValue());
        scheduleJobMapper.updateStatus(id,ScheduleStatus.START.getValue());
        try {
            quartzService.operateJob(ScheduleStatus.START, job);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pause a scheduled task
     *
     * @param id job id
     */
    public void pause(String id) {
        ScheduleJob job = this.findById(id);
        job.setStatus(ScheduleStatus.PAUSE.getValue());
        scheduleJobMapper.updateStatus(id,ScheduleStatus.PAUSE.getValue());
        try {
            quartzService.operateJob(ScheduleStatus.PAUSE, job);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete scheduled task
     *
     * @param id job id
     */
    public void delete(String id) {
        ScheduleJob job = this.findById(id);
        this.deleteByPrimaryKey(id);
        try {
            quartzService.operateJob(ScheduleStatus.DELETE, job);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start all scheduled tasks
     */
    public void startAllJob() {
        ScheduleJob job = new ScheduleJob();
        job.setStatus(ScheduleStatus.START.getValue());
        scheduleJobMapper.updateStatus(null,ScheduleStatus.START.getValue());
        try {
            quartzService.startAllJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pause all scheduled tasks
     */
    public void pauseAllJob() {
        ScheduleJob job = new ScheduleJob();
        job.setStatus(ScheduleStatus.PAUSE.getValue());
        scheduleJobMapper.updateStatus(null, ScheduleStatus.PAUSE.getValue());
        try {
            quartzService.pauseAllJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
