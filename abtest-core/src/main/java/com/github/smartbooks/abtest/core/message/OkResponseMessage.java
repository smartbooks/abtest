package com.github.smartbooks.abtest.core.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.smartbooks.abtest.core.ResponseMessage;

import java.util.Map;

/**
 * 默认成功消息
 */
public class OkResponseMessage extends ResponseMessage {

    public OkResponseMessage(JsonNode jsonNode) {
        setData(jsonNode);
    }

    public OkResponseMessage(Map<String, String> param) {
        setData(param);
    }

    public OkResponseMessage(String content) {
        setData(content);
    }
}
