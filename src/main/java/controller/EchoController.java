package controller;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

public class EchoController {
    public static void main(String[] args) {
        SpringApplication.run(EchoController.class, args);
    }

    @RestController
    public static class MyController {
        @Autowired
        private LineMessagingService lineMessagingService;

        @PostMapping("/callback")
        public void callback(@LineBotMessages List<Event> events) throws IOException {
            for (Event event : events) {
                System.out.println("event: " + event);
                if (event instanceof MessageEvent) {
                    MessageContent message = ((MessageEvent) event).getMessage();
                    if (message instanceof TextMessageContent) {
                        System.out.println("Sending reply message");
                        TextMessageContent textMessageContent = (TextMessageContent) message;
                        BotApiResponse apiResponse = lineMessagingService.replyMessage(
                                new ReplyMessage(
                                        ((MessageEvent) event).getReplyToken(),
                                        new TextMessage(textMessageContent.getText()
                                        ))).execute().body();
                        System.out.println("Sent messages: " + apiResponse);
                    }
                }
            }
        }
    }
}
