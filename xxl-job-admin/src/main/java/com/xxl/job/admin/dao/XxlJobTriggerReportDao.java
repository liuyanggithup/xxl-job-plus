package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobTriggerReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: seventeen
 * @Date: 2020/3/17 16:05
 * @description:
 */
@Mapper
public interface XxlJobTriggerReportDao {

    List<XxlJobTriggerReport> selectByDay(@Param("startDay") String startDay,
                                          @Param("endDay") String endDay);

    int delete(@Param("day") String day);

    void save(XxlJobTriggerReport xxlJobTriggerReport);
}
