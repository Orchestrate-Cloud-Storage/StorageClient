package com.llnw.storage.client;

import com.google.common.io.Closeables;
import com.google.common.util.concurrent.Uninterruptibles;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.channels.ClosedByInterruptException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class EndpointIntegrationHelper {
    private static final Logger log = LoggerFactory.getLogger(EndpointIntegrationHelper.class);


    public void listTest(Endpoint endpoint) throws Exception {
        try {
            List<String> list = endpoint.listFiles("/");

            assertTrue(list.size() > 1);
            assertTrue(list.get(0).length() > 1);
        } finally {
            Closeables.closeQuietly(endpoint);
        }
    }


    public void interruptTest(Endpoint endpoint, File testFile) throws Exception {
        final long time = 500; // ms
        final AtomicBoolean interrupt = new AtomicBoolean();
        final AtomicLong itime = new AtomicLong();

        endpoint.noop();
        log.info("Forced login");

        final Thread thisThread = Thread.currentThread();
        Thread interrupter = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!interrupt.get())
                    Uninterruptibles.sleepUninterruptibly(1, TimeUnit.MILLISECONDS);

                Uninterruptibles.sleepUninterruptibly(time, TimeUnit.MILLISECONDS);

                thisThread.interrupt();
                itime.set(System.nanoTime());
                log.info("Bing");
            }
        });

        interrupter.start();

        long start = System.nanoTime();
        interrupt.set(true);
        try {
            endpoint.makeDirectory("/content/abc123");
            endpoint.upload(testFile, "/content/abc123", "test.txt", null);
            fail();
        } catch (ClosedByInterruptException e) {
            // Ignore
        } catch (InterruptedIOException e) {
            // Ignore
        } finally {
            Closeables.closeQuietly(endpoint);
        }
        Thread.interrupted();
        long elapsed = (System.nanoTime() - start) / (1000 * 1000);
        log.info("{}", elapsed);

        assertTrue(time     < elapsed);
        assertTrue(time * 20 > elapsed);
    }


    public void createDelete(Endpoint endpoint, String dir, File testFile) throws IOException {
        createDelete(endpoint, dir, testFile, false);
    }

    public void createDelete(Endpoint endpoint, String dir, File testFile, boolean testExists) throws IOException {
        try {
            endpoint.noop(); // Force login

            if (testExists) {
                if (endpoint.exists(dir)) {
                    endpoint.deleteDirectory(dir);
                }
                assertFalse(endpoint.exists(dir));
            }
            endpoint.makeDirectory(dir);
            if (testExists) {
                assertTrue(endpoint.exists(dir));
            }

            // cleanup any existing files first
            List<String> files = endpoint.listFiles(dir);
            for (String file : files) {
                endpoint.deleteFile(dir + File.separator + file);
            }

            final String fileName = "test.txt";
            final String remoteFilePath = dir + File.separator + fileName;
            if (testExists) {
                assertFalse(endpoint.exists(remoteFilePath));
            }

            endpoint.upload(testFile, dir, fileName, null);
            files = endpoint.listFiles(dir);

            assertTrue(files.size() == 1);
            assertEquals(files.get(0), fileName);
            if (testExists) {
                assertTrue(endpoint.exists(remoteFilePath));
            }

            for (String file : files) {
                endpoint.deleteFile(dir + File.separator + file);
            }

            endpoint.deleteDirectory(dir);

            if (testExists) {
                assertFalse(endpoint.exists(remoteFilePath));
            }
        } finally {
            Closeables.closeQuietly(endpoint);
        }
    }
}
