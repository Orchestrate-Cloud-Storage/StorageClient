package com.llnw.storage.client;

import java.io.IOException;

public class EndpointException extends IOException {
    private static final long serialVersionUID = -421726138420470065L;

    public EndpointException(String s) {
        super(s);
    }

    public EndpointException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
