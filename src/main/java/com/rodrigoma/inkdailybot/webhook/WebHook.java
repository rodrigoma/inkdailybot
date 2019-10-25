package com.rodrigoma.inkdailybot.webhook;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static kong.unirest.Unirest.post;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class WebHook {

    private static final Logger logger = getLogger(WebHook.class);

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

    @RequestMapping(value = "/webhooks", method = POST, consumes = {APPLICATION_JSON_VALUE})
    public @ResponseBody ResponseEntity receiveUpdate(@RequestBody final String update) {
        JsonObject jUpdate = JsonParser.parseString(update).getAsJsonObject();
        logger.info("Mensagem recebida! {}", jUpdate.toString());

        logger.info("Teste data: {}", LocalDateTime.now().toString());

        if (jUpdate.getAsJsonObject("message").has("photo")) {
            int msgId = jUpdate.getAsJsonObject("message").get("message_id").getAsInt();
            String user = jUpdate.getAsJsonObject("message").getAsJsonObject("from").get("first_name").getAsString();
            logger.info("MSG-ID: {}, USER: {}", msgId, user);
            sendTelegramMessage("Legal a imagem " + user, msgId);
        }

        return new ResponseEntity(OK);
    }

    private void sendTelegramMessage(String text, int replyMsgId) {
        post(telegramUrlSendMessage.replace(TELEGRAM_TO_REPLACE, telegramToken))
                .field(TELEGRAM_FIELD_CHAT_ID, telegramChatId)
                .field(TELEGRAM_FIELD_TEXT, text)
                .field(TELEGRAM_FIELD_PARSE_MODE, TELEGRAM_VALUE_PARSE_MODE)
                .field("reply_to_message_id", String.valueOf(replyMsgId))
                .asJson();
    }
}
