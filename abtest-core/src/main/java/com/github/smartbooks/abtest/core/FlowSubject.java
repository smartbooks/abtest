package com.github.smartbooks.abtest.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 流量主题抽象基类
 */
public class FlowSubject {

    private List<FlowObserver> flowObserverList = new ArrayList<>();

    public void attach(FlowObserver observer) {
        flowObserverList.add(observer);
    }

    public void detach(FlowObserver observer) {
        flowObserverList.remove(observer);
    }

    public void detachAll() {
        flowObserverList.clear();
    }

    public void notify(FlowMessage message) {
        for (FlowObserver observer : flowObserverList) {
            observer.update(message);
        }
    }

}
