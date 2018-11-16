package com.github.smartbooks.abtest.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class ResponseMessageWrap {

    private final static Logger logger = LogManager.getLogger(ResponseMessageWrap.class);

    public static final String CONTENT_TYPE = "application/json";

    public final static String CHARSET = "UTF-8";
    
    public static void flush(ResponseMessage message, HttpServletResponse resp) {
        flush(message, false, resp);
    }

    public static void flush(ResponseMessage message, Boolean pretty, HttpServletResponse resp) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            String jsonContent;

            if (pretty) {
                jsonContent = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
            } else {
                jsonContent = mapper.writeValueAsString(message);
            }

            resp.setContentType(CONTENT_TYPE);

            ServletOutputStream out = resp.getOutputStream();

            out.write(jsonContent.getBytes(CHARSET));

            out.flush();

            out.close();

        } catch (Exception e) {
            logger.error(e);
        }

    }

}
