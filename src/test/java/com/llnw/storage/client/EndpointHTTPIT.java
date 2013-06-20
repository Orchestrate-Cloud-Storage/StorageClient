package com.llnw.storage.client;

import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import com.llnw.storage.client.io.Chunk;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.Duration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class EndpointHTTPIT {
    private static File testFile;

    @Autowired
    private EndpointHTTP http;

    @Autowired
    private EndpointIntegrationHelper helper;


    @BeforeClass
    public static void setup() throws IOException {
        testFile = File.createTempFile("upload_this", "tmp");
        BufferedWriter os = new BufferedWriter(new FileWriter(testFile));

        for (int i = 0; i < 100000; i++) {
            os.append("This is a test file");
            os.newLine();
            os.append("Testing, one two three");
            os.newLine();
        }

        IOUtils.closeQuietly(os);
    }


    @AfterClass
    public static void teardown() {
        FileUtils.deleteQuietly(testFile);
    }


    @Test
    public void listTest() throws Exception {
        helper.listTest(http);
    }


    @Test
    public void createDelete() throws Exception {
        helper.createDelete(http, "/abc_123", testFile);
    }


    @Test
    public void interruptTest() throws Exception {
        helper.interruptTest(http, testFile);
    }


    @Test
    public void createDeleteExistenceChecks() throws Exception {
        helper.createDelete(http, "/mike_abc_123", testFile, true);
    }


    @Test
    public void multipartBasic() throws Exception {
        final String mpDir = "/multipartTest/" + UUID.randomUUID();
        final List<Chunk> chunks = Lists.newArrayList();

        final int number = 10;
        final long len = testFile.length();
        for (int i = 0; i < number; i++) {
            chunks.add(new Chunk(i, i * len / number, len / number, true));
        }

        try {
            http.makeDirectory(mpDir);
            http.startMultipartUpload(mpDir, "chunked.txt");
            http.uploadPart(testFile, chunks.iterator(), null);
            http.completeMultipartUpload();
            assertTrue(http.exists(mpDir + "/chunked.txt"));
            http.deleteFile(mpDir + "/chunked.txt");
        } finally {
            http.deleteDirectory(mpDir);
            Closeables.closeQuietly(http);
        }
    }
}
