package com.genie.flow;

public interface Execution {

    /**
     * The unique identifier of the execution.
     */
    String getId();

    /**
     * Returns the name of this execution.
     */
    String getName();

    /**
     * Indicates if the execution is ended.
     */
    boolean isEnded();

    /**
     * The tenant identifier of this process instance
     */
    String getTenantId();
}
