package org.secwallet.schedule.quartz;

import org.secwallet.schedule.model.ScheduleJob;
import org.secwallet.schedule.util.SpringUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class QuartzFactory implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //get schedule data
        ScheduleJob scheduleJob = (ScheduleJob) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        //get Bean
        Object object = SpringUtil.getBean(scheduleJob.getExecuteClassName());
        try {
            //Execute the corresponding method using the launch
            Method method = object.getClass().getMethod(scheduleJob.getExecuteMethodName());
            method.invoke(object);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
