package com.github.smartbooks.abtest.core;

import com.github.smartbooks.abtest.core.algorithm.DefaultHashAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 实验矩阵
 */
public class ExperimentMatrix {

    private static final Logger logger = LogManager.getLogger(ExperimentMatrix.class);

    /**
     * 实验主题
     */
    private Map<String, ExperimentSubject> experimentSubjectMap = new HashMap<>();

    /**
     * 分流算法
     */
    private ExperimentAlgorithm experimentAlgorithm = new DefaultHashAlgorithm();

    public static final String paramPrefix = "smartbooks.abtest";

    /**
     * 实验分配
     *
     * @param url 主题标识
     * @param id  用户标识
     * @return 实验参数
     */
    public Map<String, String> exec(String url, long id) {

        Map<String, String> resultMap = new HashMap<>();

        ExperimentSubject subject = experimentSubjectMap.getOrDefault(url, null);

        if (null != subject && null != experimentAlgorithm) {

            try {

                List<Experimentlayer> experimenterList = subject.getExperimentlayerList();

                for (Experimentlayer experimentlayer : experimenterList) {

                    long bucket = experimentAlgorithm.allocation(
                            id,
                            experimentlayer.getBucketSize(),
                            experimentlayer.getName());

                    List<ExperimentBucket> experimentBucketList = experimentlayer.getExperimentBucketList();

                    for (ExperimentBucket experimentBucket : experimentBucketList) {

                        if (experimentBucket.getBucketSet().contains(bucket)) {

                            String keyPrefix = String.format("%s.%s.%s.%s",
                                    paramPrefix,
                                    subject.getName(),
                                    experimentlayer.getName(),
                                    experimentBucket.getName());

                            Iterator<Map.Entry<String, String>> it = experimentBucket.getParamMap().entrySet().iterator();

                            while (it.hasNext()) {
                                Map.Entry<String, String> entry = it.next();
                                String key = String.format("%s.%s", keyPrefix, entry.getKey());
                                String value = entry.getValue();
                                resultMap.put(key, value);
                            }

                        }

                    }

                }

            } catch (Exception e) {
                logger.error(e);
            }
        }

        return resultMap;
    }

}
