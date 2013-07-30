Limelight Orchestrate Cloud Storage Java Client
======================

Java client library for interacting with Limelight Orchestrate Cloud Storage via the HTTP JSON RPC API or via FTP.

How to use
----------

Given a hostname, username, and a password, you can use an EndpointFactory to get an Endpoint like so:

    final Endpoint agile = new EndpointFactory(host, user, pass).create(false);

The final parameter is whether to use an FTP or an HTTP endpoint. This pattern is used to allow seamless fallback to FTP from HTTP or vice-versa.

You can do lots of things with an Endpoint:

    agile.makeDirectory("/test/directory/here"); // recursive!
    final boolean exists = agile.exists("/test/directory/here"); // true

    final File sampleFile = File.createTempFile("test", ".txt");
    fill(sampleFile); // give it some data

    agile.upload(sampleFile, "/test/directory/here", "remotename.txt", null);
    final boolean sampleExists = agile.exists("/test/directory/here/remotename.txt"); // true

    agile.deleteFile("/test/directory/here/remotename.txt");
    agile.deleteDirectory("/test/directory/here");

All of these methods work whether you are using FTP or HTTP to access Agile.

The upload method optionally takes an ActivityCallback class, which is called every so often when data is sent.

Multipart Support
-----------------

To use the new Agile multipart upload, you must use the HTTP API:

    final EndpointMultipart mp = new EndpointHTTP(url, user, pass);
    final String path = "/test/multipart";

    mp.makeDirectory(path);

    // This ID is used to resume multipart uploads later. The EndpointHTTP
    // class will cache it while it's still being used, so there's no need to
    // specify it for uploadPart

    final String id = mp.startMultipartUpload(path, "file.txt");

    final File bigFile = getFile();

    // Use fixed size chunks
    final Iterator<Chunk> chunks = new FixedChunks(bigFile.size()).iterator();

    mp.uploadPart(bigFile, chunks, new ActivityCallback() {
        @Override
        public void callback() {
            System.out.println("Beep");
        }
    });

    mp.completeMultipartUpload();

    Thread.sleep(1000); // some time needs to pass
    mp.exists(path + "/file.txt"); // true

Multipart upload can be used simultaneously from different machines as long as they are not uploading the same chunk ID. Share the multipart ID on each machine/thread, and call resumeMultipartUpload instead of startMultipartUpload.

To stop a multipart upload, call abortMultipartUpload.

To start a multipart upload from a different machine or thread than the one that started the connection, you must use setMpid and resumeMultipartUpload:

    final EndpointMultipart mp = new EndpointHTTP(url, user, pass);
    mp.setMpid(mpid); // mpid from another thread or shared from another machine
    mp.resumeMultipartUpload(); // this will throw if invalid or if it's in the wrong state

    final MultipartStatus status = mp.getMultipartStatus(); // should be READY

FTP does not support multipart upload.

