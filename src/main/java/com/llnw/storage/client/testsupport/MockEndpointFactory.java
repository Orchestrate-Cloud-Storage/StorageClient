package com.llnw.storage.client.testsupport;

import com.llnw.storage.client.Endpoint;
import com.llnw.storage.client.EndpointFactory;
import com.llnw.storage.client.io.ActivityCallback;

import javax.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class MockEndpointFactory extends EndpointFactory {

    public Endpoint ftpEndpoint = null;
    public Endpoint httpEndpoint = null;

    public MockEndpointFactory() {
        this(null, null);
    }

    public MockEndpointFactory(Endpoint ftpEndpoint, Endpoint httpEndpoint) {
        super(null,null,null);
        this.ftpEndpoint = ftpEndpoint;
        this.httpEndpoint = httpEndpoint;
    }

    @Override
    public Endpoint create(boolean useFTP) {
        final Endpoint targetEndpoint = (useFTP) ? this.ftpEndpoint : this.httpEndpoint;
        if (targetEndpoint != null) {
            return targetEndpoint;
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

                @Override
                public void upload(ByteBuffer byteBuffer, String path, String name, ActivityCallback callback)
                        throws IOException {

                }

            };
        }
    }

    public void setFtpEndpoint(Endpoint endpoint) {
        this.ftpEndpoint = endpoint;
    }

    public void setHttpEndpoint(Endpoint endpoint) {
        this.httpEndpoint = endpoint;
    }
}
