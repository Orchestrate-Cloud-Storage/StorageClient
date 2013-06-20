package com.llnw.storage.client;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class EndpointFTPIT {

    private static File testFile;

    @Autowired
    private EndpointFTP ftp;

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
        helper.listTest(ftp);
    }


    @Test
    public void createDelete() throws Exception {
        helper.createDelete(ftp, "/content/abc_123", testFile);
    }


    @Test
    public void interruptTest() throws Exception {
        helper.interruptTest(ftp, testFile);
    }


    @Test
    public void createDeleteExistenceChecks() throws Exception {
        helper.createDelete(ftp, "/content/mike_abc_69", testFile, true);
    }
}
