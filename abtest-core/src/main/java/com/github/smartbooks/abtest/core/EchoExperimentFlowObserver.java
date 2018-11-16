package com.github.smartbooks.abtest.core;

public class EchoExperimentFlowObserver extends ExperimentFlowObserver {

    public EchoExperimentFlowObserver() {

        ExperimentSubject subject = new ExperimentSubject();
        subject.setName("subject_login");
        subject.setSource("com.gituhub.smartbooks.login");
        subject.setTarget("http://www.baidu.com");

        //layer
        Experimentlayer layer = new Experimentlayer();
        layer.setBucketSize(5L);
        layer.setName("layer_ui");

        {
            ExperimentBucket bucket = new ExperimentBucket();
            bucket.setName("bucket_a");
            bucket.getBucketSet().add(0L);
            bucket.getBucketSet().add(1L);
            bucket.getBucketSet().add(2L);
            bucket.getParamMap().put("background", "red");
            bucket.getParamMap().put("skin", "blue");
            layer.getExperimentBucketList().add(bucket);
        }

        {
            ExperimentBucket bucket = new ExperimentBucket();
            bucket.setName("bucket_b");
            bucket.getBucketSet().add(3L);
            bucket.getBucketSet().add(4L);
            bucket.getParamMap().put("background", "white");
            bucket.getParamMap().put("skin", "black");
            bucket.getParamMap().put("_target", "http://qq.com");
            layer.getExperimentBucketList().add(bucket);
        }

        subject.getExperimentlayerList().add(layer);

        super.put(subject);
    }

}
