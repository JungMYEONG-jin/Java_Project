package com.webflux.rsocketclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Slf4j
@ShellComponent
public class RSocketShellClient {

    private final RSocketRequester rSocketRequester;

    public RSocketShellClient(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }


    @ShellMethod("Send one request. One response will be printed.")
    public void requestResponse() throws InterruptedException{
        log.info("Sending one request. Wait for a moment...");
        this.rSocketRequester.route("request-response")
                .data(new Message("youu", "client"))
                .retrieveMono(Message.class).log().block();
    }
}
