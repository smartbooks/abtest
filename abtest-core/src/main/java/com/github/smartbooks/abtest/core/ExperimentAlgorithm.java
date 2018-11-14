package com.github.smartbooks.abtest.core;

/**
 * 流量分桶算法
 */
public abstract class ExperimentAlgorithm {

    public abstract long allocation(long id, long bucketSize, String factor);

}
