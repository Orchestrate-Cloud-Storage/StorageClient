package com.llnw.storage.client;

import com.llnw.storage.client.io.ActivityCallback;
import org.joda.time.Duration;

import javax.annotation.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Endpoint extends Closeable {
    void deleteDirectory(String path) throws IOException;
    void deleteFile(String path) throws IOException;
    void makeDirectory(String path) throws IOException;
    List<String> listFiles(String path) throws IOException;
    void upload(File file, String path, String name, @Nullable ActivityCallback callback) throws IOException;
    void noop() throws IOException;
    boolean exists(String path) throws IOException;
}
