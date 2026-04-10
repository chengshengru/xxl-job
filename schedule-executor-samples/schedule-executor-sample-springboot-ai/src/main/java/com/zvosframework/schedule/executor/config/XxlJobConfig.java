package com.zvosframework.schedule.executor.config;

import com.zvosframework.schedule.core.handler.annotation.ScheduleSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Configuration
public class XxlJobConfig {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

    @Value("${schedule.admin.addresses}")
    private String adminAddresses;

    @Value("${schedule.admin.accessToken}")
    private String accessToken;

    @Value("${schedule.admin.timeout}")
    private int timeout;

    @Value("${schedule.executor.enabled}")
    private Boolean enabled;

    @Value("${schedule.executor.appname}")
    private String appname;

    @Value("${schedule.executor.address}")
    private String address;

    @Value("${schedule.executor.ip}")
    private String ip;

    @Value("${schedule.executor.port}")
    private int port;

    @Value("${schedule.executor.logpath}")
    private String logPath;

    @Value("${schedule.executor.logretentiondays}")
    private int logRetentionDays;

    @Value("${schedule.executor.excludedpackage}")
    private String excludedPackage;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        logger.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setTimeout(timeout);
        xxlJobSpringExecutor.setEnabled(enabled);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        xxlJobSpringExecutor.setExcludedPackage(excludedPackage);

        return xxlJobSpringExecutor;
    }

}