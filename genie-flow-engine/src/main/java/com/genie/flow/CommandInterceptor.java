package com.genie.flow;

public interface CommandInterceptor {

    CommandInterceptor getNext();

    void setNext(CommandInterceptor next);

}
