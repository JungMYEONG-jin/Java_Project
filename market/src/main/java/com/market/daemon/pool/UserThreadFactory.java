package com.market.daemon.pool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class UserThreadFactory implements ThreadFactory
{
    private final String prefix;
    private final AtomicInteger num = new AtomicInteger(1);


    public UserThreadFactory(String whatNamePrefix) {
        prefix = "From UserThreadFactory" + whatNamePrefix + "-Worker-";
    }

    @Override
    public Thread newThread(Runnable r) {
        String threadName = prefix + num.getAndIncrement();
        Thread thread = new Thread(r, threadName);
        System.out.println(thread.getName());
        return thread;
    }
}
