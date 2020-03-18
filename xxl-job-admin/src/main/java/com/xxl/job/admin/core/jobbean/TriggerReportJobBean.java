package com.xxl.job.admin.core.jobbean;

import com.xxl.job.admin.biz.XxlJobTriggerLogBiz;
import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.core.model.XxlJobTriggerReport;
import com.xxl.job.admin.dao.XxlJobTriggerReportDao;
import com.xxl.job.core.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

/**
 * @author: seventeen
 * @Date: 2020/3/17 18:03
 * @description:
 */
public class TriggerReportJobBean extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(TriggerReportJobBean.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info(">>>>> xxl-job, trigger report job...");
        Date date = DateUtil.getMinusDayDate(new Date(), 1);
        XxlJobTriggerReport xxlJobTriggerReport = XxlJobTriggerLogBiz.triggerCountByDay(date);
        if (xxlJobTriggerReport == null) {
            return;
        }
        XxlJobTriggerReportDao xxlJobTriggerReportDao = XxlJobAdminConfig.getAdminConfig().getXxlJobTriggerReportDao();
        String day = DateUtil.format(date, DateUtil.DAY_DATE_FORMAT);
        List<XxlJobTriggerReport> xxlJobTriggerReports = xxlJobTriggerReportDao.selectByDay(day, day);
        if (!CollectionUtils.isEmpty(xxlJobTriggerReports)) {
            xxlJobTriggerReportDao.delete(day);
        }
        xxlJobTriggerReportDao.save(xxlJobTriggerReport);

    }
}
