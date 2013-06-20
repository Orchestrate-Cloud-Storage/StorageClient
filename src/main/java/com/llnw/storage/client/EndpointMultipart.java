package com.llnw.storage.client;

import com.llnw.storage.client.io.ActivityCallback;
import com.llnw.storage.client.io.Chunk;
import org.joda.time.Duration;

import javax.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public interface EndpointMultipart extends Endpoint {
    public String startMultipartUpload(String path, String name) throws IOException;
    public void resumeMultipartUpload(String mpid) throws IOException;
    public void uploadPart(File file, @Nullable Iterator<Chunk> chunks,
            @Nullable Duration heartbeatInterval, @Nullable ActivityCallback callback) throws IOException;
    public void abortMultipartUpload() throws IOException;
    public void completeMultipartUpload() throws IOException;
}
