package com.llnw.storage.client.io;

import com.google.common.base.Objects;

public class Chunk {
    public final int number;
    public final long offset;
    public final long length;
    public final boolean appending; // is this chunk the latest in the file?

    public Chunk(int number, long offset, long length, boolean appending) {
        this.number = number;
        this.offset = offset;
        this.length = length;
        this.appending = appending;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(number, offset, length, appending);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Chunk) {
            Chunk chunk = (Chunk)obj;
            return number == chunk.number &&
                    offset == chunk.offset &&
                    length == chunk.length &&
                    appending == chunk.appending;
        }
        return false;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(Chunk.class)
                .add("number", number)
                .add("offset", offset)
                .add("length", length)
                .add("appending", appending)
                .toString();
    }
}
