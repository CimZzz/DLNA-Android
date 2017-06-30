package com.virtualightning.dlna.factory;

import java.util.concurrent.ExecutorService;

public interface ThreadPoolFactory {
    ExecutorService createThreadPool();
}
