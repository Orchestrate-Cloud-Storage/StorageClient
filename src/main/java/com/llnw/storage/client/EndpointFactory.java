package com.llnw.storage.client;

import com.google.common.base.Throwables;

import java.net.MalformedURLException;
import java.net.URL;

public class EndpointFactory {
    public Endpoint create(String host, String username, String password, boolean useFTP) {
        if (!useFTP) {
            try {
                return new EndpointHTTP(new URL("http://" + host + ":8080"), username, password);
            } catch (MalformedURLException e) {
                throw Throwables.propagate(e);
            }
        } else {
            return new EndpointFTP(host, username, password);
        }
    }
}
