package com.github.smartbooks.abtest.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 实验层
 */
public class Experimentlayer {

    private String name = "";
    private String summary = "";
    private Long bucketSize = 100L;
    private List<ExperimentBucket> experimentBucketList = new ArrayList<>();

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

    public Long getBucketSize() {
        return bucketSize;
    }

    public void setBucketSize(Long bucketSize) {
        this.bucketSize = bucketSize;
    }

    public List<ExperimentBucket> getExperimentBucketList() {
        return experimentBucketList;
    }

    public void setExperimentBucketList(List<ExperimentBucket> experimentBucketList) {
        this.experimentBucketList = experimentBucketList;
    }
}
