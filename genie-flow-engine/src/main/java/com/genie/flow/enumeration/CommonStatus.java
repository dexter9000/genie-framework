package com.genie.flow.enumeration;

import com.genie.core.utils.ValueEnum;

public enum CommonStatus implements ValueEnum<Integer> {
    /**
     * 不可用
     */
    DISABLE(0),
    /**
     * 可用
     */
    ENABLE(1),
    ;

    Integer value;

    CommonStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
