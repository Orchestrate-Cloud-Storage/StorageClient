package com.llnw.storage.client;

import com.google.common.base.Objects;


public class MultipartPiece {
    public final int number;
    public final int state;
    public final int size;
    public final int error;

    public MultipartPiece(int number, int state, int size, int error) {
        this.number = number;
        this.state = state;
        this.size = size;
        this.error = error;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass())
                .add("number", number)
                .add("state", state)
                .add("size", size)
                .add("error", error).toString();
    }

    public MultipartStatus getStatus() {
        return MultipartStatus.fromInt(state);
    }
}
