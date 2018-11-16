package com.github.smartbooks.abtest.core;

import java.util.Map;

public abstract class ABTestServiceActuator {

    public void exec(Map<String, String> abTestParam, FlowMessage flowMessage, ExperimentSubject subject) {
    }

}
