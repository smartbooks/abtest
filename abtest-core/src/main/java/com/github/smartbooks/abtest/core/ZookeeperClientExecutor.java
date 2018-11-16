package com.github.smartbooks.abtest.core;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class ZookeeperClientExecutor implements Watcher {

    @Override
    public void process(WatchedEvent watchedEvent) {

        System.out.println(watchedEvent);

    }
}
