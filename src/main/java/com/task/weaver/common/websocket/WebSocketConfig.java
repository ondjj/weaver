package com.task.weaver.common.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chattingWs")
                .setAllowedOriginPatterns("*")
                .withSockJS();  //낮은 버전 브라우저도 사용할 수 있도록.
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");  // (1) 메시지 구독 요청: 메시지 송신
        registry.setApplicationDestinationPrefixes("/pub"); // (2) 메시지 발행 요청: 메시지 수신
    }
}
