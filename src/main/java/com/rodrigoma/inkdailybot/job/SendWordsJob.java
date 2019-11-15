package com.rodrigoma.inkdailybot.job;

import com.rodrigoma.inkdailybot.services.Pictionary;
import com.rodrigoma.inkdailybot.services.Redis;
import com.rodrigoma.inkdailybot.services.Telegram;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.lang.String.join;
import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class SendWordsJob {

    private static final Logger logger = getLogger(SendWordsJob.class);

    private final Redis redis;
    private final Telegram telegram;
    private final Pictionary pictionary;

    private static final String FORMAT_DATE = "yyyyMMdd";

    @Autowired
    public SendWordsJob(Redis redis, Telegram telegram, Pictionary pictionary) {
        this.redis = redis;
        this.telegram = telegram;
        this.pictionary = pictionary;
    }

    public void send() {
        logger.info("Bot rodando...");

        String fmtToday = now().format(ofPattern(FORMAT_DATE));

        logger.info("Hoje eh {}...", fmtToday);

        Set<String> wordsToInk;

        if (redis.hasKey(fmtToday)) {
            logger.info("KEY encontrada...");
            wordsToInk = redis.retriveWords(fmtToday);
        } else {
            logger.info("KEY não encontrada...");
            wordsToInk = pictionary.getRandomWord(fmtToday);
        }

        telegram.sendMessage(mountMessage(wordsToInk));
    }

    private String mountMessage(Set<String> wordsToink) {
        return redis.retriveMessage(join(" - ", wordsToink));
    }
}