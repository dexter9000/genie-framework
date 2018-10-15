package com.genie.flow;

public interface Command<T> {

    T execute(CommandContext commandContext);

}
