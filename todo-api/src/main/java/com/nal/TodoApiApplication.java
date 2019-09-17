package com.nal;

import com.nal.core.repository.impl.GenericJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.nal")
@EnableJpaRepositories(repositoryBaseClass = GenericJpaRepository.class, basePackages = "com.nal.core.repository")
@EnableTransactionManagement
@EntityScan(basePackages = "com.nal.core.entity")
@Slf4j
public class TodoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoApiApplication.class, args);
    }

}
