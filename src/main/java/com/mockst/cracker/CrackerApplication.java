package com.mockst.cracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/16 22:59
 * @Description:
 */
@EnableJpaAuditing
@SpringBootApplication
public class CrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrackerApplication.class, args);
    }

}
