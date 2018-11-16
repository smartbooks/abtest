package com.github.smartbooks.abtest.core.message;

import com.github.smartbooks.abtest.core.ExperimentSubject;
import com.github.smartbooks.abtest.core.ResponseMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * 失败的服务调用器
 */
public class FailABTestServiceActuator extends ResponseMessage {

    public FailABTestServiceActuator(Map<String, String> abTestParam, ExperimentSubject subject, Exception e) {

        super.setStatus(-1);
        super.setCode("103");

        Map<String, Object> entity = new HashMap<>();
        entity.put("param", abTestParam);
        entity.put("subject", subject);

        super.setData(entity);

        super.setMsg(e.getMessage());

    }
}
