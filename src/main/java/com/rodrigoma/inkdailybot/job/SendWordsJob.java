package com.rodrigoma.inkdailybot.job;

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
import static kong.unirest.Unirest.post;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class SendWordsJob {

    private static final Logger logger = getLogger(SendWordsJob.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${word.url:#{null}}")
    private String randomWordUrl;

//    @Value("${word.header.host:#{null}}")
//    private String randomWordHost;
//
//    @Value("${word.header.key:#{null}}")
//    private String randomWordKey;

//    private static final String RANDOM_WORD_HEADER_HOST = "x-rapidapi-host";
//    private static final String RANDOM_WORD_HEADER_KEY = "x-rapidapi-key";

    @Value("${telegram.bot.sendmessage:#{null}}")
    private String telegramUrlSendMessage;

    @Value("${telegram.bot.token:#{null}}")
    private String telegramToken;

    @Value("#{new Long('${telegram.chat.id.test:0}')}")
    private Long telegramChatId;

    private static final String TELEGRAM_TO_REPLACE = "<REPLACE_TOKEN>";
    private static final String TELEGRAM_FIELD_CHAT_ID = "chat_id";
    private static final String TELEGRAM_FIELD_TEXT = "text";
    private static final String TELEGRAM_FIELD_PARSE_MODE = "parse_mode";
    private static final String TELEGRAM_VALUE_PARSE_MODE = "Markdown";

    private static final String FORMAT_DATE = "yyyyMMdd";

//    @Scheduled(cron = "0 43 12 * * ?", zone = "America/Sao_Paulo") // TODO TESTE
    @Scheduled(cron = "0 0 8 * * ?", zone = "America/Sao_Paulo")
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

        sendTelegramMessage(mountMessage(wordsToInk));
    }

    private String mountMessage(Set<String> wordsToink) {
        String message = "Bom Dia Hoomans!\n" +
                "Aqui estão os temas que selecionei para vcs\n" +
                "*Temas de hoje: " +
                join(" - ", wordsToink) +
                "*\n" +
                "Então bora desenhar!! \uD83C\uDFA8";

        return message;
    }

    private void sendTelegramMessage(String text) {
        logger.info("ChatID: {}, TOKEN: {}, URL: {} ", telegramChatId, telegramToken, telegramUrlSendMessage);
        post(telegramUrlSendMessage.replace(TELEGRAM_TO_REPLACE, telegramToken))
                .field(TELEGRAM_FIELD_CHAT_ID, telegramChatId)
                .field(TELEGRAM_FIELD_TEXT, text)
                .field(TELEGRAM_FIELD_PARSE_MODE, TELEGRAM_VALUE_PARSE_MODE)
                .asJson();
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
        return word;
    }
}