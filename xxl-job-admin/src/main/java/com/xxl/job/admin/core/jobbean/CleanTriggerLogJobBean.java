package com.xxl.job.admin.core.jobbean;

import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.core.util.DateUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: seventeen
 * @Date: 2020/3/16 13:37
 * @description:
 */
public class CleanTriggerLogJobBean extends QuartzJobBean {


    private static ExecutorService executorService = new ThreadPoolExecutor(10, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE), new ThreadPoolExecutor.DiscardPolicy());


    private static Logger logger = LoggerFactory.getLogger(CleanTriggerLogJobBean.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info(">>>>> xxl-job, clean trigger log job...");
        Date time = new Date();
        int triggerLogRetentionDays = XxlJobAdminConfig.getAdminConfig().getTriggerLogRetentionDays();
        //triggerLogRetentionDays <= 0 不清理
        if (triggerLogRetentionDays <= 0) {
            return;
        }
        Date triggerTimeEnd = DateUtil.getMinusDayDate(time, triggerLogRetentionDays);
        List<XxlJobInfo> xxlJobInfos = XxlJobAdminConfig.getAdminConfig().getXxlJobInfoDao().pageList(0, Integer.MAX_VALUE, 0, null, null);
        for (XxlJobInfo xxlJobInfo : xxlJobInfos) {
            try {
                executorService.execute(() -> cleanJobLog(xxlJobInfo.getJobGroup(), xxlJobInfo.getId(), triggerTimeEnd));
            } catch (Exception e) {
                logger.error(">>>> xxl-job, clean trigger log error, jobGroup: {} jobId {} triggerTimeEnd {}", xxlJobInfo.getJobGroup(), xxlJobInfo.getId(), DateUtil.format(triggerTimeEnd));
            }
        }
    }

    private void cleanJobLog(int jobGroup, int jobId, Date triggerTimeEnd) {

        while (true) {
            List<XxlJobLog> xxlJobLogs = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().pageList(0, 10000, jobGroup, jobId, null, triggerTimeEnd, 0);
            if (CollectionUtils.isEmpty(xxlJobLogs)) {
                break;
            } else {
                for (XxlJobLog xxlJobLog : xxlJobLogs) {
                    XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().deleteById(xxlJobLog.getId());
                }
            }
        }

    }
}
