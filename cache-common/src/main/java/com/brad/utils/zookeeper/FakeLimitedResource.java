package com.brad.utils.zookeeper;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simulates some external resource that can only be access by one process at a time
 *
 * @author zhangyu
 */
public class FakeLimitedResource {
    private final AtomicBoolean inUse = new AtomicBoolean(false);

    public void use() throws InterruptedException {
        // in a real application this would be accessing/manipulating a shared resource

        if (!inUse.compareAndSet(false, true)) {
            throw new IllegalStateException("Needs to be used by one client at a time");
        }

        try {
            Thread.sleep((long) (3 * new Random().nextInt()));
        } finally {
            inUse.set(false);
        }
    }
}
