package com.xxl.job.executor.service.jobhandler;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.core.config.XxlJobConfig;
import com.xxl.job.executor.core.util.CommandUtil;
import com.xxl.job.executor.core.util.Md5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@JobHandler(value = "curlJobHandler")
@Component
public class CurlJobHandler extends IJobHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurlJobHandler.class);

    @Resource
    private XxlJobConfig xxlJobConfig;


    @Override
    public ReturnT<String> execute(String param) throws Exception {

        LOGGER.info("Curl Job Start,param {}", param);
        XxlJobLogger.log("Curl Job Start,param {}", param);
        if (StringUtils.isEmpty(param)) {
            XxlJobLogger.log("参数不能为空，请设置");
            return new ReturnT<String>(IJobHandler.FAIL.getCode(), "参数不能为空，请设置");
        }

        //处理换行符
        param = param.replace("\n","");

        String signKey = xxlJobConfig.getSignKey();
        Long timestamp = System.currentTimeMillis();
        String sign = Md5Utils.md5(timestamp+signKey);
        if(param.indexOf("?") == -1){
            param += "?timestamp="+timestamp+"&sign="+sign;
        }else {
            param += "&timestamp="+timestamp+"&sign="+sign;
        }

        String command = "curl ";
        String m = CommandUtil.getOptionValue("-m", param);
        if (StringUtils.isEmpty(m)) {
            command += "-m 1200 ";
        }

        String connectTimeout = CommandUtil.getOptionValue("--connect-timeout", param);
        if (StringUtils.isEmpty(connectTimeout)) {
            command += "--connect-timeout 1200 ";
        }

        command += param;


        ReturnT<String> resultT = CommandUtil.execCMD(command,86400);
        XxlJobLogger.log("Curl Job End,param {},command {} result {}",param, command, JSON.toJSONString(resultT));
        LOGGER.info("Curl Job End,param {},command {} result {}",param, command, JSON.toJSONString(resultT));

        return resultT;
    }


}
