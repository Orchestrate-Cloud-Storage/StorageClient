package com.llnw.storage.client;

public enum MultipartStatus {
    UNKNOWN,
    NEW,
    READY,
    COMPLETE, // Set after completeMultipartUpload is called
    MERGE,
    JOIN,
    SUCCESS, // This is when the file has been successfully merged
    CLEANUP,
    DELETED,
    ERROR
}
