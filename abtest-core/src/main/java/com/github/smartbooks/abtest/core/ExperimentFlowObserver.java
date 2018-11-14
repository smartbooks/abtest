package com.github.smartbooks.abtest.core;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 实验流量观察着
 */
public class ExperimentFlowObserver extends FlowObserver {

    private final Logger logger = LogManager.getLogger(ExperimentFlowObserver.class);

    private ExperimentMatrix experimentMatrix = new ExperimentMatrix();

    private Map<String, ABTestService> abTestServiceMap = new HashMap<>();

    public void put(ExperimentSubject experimentSubject, ABTestService abTestService) {
        String url = abTestService.getUrl();
        experimentMatrix.getExperimentSubjectMap().put(url, experimentSubject);
        abTestServiceMap.put(url, abTestService);
    }

    @Override
    public void update(FlowMessage message) {

        try {

            Pair<String, Long> pm = resolveExperimentParam(message);

            if (null != pm) {

                Map<String, String> readyParamMap = experimentMatrix.exec(pm.getKey(), pm.getValue());

                ABTestService abTestService = abTestServiceMap.getOrDefault(pm.getKey(), null);

                if (null != abTestService) {

                    abTestService.exec(readyParamMap, message);

                }
            }

        } catch (Exception e) {
            logger.error(e);
        }

    }

    private Pair<String, Long> resolveExperimentParam(FlowMessage msg) {

        try {

            String url = msg.getReq().getParameter("url");

            long id = Long.valueOf(msg.getReq().getParameter("id"));

            return new Pair<>(url, id);

        } catch (Exception e) {
            logger.error(e);
        }

        return null;
    }

}
