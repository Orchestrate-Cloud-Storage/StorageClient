package com.llnw.storage.client.io;

import org.apache.commons.io.input.ProxyInputStream;

import javax.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class HeartbeatInputStream extends ProxyInputStream {
    private final ActivityCallback callback;

    public HeartbeatInputStream(InputStream proxy, @Nullable ActivityCallback callback)
            throws IOException {
        super(proxy);
        this.callback = callback;
        beforeRead(0);
    }


    @SuppressWarnings("resource")
    public HeartbeatInputStream(File file, @Nullable ActivityCallback callback)
            throws IOException {
        // The FileInputStream is closed when its channel is closed, so the suppressed warning is invalid.
        this(Channels.newInputStream(new FileInputStream(file).getChannel()), callback);
    }


    @Override
    public long skip(long bytes) throws IOException {
        beforeRead((int)bytes);
        final long rv = super.skip(bytes);
        afterRead((int)rv);
        return rv;
    }


    @Override
    public void reset() throws IOException {
        beforeRead(0);
        super.reset();
    }


    @Override
    protected void beforeRead(int n) throws IOException {
        if (callback != null)
            callback.callback();
    }


    public static InputStream wrap(final FileChannel fc, final Chunk chunk, final ActivityCallback callback)
            throws IOException {
        return wrap(fc.map(MapMode.READ_ONLY, chunk.offset, chunk.length), callback);
    }


    public static HeartbeatInputStream wrap(final ByteBuffer buf, final ActivityCallback callback)
            throws IOException {
        return new HeartbeatInputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                if (!buf.hasRemaining()) {
                    return -1;
                }
                return buf.get();
            }

            @Override
            public void reset() throws IOException {
                buf.position(0);
            }

            @Override
            public int read(byte[] bytes, int off, int len) throws IOException {
                if (!buf.hasRemaining()) {
                    return -1;
                }
                // Read only what's left
                len = Math.min(len, buf.remaining());
                buf.get(bytes, off, len);
                return len;
            }
        }, callback);
    }
}
