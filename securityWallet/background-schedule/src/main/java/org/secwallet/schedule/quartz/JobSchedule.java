package org.secwallet.schedule.quartz;


import org.secwallet.schedule.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobSchedule implements CommandLineRunner {


    @Autowired
    private QuartzService taskService;

    /**
     * task start
     * @param strings
     * @throws Exception
     */
    @Override
    public void run(String... strings) throws Exception {
        System.out.println("start==============start");
        taskService.timingTask();
        System.out.println("end==============end");
    }
}
