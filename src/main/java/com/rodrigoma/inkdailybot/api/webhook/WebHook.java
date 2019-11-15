package com.rodrigoma.inkdailybot.api.webhook;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rodrigoma.inkdailybot.services.Redis;
import com.rodrigoma.inkdailybot.services.Telegram;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class WebHook {

    private static final Logger logger = getLogger(WebHook.class);

    private final Redis redis;
    private final Telegram telegram;

    @Autowired
    public WebHook(Redis redis, Telegram telegram) {
        this.redis = redis;
        this.telegram = telegram;
    }

    @RequestMapping(value = "/webhooks", method = POST, consumes = {APPLICATION_JSON_VALUE})
    public @ResponseBody ResponseEntity receiveUpdate(@RequestBody final String update) {
        JsonObject jUpdate = JsonParser.parseString(update).getAsJsonObject();
        logger.info("Mensagem recebida! {}", jUpdate.toString());

        logger.info("Teste data: {}", LocalDateTime.now().toString());

        if (jUpdate.getAsJsonObject("message").has("photo")) {
            int msgId = jUpdate.getAsJsonObject("message").get("message_id").getAsInt();
            String user = jUpdate.getAsJsonObject("message").getAsJsonObject("from").get("first_name").getAsString();
            logger.info("MSG-ID: {}, USER: {}", msgId, user);

            telegram.replyMessage(redis.retriveMotivation(user), msgId);
        }

        return new ResponseEntity(OK);
    }
}