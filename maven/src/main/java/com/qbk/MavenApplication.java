package com.qbk;

import com.qbk.util.DefaultProfileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * maven 和 spring 多环境结合
 */
@Slf4j
@SpringBootApplication
public class MavenApplication {

    public static void main(String[] args) throws UnknownHostException {
        //SpringApplication.run(MavenApplication.class, args);

        //启动参数：
        //Program arguments: --spring.profiles.active=test

        //打包命令：
        //mvn clean package -P test

        SpringApplication app = new SpringApplication(MavenApplication.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t"
                        + "Local: \t\thttp://localhost:{}\n\t"
                        + "External: \thttp://{}:{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"), env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port"));
    }

}
