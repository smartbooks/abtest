package com.github.smartbooks.abtest.core.message;

import com.github.smartbooks.abtest.core.ResponseMessage;

/**
 * 未找到实验主题
 */
public class NoFoundExperimentSubjectResponseMessage extends ResponseMessage {

    public NoFoundExperimentSubjectResponseMessage(String source) {
        super.setStatus(-1);
        super.setCode("101");
        super.setMsg(String.format("Not Found Experiment Subject %s", source));
    }
}
