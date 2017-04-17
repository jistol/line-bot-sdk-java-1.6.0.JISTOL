/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.spring.boot.custom.LineMessagingClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
    @Autowired
    private LineMessagingClientFactory lineMessagingClientFactory;

    @Autowired
    private LineMessagingClient lineMessagingClient;

    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    @EventMapping(priority = 1, secretKey = "72b228bcc5b7a54363a2d80ca428538f")
    public void handleTextMessageEvent1(MessageEvent<TextMessageContent> event, String secretKey) {
        System.out.println("event: " + event);
        System.out.println("secretKey: " + secretKey);
        lineMessagingClientFactory.get(secretKey).pushMessage(new PushMessage(event.getSource().getUserId(), new TextMessage("test")));
    }

    @EventMapping(priority = 1, secretKey = "ac85c631a5728860764a6d7aa8296b2c")
    public void handleTextMessageEvent2(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        System.out.println("secretKey: " + this.getClass().getEnclosingMethod().getAnnotation(EventMapping.class).secretKey());
        lineMessagingClient.pushMessage(new PushMessage(event.getSource().getUserId(), new TextMessage("test")));
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event, String secretKey) {
        System.out.println("event: " + event);
        System.out.println("secretKey: " + secretKey);
    }
}
