package com.rodrigoma.inkdailybot.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.TRUE;
import static java.lang.String.valueOf;
import static kong.unirest.Unirest.post;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class Telegram {

    private static final Logger logger = getLogger(Telegram.class);

    private static final String TELEGRAM_TO_REPLACE = "<REPLACE_TOKEN>";
    private static final String TELEGRAM_FIELD_CHAT_ID = "chat_id";
    private static final String TELEGRAM_FIELD_TEXT = "text";
    private static final String TELEGRAM_FIELD_PARSE_MODE = "parse_mode";
    private static final String TELEGRAM_VALUE_PARSE_MODE = "Markdown";

    private static final String TELEGRAM_FIELD_REPLY_TO_MSG_ID = "reply_to_message_id";
    private static final String TELEGRAM_FIELD_DISABLE_NOTIFICATION = "disable_notification";
    private static final String TELEGRAM_VALUE_DISABLE_NOTIFICATION = TRUE.toString();

    @Value("${telegram.bot.sendmessage:#{null}}")
    private String telegramBotSendMessageUrl;

    @Value("${telegram.bot.token:#{null}}")
    private String telegramBotToken;

    @Value("#{new Long('${telegram.chat.id:0}')}")
    private Long telegramChatId;

    public void sendMessage(final String textMessage) {
        logger.info("Enviando mensagem para o chatID: {}", telegramChatId);
        post(telegramBotSendMessageUrl.replace(TELEGRAM_TO_REPLACE, telegramBotToken))
                .field(TELEGRAM_FIELD_CHAT_ID, telegramChatId)
                .field(TELEGRAM_FIELD_PARSE_MODE, TELEGRAM_VALUE_PARSE_MODE)
                .field(TELEGRAM_FIELD_TEXT, textMessage)
                .asJson();
    }

    public void replyMessage(final String textMessage, final int replyMsgId) {
        logger.info("Reply para a mensagem {} no chatID: {}", replyMsgId, telegramChatId);
        post(telegramBotSendMessageUrl.replace(TELEGRAM_TO_REPLACE, telegramBotToken))
                .field(TELEGRAM_FIELD_CHAT_ID, telegramChatId)
                .field(TELEGRAM_FIELD_PARSE_MODE, TELEGRAM_VALUE_PARSE_MODE)
                .field(TELEGRAM_FIELD_DISABLE_NOTIFICATION, TELEGRAM_VALUE_DISABLE_NOTIFICATION)
                .field(TELEGRAM_FIELD_TEXT, textMessage)
                .field(TELEGRAM_FIELD_REPLY_TO_MSG_ID, valueOf(replyMsgId))
                .asJson();
    }
}