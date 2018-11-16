package com.github.smartbooks.abtest.core.message;

import com.github.smartbooks.abtest.core.ResponseMessage;

public class ErrorResponseMessage extends ResponseMessage {

    public ErrorResponseMessage(String error) {

        super.setStatus(-1);
        super.setCode("0");
        super.setMsg(error);

    }

    public ErrorResponseMessage(Exception e) {

        super.setStatus(-1);
        super.setCode("0");
        super.setMsg(e.getMessage());
        super.setData(e);

    }
}
