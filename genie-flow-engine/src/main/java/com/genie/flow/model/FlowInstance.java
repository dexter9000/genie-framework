package com.genie.flow.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程实例
 */
public class FlowInstance {

    private String id;

    private String modelId;

    private String status; // TODO 枚举

    private String name;

    private String profile; // TODO 运行模式

    private Date startTime;

    private boolean completeable;

    private Map<String, Object> variables = new HashMap<>();
}
