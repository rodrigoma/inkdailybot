package com.rodrigoma.inkdailybot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import static java.util.Arrays.sort;
import static java.util.Arrays.stream;
import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableScheduling
public class InkDailyBotApp {

    private static final Logger logger = LoggerFactory.getLogger(InkDailyBotApp.class);

    public static void main(String[] args) {
        ApplicationContext context = run(InkDailyBotApp.class, args);

        String[] beanNames = context.getBeanDefinitionNames();
        sort(beanNames);

        logger.info("========== BEANS ==========");
        stream(beanNames).forEachOrdered(beanName -> logger.info("BEAN: {}", beanName));
        logger.info("========== BEANS ==========");
    }
}