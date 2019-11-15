package com.rodrigoma.inkdailybot.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class Redis {

    private static final Logger logger = getLogger(Redis.class);

    private final StringRedisTemplate stringRedisTemplate;

    private static final String MESSAGE_KEY = "message";
    private static final String MESSAGE_REPLACE_KEYWORD = "<WORDS>";

    private static final String MOTIVATION_KEY = "motivation";
    private static final String MOTIVATION_REPLACE_KEYWORD = "NOME";

    @Autowired
    public Redis(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void addWord(String fmtToday, String word) {
        stringRedisTemplate
                .opsForSet()
                .add(fmtToday, word);
    }

    public boolean hasKey(String fmtToday) {
        return stringRedisTemplate
                .hasKey(fmtToday);
    }

    public Set<String> retriveWords(String fmtToday) {
        logger.info("Pegando palavras do dia {}", fmtToday);
        return stringRedisTemplate
                .opsForSet()
                .members(fmtToday);
    }

    public String retriveMessage(String toReplace) {
        logger.info("Buscando o texto da mensagem do Bot");
        return stringRedisTemplate
                .opsForValue()
                .get(MESSAGE_KEY)
                .replace(MESSAGE_REPLACE_KEYWORD, toReplace);
    }

    public String retriveMotivation(String userToReplace) {
        logger.info("Buscando o texto de motivacao");
        return stringRedisTemplate
                .opsForSet()
                .randomMember(MOTIVATION_KEY)
                .replace(MOTIVATION_REPLACE_KEYWORD, userToReplace);
    }
}