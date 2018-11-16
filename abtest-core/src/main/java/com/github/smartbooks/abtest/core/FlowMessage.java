package com.github.smartbooks.abtest.core;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 流量消息
 */
public class FlowMessage {

    private final Logger logger = LogManager.getLogger(FlowMessage.class);
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private Map<String, String> reqMap = new HashMap<>();

    public FlowMessage() {
    }

    public FlowMessage(HttpServletRequest req, HttpServletResponse resp) {
        this.req = req;
        this.resp = resp;
    }

    public HttpServletRequest getReq() {
        return req;
    }

    public void setReq(HttpServletRequest req) {
        this.req = req;
    }

    public HttpServletResponse getResp() {
        return resp;
    }

    public void setResp(HttpServletResponse resp) {
        this.resp = resp;
    }

    public Map<String, String> getReqMap() {
        return reqMap;
    }

    public void setReqMap(Map<String, String> reqMap) {
        this.reqMap = reqMap;
    }

    public void resolveParam() {

        try {

            Map<String, String[]> map = req.getParameterMap();

            Iterator<String> it = map.keySet().iterator();

            while (it.hasNext()) {

                try {

                    String key = it.next();

                    String value = map.get(key)[0];

                    reqMap.put(key, value);

                } catch (Exception e) {
                    logger.error(e);
                }

            }

            Enumeration<String> enumeration = req.getParameterNames();

            while (enumeration.hasMoreElements()) {

                try {

                    String key = enumeration.nextElement();

                    String value = req.getParameter(key);

                    reqMap.put(key, value);

                } catch (Exception e) {
                    logger.error(e);
                }
            }

        } catch (Exception e) {
            logger.error(e);
        }

    }

    public Pair<String, Long> getCommand() {

        String key = reqMap.getOrDefault("_cmd", null);

        Long uid = 0L;
        try {
            uid = Long.valueOf(reqMap.getOrDefault("_uid", "0"));
        } catch (Exception e) {
        }

        return new Pair<>(key, uid);
    }
}
