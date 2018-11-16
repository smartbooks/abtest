package com.github.smartbooks.abtest.core.actuator;

import com.github.smartbooks.abtest.core.ABTestServiceActuator;
import com.github.smartbooks.abtest.core.ExperimentSubject;
import com.github.smartbooks.abtest.core.FlowMessage;
import com.github.smartbooks.abtest.core.ResponseMessageWrap;
import com.github.smartbooks.abtest.core.message.FailABTestServiceActuator;
import com.github.smartbooks.abtest.core.message.OkResponseMessage;
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

            //服务调用成功
            ResponseMessageWrap.flush(new OkResponseMessage(flowMessage.getReqMap()), flowMessage.getResp());

        } catch (Exception e) {
            logger.error(e);

            //服务调用失败
            ResponseMessageWrap.flush(new FailABTestServiceActuator(abTestParam, subject, e), flowMessage.getResp());
        }

    }
}
