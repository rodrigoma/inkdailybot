package com.rodrigoma.inkdailybot.webhook;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class WebHook {

    private static final Logger logger = getLogger(WebHook.class);

    @RequestMapping(value = "/webhooks", method = POST, consumes = {APPLICATION_JSON_VALUE})
    public @ResponseBody ResponseEntity receiveUpdate(@RequestBody final String update) {
        JsonObject jUpdate = JsonParser.parseString(update).getAsJsonObject();
        logger.info("Mensagem recebida! {}", jUpdate.toString());

        if (jUpdate.getAsJsonObject("message").has("photo")) {
            logger.info("Tem node Photo");
        } else {
            logger.info("NÃO tem node Photo");
        }

        return new ResponseEntity(OK);
    }
}
