package com.github.smartbooks.abtest.core.message;

import com.github.smartbooks.abtest.core.ExperimentSubject;
import com.github.smartbooks.abtest.core.ResponseMessage;

/**
 * 流量实验失败
 */
public class FailExperimentSubjectResponseMessage extends ResponseMessage {

    public FailExperimentSubjectResponseMessage(ExperimentSubject subject, Exception e) {
        super.setStatus(-1);
        super.setCode("102");
        super.setData(subject);
        super.setMsg(e.getMessage());
    }
}
