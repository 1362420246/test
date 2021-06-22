package com.qbk.postprocessor.handler;

import lombok.Data;

@Data
public class Handler {

    private ParentHandler parentHandler;

    public String getName() {
        return parentHandler.getName();
    }
}
