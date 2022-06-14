package com.webflux.rsocketserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class RSocketController {

    @MessageMapping("request-response")
    public Message requestResponse(Message request){
        log.info("Received request-response result = {}", request);
        return new Message("youu", "server");
    }
}
