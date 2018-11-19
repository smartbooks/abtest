package com.github.smartbooks.abtest.core;

import com.github.smartbooks.abtest.core.message.ErrorResponseMessage;
import com.github.smartbooks.abtest.core.message.ParamVerifyFailResponseMessage;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 实验流量观察着
 */
public class ExperimentFlowObserver extends FlowObserver {

    private final Logger logger = LogManager.getLogger(ExperimentFlowObserver.class);

    private ExperimentMatrix experimentMatrix = new ExperimentMatrix();

    private ZookeeperClientWatcher zookeeperClientWatcher = new ZookeeperClientWatcher();

    public ExperimentFlowObserver() {
        zookeeperClientWatcher.setExperimentMatrix(experimentMatrix);
        zookeeperClientWatcher.start();
    }

    public ExperimentMatrix getExperimentMatrix() {
        return experimentMatrix;
    }

    public void setExperimentMatrix(ExperimentMatrix experimentMatrix) {
        this.experimentMatrix = experimentMatrix;
    }

    public void put(ExperimentSubject subject) {
        experimentMatrix.getExperimentSubjectMap().put(subject.getSource(), subject);
    }

    @Override
    public void update(FlowMessage message) {
        try {

            message.resolveParam();

            Pair<String, Long> pm = message.getCommand();

            if (validateExperimentParam(pm)) {

                experimentMatrix.exec(pm.getKey(), pm.getValue(), message);

            } else {

                ResponseMessageWrap.flush(new ParamVerifyFailResponseMessage(
                        new Pair<>("_cmd", "Require StringType"),
                        new Pair<>("_uid", "Require LoneType")
                ), message.getResp());

            }

        } catch (Exception e) {
            logger.error(e);

            //未知的错误
            ResponseMessageWrap.flush(new ErrorResponseMessage(e), message.getResp());
        }
    }

    private boolean validateExperimentParam(Pair<String, Long> cmd) {

        String key = cmd.getKey();
        if (null == key || key.isEmpty()) {
            return false;
        }

        Long uid = cmd.getValue();
        if (uid == 0) {
            return false;
        }

        return true;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        zookeeperClientWatcher.stop();
    }
}
