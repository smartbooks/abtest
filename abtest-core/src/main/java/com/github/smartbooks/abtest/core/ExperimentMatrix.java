package com.github.smartbooks.abtest.core;

import com.github.smartbooks.abtest.core.algorithm.DefaultHashAlgorithm;
import com.github.smartbooks.abtest.core.message.FailExperimentSubjectResponseMessage;
import com.github.smartbooks.abtest.core.message.NoFoundExperimentSubjectResponseMessage;
import com.github.smartbooks.abtest.core.utils.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * 实验矩阵
 */
public class ExperimentMatrix {

    private final Logger logger = LogManager.getLogger(ExperimentMatrix.class);

    /**
     * 实验主题
     */
    private Map<String, ExperimentSubject> experimentSubjectMap = new HashMap<>();

    /**
     * 分流算法
     */
    private ExperimentAlgorithm experimentAlgorithm = new DefaultHashAlgorithm();


    public void exec(String source, long id, FlowMessage message) {

        Map<String, String> resultMap = new HashMap<>();

        ExperimentSubject subject = experimentSubjectMap.getOrDefault(source, null);

        if (null != subject && null != experimentAlgorithm) {

            try {

                List<String> abTestPathList = new ArrayList<>();

                List<Experimentlayer> experimenterList = subject.getExperimentlayerList();

                for (Experimentlayer experimentlayer : experimenterList) {

                    long bucket = experimentAlgorithm.allocation(
                            id,
                            experimentlayer.getBucketSize(),
                            experimentlayer.getName());

                    List<ExperimentBucket> experimentBucketList = experimentlayer.getExperimentBucketList();

                    for (ExperimentBucket experimentBucket : experimentBucketList) {

                        if (experimentBucket.getBucketSet().contains(bucket)) {

                            String abTestPath = String.format("%s/%s/%s:%s",
                                    subject.getName(),
                                    experimentlayer.getName(),
                                    experimentBucket.getName(),
                                    bucket);

                            abTestPathList.add(abTestPath);

                            Iterator<Map.Entry<String, String>> it = experimentBucket.getParamMap().entrySet().iterator();

                            while (it.hasNext()) {
                                Map.Entry<String, String> entry = it.next();
                                resultMap.put(entry.getKey(), entry.getValue());
                            }

                        }

                    }

                }

                if (resultMap.containsKey("_target") == false) {
                    resultMap.put("_target", subject.getTarget());
                }

                resultMap.put("_path", ListUtils.mkString(abTestPathList, ","));

                //服务执行器调用接口
                subject.getAbTestServiceActuator().exec(resultMap, message, subject);

            } catch (Exception e) {
                logger.error(e);

                //实验发生未知错误
                ResponseMessageWrap.flush(new FailExperimentSubjectResponseMessage(subject, e), message.getResp());
            }

        } else {

            //未找到实验主题
            ResponseMessageWrap.flush(new NoFoundExperimentSubjectResponseMessage(source), message.getResp());

        }

    }


    public Map<String, ExperimentSubject> getExperimentSubjectMap() {
        return experimentSubjectMap;
    }

    public void setExperimentSubjectMap(Map<String, ExperimentSubject> experimentSubjectMap) {
        this.experimentSubjectMap = experimentSubjectMap;
    }

    public ExperimentAlgorithm getExperimentAlgorithm() {
        return experimentAlgorithm;
    }

    public void setExperimentAlgorithm(ExperimentAlgorithm experimentAlgorithm) {
        this.experimentAlgorithm = experimentAlgorithm;
    }
}
