package com.llnw.storage.client.testsupport;

import com.llnw.storage.client.Endpoint;
import com.llnw.storage.client.EndpointFactory;
import com.llnw.storage.client.io.ActivityCallback;

import javax.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MockEndpointFactory extends EndpointFactory {

    public Endpoint alwaysReturn = null;

    public MockEndpointFactory() {
        this(null);
    }

    public MockEndpointFactory(Endpoint alwaysReturn) {
        super(null,null,null);
        this.alwaysReturn = null;
    }

    @Override
    public Endpoint create(boolean useFTP) {
        if (alwaysReturn != null) {
            return alwaysReturn;
        } else {

            return new Endpoint() {
                @Override
                public void deleteDirectory(String path) throws IOException {
                }

                @Override
                public void deleteFile(String path) throws IOException {
                }

                @Override
                public void close() {
                }

                @Override
                public void makeDirectory(String path) throws IOException {
                }

                @Override
                public List<String> listFiles(String path) throws IOException {
                    return null;
                }

                @Override
                public void upload(File file, String path, String name, @Nullable ActivityCallback callback)
                        throws IOException {
                }

                @Override
                public void noop() throws IOException {
                }

                @Override
                public boolean exists(String path) throws IOException {
                    return false;
                }
            };
        }
    }

    public void setEndpoint(Endpoint alwaysReturn) {
        this.alwaysReturn = alwaysReturn;
    }
}
