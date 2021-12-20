package com.tongcha.simulation.VO;

import org.springframework.stereotype.Component;

@Component
public class Content {
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;
}
