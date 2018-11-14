package com.github.smartbooks.abtest.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 实验主题
 */
public class ExperimentSubject {

    private String url = "";
    private String name = "";
    private String summary = "";
    private List<Experimentlayer> experimentlayerList = new ArrayList<>();
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
