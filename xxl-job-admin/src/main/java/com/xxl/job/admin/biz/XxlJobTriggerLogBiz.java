package com.xxl.job.admin.biz;

import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.core.model.XxlJobTriggerReport;
import com.xxl.job.core.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: seventeen
 * @Date: 2020/3/17 19:21
 * @description:
 */
public class XxlJobTriggerLogBiz {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobTriggerLogBiz.class);

    public static XxlJobTriggerReport triggerCountByDay(Date date) {
        String day = DateUtil.format(date, DateUtil.DAY_DATE_FORMAT);
        Date startDate;
        Date endDate;
        try {
            startDate = DateUtil.parse(day + " 00:00:00");
            endDate = DateUtil.parse(day + " 23:59:59");
        } catch (ParseException e) {
            logger.error("", e);
            throw new RuntimeException("date pare exception");
        }
        List<Map<String, Object>> triggerCountMapAll = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().triggerCountByDay(startDate, endDate);
        if (CollectionUtils.isNotEmpty(triggerCountMapAll)) {
            Map<String, Object> item = triggerCountMapAll.get(0);
            String triggerDay = String.valueOf(item.get("triggerDay"));
            int triggerDayCount = Integer.valueOf(String.valueOf(item.get("triggerDayCount")));
            int triggerDayCountRunning = Integer.valueOf(String.valueOf(item.get("triggerDayCountRunning")));
            int triggerDayCountSuc = Integer.valueOf(String.valueOf(item.get("triggerDayCountSuc")));
            int triggerDayCountFail = triggerDayCount - triggerDayCountRunning - triggerDayCountSuc;
            XxlJobTriggerReport xxlJobTriggerReport = new XxlJobTriggerReport();
            xxlJobTriggerReport.setDay(triggerDay);
            xxlJobTriggerReport.setTriggerDayCount(triggerDayCount);
            xxlJobTriggerReport.setTriggerDayCountFail(triggerDayCountFail);
            xxlJobTriggerReport.setTriggerDayCountRunning(triggerDayCountRunning);
            xxlJobTriggerReport.setTriggerDayCountSuc(triggerDayCountSuc);
            return xxlJobTriggerReport;
        }

        return null;
    }

}
