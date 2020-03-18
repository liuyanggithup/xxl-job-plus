package com.xxl.job.admin.core.schedule;

import com.xxl.job.admin.core.jobbean.CleanTriggerLogJobBean;
import com.xxl.job.admin.core.jobbean.TriggerReportJobBean;
import com.xxl.job.core.util.DateUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author: seventeen
 * @Date: 2020/3/16 10:40
 * @description:
 */
public final class XxlJobSelfScheduler {


    private static final Logger logger = LoggerFactory.getLogger(XxlJobSelfScheduler.class);

    // ---------------------- addCleanTriggerLogJob ----------------------

    /**
     * scheduler
     */
    private static Scheduler scheduler;

    private static void addCleanTriggerLogJob() throws SchedulerException {

        String jobName = "cleanTriggerLogJobHandler";
        String jobGroup = "xxl-job-admin-self-scheduler-default";
        //1小时清理一次
        String cronExpression = "23 23 0/1 * * ?";
        Class<? extends Job> jobClass_ = CleanTriggerLogJobBean.class;
        addCronJob(jobName, jobGroup, cronExpression, jobClass_);

    }

    private static void addTriggerReportJob() throws SchedulerException{
        String jobName = "triggerReportJobHandler";
        String jobGroup = "xxl-job-admin-self-scheduler-default";
        //1小时清理一次
        String cronExpression = "5 0 0,6,12,18,23 * * ?";
        Class<? extends Job> jobClass_ = TriggerReportJobBean.class;
        addCronJob(jobName, jobGroup, cronExpression, jobClass_);
    }

    private static void addCronJob(String jobName, String jobGroup, String cronExpression, Class<? extends Job> jobClass_) throws SchedulerException {
        // 1、job key
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        JobKey jobKey = new JobKey(jobName, jobGroup);

        // 2、valid
        if (scheduler.checkExists(triggerKey)) {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            Class<? extends Job> oldJobClass_ = jobDetail.getJobClass();
            if (trigger.getCronExpression().equals(cronExpression) && jobClass_ == oldJobClass_) {
                return;
            }
            scheduler.unscheduleJob(triggerKey);
            logger.info(">>>>>>>>>>> removeJob success, triggerKey:{}", triggerKey);
        }

        // 3、corn trigger
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
        // withMisfireHandlingInstructionDoNothing 忽略掉调度终止过程中忽略的调度
        CronTrigger cronTrigger = TriggerBuilder
                .newTrigger().withIdentity(triggerKey)
                .withSchedule(cronScheduleBuilder)
                .build();

        // 4、job detail
        JobDetail jobDetail = JobBuilder.newJob(jobClass_).withIdentity(jobKey).build();
        Date date = scheduler.scheduleJob(jobDetail, cronTrigger);
        logger.info(">>>>>>>>>>> addJob success, jobDetail:{}, cronTrigger:{}, date:{}", jobDetail, cronTrigger, DateUtil.format(date));
    }


    public void setScheduler(Scheduler scheduler) {
        XxlJobSelfScheduler.scheduler = scheduler;
    }

    public void start() throws Exception {

        logger.info(">>>>>>>>>>>>XxlJobSelfScheduler, add clean trigger log job...");
        addCleanTriggerLogJob();
        logger.info(">>>>>>>>>>>>XxlJobSelfScheduler, add trigger report job...");
        addTriggerReportJob();

    }


}
