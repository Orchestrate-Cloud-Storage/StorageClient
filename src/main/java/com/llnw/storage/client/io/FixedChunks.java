package com.llnw.storage.client.io;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class FixedChunks implements Iterable<Chunk> {

    //-------------------------------------------------------------
    // Constants
    //-------------------------------------------------------------

    private static final long MINIMUM_CHUNK_SIZE = 16 * 1024 * 1024; // 16 MiB
    private static final long MAXIMUM_CHUNK_SIZE = 512 * 1024 * 1024; // 512 MiB
    private static final int TARGET_CHUNKS = 100;

    //-------------------------------------------------------------
    // Variables - private final
    //-------------------------------------------------------------

    public final long size;
    public final long chunkSize;
    public final int totalChunks;

    //-------------------------------------------------------------
    // Constructors
    //-------------------------------------------------------------

    public FixedChunks(long size) {
        this(size, MINIMUM_CHUNK_SIZE, MAXIMUM_CHUNK_SIZE);
    }


    public FixedChunks(long size, long minimumChunkSize, long maximumChunkSize) {
        this.size = size;
        final long baseChunkSize = size / TARGET_CHUNKS;
        if (baseChunkSize < minimumChunkSize) // Use fewer chunks to keep chunk size up
            chunkSize = Math.min(size, minimumChunkSize); // Chunk size can never be > size
        else if (baseChunkSize > maximumChunkSize) // Use more chunks to keep chunk size down
            chunkSize = maximumChunkSize;
        else
            chunkSize = baseChunkSize;
        totalChunks = (int)((size + chunkSize - 1) / chunkSize);
    }

    //-------------------------------------------------------------
    // Methods - public
    //-------------------------------------------------------------

    @Override
    public Iterator<Chunk> iterator() {
        return new ChunkIterator();
    }

    //-------------------------------------------------------------
    // Classes - private
    //-------------------------------------------------------------

    private class ChunkIterator implements Iterator<Chunk> {

        //-------------------------------------------------------------
        // Variables - private
        //-------------------------------------------------------------

        private int chunk = 0;

        //-------------------------------------------------------------
        // Methods - public
        //-------------------------------------------------------------

        @Override
        public boolean hasNext() {
            return chunk < totalChunks;
        }


        @Override
        public Chunk next() {
            if (!hasNext()) throw new NoSuchElementException();
            chunk++;
            final long offset = (chunk - 1) * chunkSize;
            final long length = size - offset > chunkSize ? chunkSize : size - offset;
            return new Chunk(chunk - 1, offset, length, true);
        }


        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

