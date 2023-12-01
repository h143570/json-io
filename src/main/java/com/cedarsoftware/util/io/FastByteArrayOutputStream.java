package com.cedarsoftware.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class FastByteArrayOutputStream extends OutputStream {

    private byte[] buf;
    private int count;

    public FastByteArrayOutputStream() {
        this(32);
    }

    public FastByteArrayOutputStream(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }
        buf = new byte[size];
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity - buf.length > 0) {
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        int oldCapacity = buf.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        buf = Arrays.copyOf(buf, newCapacity);
    }

    @Override
    public void write(int b) {
        ensureCapacity(count + 1);
        buf[count] = (byte) b;
        count += 1;
    }

    @Override
    public void write(byte[] b, int off, int len) {
        if ((b == null) || (off < 0) || (len < 0) ||
                (off > b.length) || (off + len > b.length) || (off + len < 0)) {
            throw new IndexOutOfBoundsException();
        }
        ensureCapacity(count + len);
        System.arraycopy(b, off, buf, count, len);
        count += len;
    }

    public void writeBytes(byte[] b) {
        write(b, 0, b.length);
    }

    public void reset() {
        count = 0;
    }

    public byte[] toByteArray() {
        return Arrays.copyOf(buf, count);
    }

    public int size() {
        return count;
    }

    public String toString() {
        return new String(buf, 0, count);
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(buf, 0, count);
    }

    @Override
    public void close() throws IOException {
        // No resources to close
    }
}
