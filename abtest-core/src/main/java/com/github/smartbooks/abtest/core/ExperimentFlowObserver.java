package com.github.smartbooks.abtest.core;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;

/**
 * 实验流量观察着
 */
public class ExperimentFlowObserver extends FlowObserver {

    private final Logger logger = LogManager.getLogger(ExperimentFlowObserver.class);

    private ExperimentMatrix experimentMatrix = new ExperimentMatrix();

    public void put(ExperimentSubject subject) {
        experimentMatrix.getExperimentSubjectMap().put(subject.getSource(), subject);
    }

    @Override
    public void update(FlowMessage message) {

        try {

            Pair<String, Long> pm = resolveExperimentParam(message);

            if (null != pm) {

                experimentMatrix.exec(pm.getKey(), pm.getValue(), message);

            }

        } catch (Exception e) {
            logger.error(e);
        }

    }

    private Pair<String, Long> resolveExperimentParam(FlowMessage msg) {

        try {

            Map<String, String[]> map = msg.getReq().getParameterMap();
            Set<String> keySet = map.keySet();

            String url = "";
            if (keySet.contains("url")) {
                url = map.get("url")[0];
            }

            long id = 0L;
            if (keySet.contains("id")) {
                id = Long.valueOf(map.get("id")[0]);
            }

            return new Pair<>(url, id);

        } catch (Exception e) {
            logger.error(e);
        }

        return null;
    }

}
