package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.executor.core.util.CommandUtil;
import org.springframework.stereotype.Component;

/**
 * @auther: xia_xun
 * @Date: 2019/3/6
 * @description:
 */
@JobHandler(value = "commandJobHandler")
@Component
public class CommandJobHandler extends IJobHandler {

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        return CommandUtil.process(param);
    }
}
