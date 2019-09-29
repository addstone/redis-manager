package com.newegg.ec.redis.config;

import com.google.common.base.Strings;
import com.newegg.ec.redis.exception.ConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

import static com.newegg.ec.redis.util.RedisUtil.CLUSTER;
import static com.newegg.ec.redis.util.RedisUtil.STANDALONE;

/**
 * @author Jay.H.Zou
 * @date 7/6/2019
 */
@Configuration
public class SystemConfig implements WebMvcConfigurer {

    public static final String CONFIG_ORIGINAL_PATH = "/redis-manager/config/";

    public static final String MACHINE_PACKAGE_ORIGINAL_PATH = "/redis-manager/machine/";

    @Value("${server.port}")
    private int serverPort;

    @Value("${redis-manager.install.conf-path}")
    private String configPath;

    @Value("${redis-manager.install.machine.package-path}")
    private String machinePackagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (Strings.isNullOrEmpty(configPath)) {
            throw new ConfigurationException("conf-path is empty.");
        }
        File file = new File(configPath);
        if (!file.isDirectory()) {
            throw new ConfigurationException(configPath + " is not a directory.");
        }
        if (!file.exists()) {
            File clusterConfDir = new File(configPath + CLUSTER);
            clusterConfDir.mkdirs();
            File standaloneConfDir = new File(configPath + STANDALONE);
            standaloneConfDir.mkdirs();
        }

        if (Strings.isNullOrEmpty(machinePackagePath)) {
            throw new ConfigurationException("machine.package-path is empty.");
        }
        File file2 = new File(machinePackagePath);
        if (!file2.isDirectory()) {
            throw new ConfigurationException(machinePackagePath + " is not a directory.");
        }
        if (!file2.exists()) {
            file2.mkdirs();
        }

        registry.addResourceHandler(CONFIG_ORIGINAL_PATH + "**").addResourceLocations("file:" + configPath);
        registry.addResourceHandler(MACHINE_PACKAGE_ORIGINAL_PATH + "**").addResourceLocations("file:" + machinePackagePath);
    }

    public int getServerPort() {
        return serverPort;
    }
}
