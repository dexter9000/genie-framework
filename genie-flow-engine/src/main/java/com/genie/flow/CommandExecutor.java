package com.genie.flow;

public interface CommandExecutor {

    <T> T execute(Command<T> command);

}
