package com.github.smartbooks.abtest.core.actuator;

import com.github.smartbooks.abtest.core.ABTestServiceActuator;
import com.github.smartbooks.abtest.core.ExperimentSubject;
import com.github.smartbooks.abtest.core.FlowMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class EchoABTestServiceActuator extends ABTestServiceActuator {

    private final Logger logger = LogManager.getLogger(EchoABTestServiceActuator.class);

    @Override
    public void exec(Map<String, String> abTestParam, FlowMessage flowMessage, ExperimentSubject subject) {

        StringBuilder repContent = new StringBuilder();

        Iterator<Map.Entry<String, String>> it = abTestParam.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry<String, String> entry = it.next();

            repContent.append(String.format("%s:%s\n", entry.getKey(), entry.getValue()));

        }

        try {

            ServletOutputStream out = flowMessage.getResp().getOutputStream();
            out.write(repContent.toString().getBytes("utf-8"));
            out.flush();
            out.close();

        } catch (IOException e) {
            logger.error(e);
        }

    }
}
