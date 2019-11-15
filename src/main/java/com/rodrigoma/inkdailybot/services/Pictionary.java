package com.rodrigoma.inkdailybot.services;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static kong.unirest.Unirest.post;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class Pictionary {

    private static final Logger logger = getLogger(Pictionary.class);

    private final Redis redis;

    @Value("${pictionary.url:#{null}}")
    private String pictionaryUrl;

    @Value("${pictionary.count:3}")
    private String pictionaryCount;

    @Value("${pictionary.game:2}")
    private String pictionaryGame;

    @Value("${pictionary.category:8}")
    private String pictionaryCategory;

    @Autowired
    public Pictionary(Redis redis) {
        this.redis = redis;
    }

    public Set<String> getRandomWord(String fmtToday) {
        logger.info("Buscando nova palavra...");

        StringBuilder body = new StringBuilder("category=").append(pictionaryCategory)
                .append("&game=").append(pictionaryGame)
                .append("&count=").append(pictionaryCount);

        HttpResponse<JsonNode> response = post(pictionaryUrl)
                .header("x-csrf-token", "9Myyzp_gqpULlBXM7C3k25i0Idjl_n6aUnma-eoBiUiB_9Cf26HFxTnRdPqVSJ2L38J766ynF_MbCPyfpXfifQ==")
                .header("x-requested-with", "XMLHttpRequest")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("cookie", "__cfduid=dbbbe943d9da360ff4ddda5e2deb8ca151571852341; PHPSESSID=7abc1a11a14b41bf218f975cccc31d1d; _csrf=ec5cbe4d4bcb44f9d2bc9a0cf4b82a9cf4948e9099704f26f0bc1ca5cbd96958a%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22u3bQDAoP2Ea6yeyPGvZ3IYiiIqffOvk5%22%3B%7D; _ga=GA1.2.1474574971.1571852343; _gid=GA1.2.1696400120.1573741453; _gat=1")
                .body(body.toString())
                .asJson();

        if (response.getStatus() != 200) {
            logger.warn("Servico {} retornou null", pictionaryUrl);
            return null;
        }

        Set<String> wordsToInk = new HashSet<>();

        response.getBody().getArray().forEach(o -> {
            String word = ((String) o).toUpperCase();
            wordsToInk.add(word);
            redis.addWord(fmtToday, word);
            logger.info("Palavra {} gerada", word);

        });

        return wordsToInk;
    }
}