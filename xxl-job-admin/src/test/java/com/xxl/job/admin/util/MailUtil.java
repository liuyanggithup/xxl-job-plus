package com.xxl.job.admin.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtil {
    private static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    /**
     * @param toAddress   收件人邮箱
     * @param mailSubject 邮件主题
     * @param mailBody    邮件正文
     * @return
     */
    public static boolean sendMail(String toAddress, String mailSubject, String mailBody) {

        try {
            // Create the email message
            HtmlEmail email = new HtmlEmail();

            //email.setDebug(true);		// 将会打印一些log
            //email.setTLS(true);		// 是否TLS校验，，某些邮箱需要TLS安全校验，同理有SSL校验
            //email.setSSL(true);

            email.setHostName("smtp.exmail.qq.com");

            email.setSslSmtpPort("465");
            email.setSSLOnConnect(true);


            email.setAuthenticator(new DefaultAuthenticator("***", "***"));
            email.setCharset("UTF-8");

            email.setFrom("xia_xun@hunliji.com","测试");
            email.addTo(toAddress);
            email.setSubject(mailSubject);
            email.setMsg(mailBody);

            //email.attach(attachment);	// add the attachment

            email.send();                // send the email
            return true;
        } catch (EmailException e) {
            logger.error(e.getMessage(), e);

        }
        return false;
    }


    public static void main(String[] args) {

        MailUtil.sendMail("yin_gu@hunliji.com","测试","测试");


    }

}
