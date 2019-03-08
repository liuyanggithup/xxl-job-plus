package com.xxl.job.executor.core.util;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandUtil {

    public static final Logger logger = LoggerFactory.getLogger(CommandUtil.class);

    public static String[] parseCommand(String command) {
        String[] args = Arrays.stream(command.split(" ")).map(e -> e.trim()).collect(Collectors.toList()).toArray(new String[]{});
        return args;
    }


    public static String getOptionValue(String option, String command) {
        String[] args = parseCommand(command);
        String optionValue = null;
        for (int i = 0; i < args.length - 1; i++) {
            if (Objects.equals(args[i], option)) {
                optionValue = args[i + 1];
                break;
            }
        }
        return optionValue;
    }


    public static ReturnT<String> process(String command) throws Exception {
        int exitValue = -1;
        BufferedReader bufferedReader = null;
        try {
            // command process
            Process process = Runtime.getRuntime().exec(command);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

            // command log
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                XxlJobLogger.log(line);
            }
            // command exit
            process.waitFor();
            exitValue = process.exitValue();
        } catch (Exception e) {
            XxlJobLogger.log(e);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        if (exitValue == 0) {
            return IJobHandler.SUCCESS;
        } else {
            return new ReturnT<String>(IJobHandler.FAIL.getCode(), "curl exit value(" + exitValue + ") is failed");
        }

    }

}
