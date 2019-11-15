package com.rodrigoma.inkdailybot;

import com.rodrigoma.inkdailybot.job.SendWordsJob;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import static java.lang.System.exit;
import static java.util.Arrays.sort;
import static java.util.Arrays.stream;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class InkDailyBotApp {

    private static final Logger logger = getLogger(InkDailyBotApp.class);

    public static void main(String[] args) {
        ApplicationContext context = run(InkDailyBotApp.class, args);

        String[] beanNames = context.getBeanDefinitionNames();
        sort(beanNames);

        logger.info("========== BEANS ==========");
        stream(beanNames).forEachOrdered(beanName -> logger.info("BEAN: {}", beanName));
        logger.info("========== BEANS ==========");

        SendWordsJob sendWordsJob = context.getBean(SendWordsJob.class);
        sendWordsJob.send();

        exit(0);

        //TODO se for deixar isso open source
        // verificar no init se as keys no redis existe, não existindo, criar as keys
    }
}