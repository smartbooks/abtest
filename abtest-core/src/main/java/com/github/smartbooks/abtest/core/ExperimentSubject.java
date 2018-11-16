package com.github.smartbooks.abtest.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.smartbooks.abtest.core.actuator.EchoABTestServiceActuator;

import java.util.ArrayList;
import java.util.List;

/**
 * 实验主题
 */
public class ExperimentSubject {

    private String source = "";
    private String target = "";
    private String name = "";
    private String summary = "";
    private List<Experimentlayer> experimentlayerList = new ArrayList<>();

    @JsonIgnore
    private ABTestServiceActuator abTestServiceActuator = new EchoABTestServiceActuator();

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Experimentlayer> getExperimentlayerList() {
        return experimentlayerList;
    }

    public void setExperimentlayerList(List<Experimentlayer> experimentlayerList) {
        this.experimentlayerList = experimentlayerList;
    }

    public ABTestServiceActuator getAbTestServiceActuator() {
        return abTestServiceActuator;
    }

    public void setAbTestServiceActuator(ABTestServiceActuator abTestServiceActuator) {
        this.abTestServiceActuator = abTestServiceActuator;
    }
}
