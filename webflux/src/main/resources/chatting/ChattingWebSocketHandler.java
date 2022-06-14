package com.webflux.chatting;

import com.webflux.chatting.service.ChattingService;
import com.webflux.chatting.service.PubSubService;
import com.webflux.chatting.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ChattingWebSocketHandler implements WebSocketHandler {

    private static final Duration PING_INTERVAL = Duration.ofSeconds(20);
    private static final byte[] PING_PAYLOAD = new byte[0];

    private final ChattingProperties properties;
    private final TokenService tokenService;
    private final PubSubService pubSubService;
    private final ChattingService chattingService;


    @Override
    public Mono<Void> handle(WebSocketSession session) {

//        List<String> tokenHeaders = session.getHandshakeInfo().getHeaders().getOrEmpty(properties.getWsTokenHeader());
//
//        Mono<Long> channelId = Flux.fromIterable(tokenHeaders).flatMap(tokenService.parseToken).next();

        Flux<WebSocketMessage> pingSource = Flux.interval(PING_INTERVAL).map(i -> session.pingMessage(factory -> factory.wrap(PING_PAYLOAD)));

//        return channelId.flatMap(cid -> {
//            Mono<Void> input = session.receive().filter(message -> message.getType() == WebSocketMessage.Type.TEXT)
//                    .flatMap(chattingService::processMessage)
//                    .concatMap(message -> pubSubService.sendMessage(cid, message))
//                    .then();
//
//            Flux<WebSocketMessage> messageSource = pubSubService.receiveMessages(cid).map(session::textMessage);
//
//            Mono<Void> output = session.send(Flux.merge(messageSource, pingSource));
//
//            return Mono.zip(input, output).then();
//        });
        return null;

    }
}
