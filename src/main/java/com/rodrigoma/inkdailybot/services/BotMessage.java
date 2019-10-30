package com.rodrigoma.inkdailybot.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BotMessage {

    private static final Logger logger = getLogger(BotMessage.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String MESSAGE_KEY = "message";
    private static final String REPLACE_KEYWORD = "<WORDS>";

    public String retriveMessage(String toReplace) {
        logger.info("Buscando o texto da mensagem do Bot");
        return stringRedisTemplate
                .opsForValue()
                .get(MESSAGE_KEY)
                .replace(REPLACE_KEYWORD, toReplace);
    }
}