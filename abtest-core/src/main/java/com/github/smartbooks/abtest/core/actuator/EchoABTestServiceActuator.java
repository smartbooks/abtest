package com.github.smartbooks.abtest.core.actuator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.smartbooks.abtest.core.ABTestServiceActuator;
import com.github.smartbooks.abtest.core.ExperimentSubject;
import com.github.smartbooks.abtest.core.FlowMessage;
import com.github.smartbooks.abtest.core.ResponseMessageWrap;
import com.github.smartbooks.abtest.core.message.FailABTestServiceActuator;
import com.github.smartbooks.abtest.core.message.OkResponseMessage;
import com.github.smartbooks.abtest.core.utils.HttpUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class EchoABTestServiceActuator extends ABTestServiceActuator {

    private final Logger logger = LogManager.getLogger(EchoABTestServiceActuator.class);

    @Override
    public void exec(Map<String, String> abTestParam, FlowMessage flowMessage, ExperimentSubject subject) {

        try {

            //参数合并
            flowMessage.getReqMap().putAll(abTestParam);

            ObjectMapper mapper = new ObjectMapper();

            String url = flowMessage.getReqMap().getOrDefault("_target", "");

            String postJson = mapper.writeValueAsString(flowMessage.getReqMap());

            String jsonResult = HttpUtils.sendPost(url, postJson);

            if (null != jsonResult && jsonResult.isEmpty() == false) {

                //服务调用成功
                ResponseMessageWrap.flush(new OkResponseMessage(mapper.readTree(jsonResult)), flowMessage.getResp());

            } else {

                //服务调用失败
                throw new NullPointerException(String.format("url:%s post:%s result:%s", url, postJson, jsonResult));
            }

        } catch (Exception e) {
            logger.error(e);

            //服务调用失败
            ResponseMessageWrap.flush(new FailABTestServiceActuator(abTestParam, subject, e), flowMessage.getResp());
        }

    }
}
