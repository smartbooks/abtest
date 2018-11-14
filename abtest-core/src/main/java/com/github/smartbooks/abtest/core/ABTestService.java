package com.github.smartbooks.abtest.core;

import java.util.Map;

public abstract class ABTestService {

    private String name = "";
    private String url = "";

    public void exec(Map<String, String> abTestParam, FlowMessage flowMessage) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
