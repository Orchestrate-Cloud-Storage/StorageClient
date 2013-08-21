package com.llnw.storage.client;

import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;

public class EndpointUtil {
    static IOException unwindInterruptException(IOException e) throws IOException {
        // This is dumb. Sometimes httpcomponents will throw a ClientProtocolException which wraps the real
        // exception we want to throw when interrupted, ClosedByInterruptException. So, try to find that
        // exception in the stack, and then throw that one instead
        Throwable t = e.getCause();

        int i = 10; // cause depth limit
        while (i-- > 0 && t != null && !(t instanceof ClosedByInterruptException)) {
            t = t.getCause();
        }

        if (t instanceof ClosedByInterruptException) {
            throw (IOException)t;
        } else {
            throw e;
        }
    }
}
