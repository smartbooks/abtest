package com.github.smartbooks.abtest.core.message;

import com.github.smartbooks.abtest.core.ResponseMessage;
import javafx.util.Pair;

/**
 * 参数验证失败消息
 */
public class ParamVerifyFailResponseMessage extends ResponseMessage {

    public ParamVerifyFailResponseMessage(Pair<String, String>... params) {
        setData(params);
        super.setStatus(-1);
        super.setCode("104");
        super.setMsg("参数校验失败");
    }
}
