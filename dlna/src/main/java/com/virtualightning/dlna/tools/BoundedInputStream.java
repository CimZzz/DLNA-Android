package com.virtualightning.dlna.tools;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xjw04 on 17/5/31.
 */
public class BoundedInputStream extends InputStream {
    private final InputStream inputStream;
    private long size;

    public BoundedInputStream(InputStream inputStream, long size) {
        this.inputStream = inputStream;
        this.size = size;
    }

    @Override
    public int read() throws IOException {
        if(this.size <= 0)
            return -1;
        else {
            this.size--;
            return this.inputStream.read();
        }
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
