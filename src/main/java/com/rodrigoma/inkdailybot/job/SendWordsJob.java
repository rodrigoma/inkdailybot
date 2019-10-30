package com.rodrigoma.inkdailybot.job;

import com.rodrigoma.inkdailybot.services.BotMessage;
import com.rodrigoma.inkdailybot.services.Telegram;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static java.lang.String.join;
import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static kong.unirest.Unirest.get;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class SendWordsJob {

    private static final Logger logger = getLogger(SendWordsJob.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BotMessage botMessage;

    @Autowired
    private Telegram telegram;

    @Value("${word.url:#{null}}")
    private String randomWordUrl;

//    @Value("${word.header.host:#{null}}")
//    private String randomWordHost;
//
//    @Value("${word.header.key:#{null}}")
//    private String randomWordKey;

//    private static final String RANDOM_WORD_HEADER_HOST = "x-rapidapi-host";
//    private static final String RANDOM_WORD_HEADER_KEY = "x-rapidapi-key";

    private static final String FORMAT_DATE = "yyyyMMdd";

//    @Scheduled(cron = "0 43 12 * * ?", zone = "America/Sao_Paulo") // TODO TESTE
    @Scheduled(cron = "0 40 8 * * *", zone = "America/Sao_Paulo")
    public void send() {
        logger.info("Bot rodando...");

        String fmtToday = now().format(ofPattern(FORMAT_DATE));

        logger.info("Hoje eh {}...", fmtToday);

        Set<String> wordsToInk;

        if (stringRedisTemplate.hasKey(fmtToday)) {
            logger.info("KEY encontrada...");
            wordsToInk = stringRedisTemplate.opsForSet().members(fmtToday);
        } else {
            logger.info("KEY não encontrada...");
            wordsToInk = get3Words(fmtToday);
        }

        telegram.sendMessage(mountMessage(wordsToInk));
    }

    private String mountMessage(Set<String> wordsToink) {
        return botMessage.retriveMessage(join(" - ", wordsToink));
    }

    private Set<String> get3Words(String fmtToday) {
        logger.info("Gerando palavras para hoje...");

        Set<String> wordsToInk = new HashSet<>();

        do {
            String word = getRandomWord();

            if (word != null) {
                wordsToInk.add(word);
                stringRedisTemplate.opsForSet().add(fmtToday, word);
                logger.info("Palavra {} gerada", wordsToInk.size());
            }
        } while (wordsToInk.size() < 3);

        return wordsToInk;
    }

    private String getRandomWord() {
        logger.info("Buscando nova palavra...");
        HttpResponse<JsonNode> response = get(randomWordUrl).asJson();

        if (response.getStatus() != 200) {
            logger.info("Servico {} retornou null", randomWordUrl);
            return null;
        }

        String word = response.getBody().getArray().getString(0);

        logger.info("Nova palavra: {}", word);
        return word.toUpperCase();
    }
}