package com.llnw.storage.client;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.llnw.storage.client.io.ActivityCallback;

import javax.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MockEndpointFactory extends EndpointFactory {

    private final File destination;

    public MockEndpointFactory() {
        this(null);
    }

    public MockEndpointFactory(File destination) {
        super(null, null, null);
        this.destination = destination;
    }


    @Override
    public Endpoint create(boolean useFTP) {
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
                return Lists.newArrayList();
            }

            @Override
            public void upload(File file, String path, String name, @Nullable ActivityCallback callback)
                    throws IOException {
                if (destination != null) {
                    Files.copy(file, destination);
                }
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
