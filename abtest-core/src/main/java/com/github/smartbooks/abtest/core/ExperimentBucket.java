package com.github.smartbooks.abtest.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 实验桶
 */
public class ExperimentBucket {

    private String name = "";
    private String summary = "";
    private Set<Integer> bucketSet = new HashSet<>();
    private Map<String, String> paramMap = new HashMap<>();
    
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

    public Set<Integer> getBucketSet() {
        return bucketSet;
    }

    public void setBucketSet(Set<Integer> bucketSet) {
        this.bucketSet = bucketSet;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }
}
