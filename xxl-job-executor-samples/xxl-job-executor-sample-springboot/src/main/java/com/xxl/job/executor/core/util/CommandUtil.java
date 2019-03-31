package com.xxl.job.executor.core.util;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
/**
 * CommandUtil
 * @author seventeen
 * @date 2019/03/26
 */
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

    /**
     * 命令执行
     *
     * @param cmd
     * @param timeout
     * @return
     */
    public static ReturnT<String> execCMD(String cmd, int timeout) {

        int exitValue = -1;
        Process process = null;
        StringBuilder sbStd = new StringBuilder();
        StringBuilder sbErr = new StringBuilder();

        long start = System.currentTimeMillis() / 1000;
        try {
            process = Runtime.getRuntime().exec(cmd);
            BufferedReader brStd = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader brErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;

            while (true) {
                if (brStd.ready()) {
                    line = brStd.readLine();
                    sbStd.append(line + "\n");
                    continue;
                }
                if (brErr.ready()) {
                    line = brErr.readLine();
                    sbErr.append(line + "\n");
                    continue;
                }

                if (process != null) {
                    try {
                        exitValue = process.exitValue();
                        break;
                    } catch (IllegalThreadStateException e) {
                    }
                }

                if (System.currentTimeMillis() / 1000 - start > timeout) {
                    sbErr.append("\n命令执行超时退出.");
                    break;
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException e) {
        } finally {
            if (process != null) {
                process.destroy();
            }
        }

        if (sbErr.length() > 0) {
            XxlJobLogger.log("\n" + sbErr.toString());
        }
        if (sbStd.length() > 0) {
            XxlJobLogger.log("\n" + sbStd.toString());
        }
        if (exitValue == 0) {
            return IJobHandler.SUCCESS;
        } else {
            return new ReturnT<String>(IJobHandler.FAIL.getCode(), "curl exit value(" + exitValue + ") is failed");
        }

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
            if (e instanceof InterruptedException) {
                throw e;
            }
            return new ReturnT<String>(IJobHandler.FAIL.getCode(), e.getMessage());
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
