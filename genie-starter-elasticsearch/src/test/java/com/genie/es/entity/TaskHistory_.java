package com.genie.es.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TaskHistory.class)
public class TaskHistory_ {

    public static volatile SingularAttribute<TaskHistory, String> factId;
    public static volatile SingularAttribute<TaskHistory, Integer> taskId;
    public static volatile SingularAttribute<TaskHistory, String> campaignId;
    public static volatile SingularAttribute<TaskHistory, String> metaId;
    public static volatile SingularAttribute<TaskHistory, String> result;
    public static volatile SingularAttribute<TaskHistory, String> status;
    public static volatile SingularAttribute<TaskHistory, String> startTime;
    public  static volatile SingularAttribute<TaskHistory, String> endTime;

}
