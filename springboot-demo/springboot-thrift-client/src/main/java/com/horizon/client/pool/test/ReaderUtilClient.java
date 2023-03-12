package com.horizon.client.pool.test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * Instantiates and uses a ReaderUtil. The GenericObjectPool supplied to the constructor will have
 * default configuration properties.
 */
public class ReaderUtilClient {

    public static void main(String[] args) {
        ObjectPool<StringBuffer> pool = new GenericObjectPool<>(new StringBufferFactory());

        ReaderUtil readerUtil = new ReaderUtil(pool);
        Reader reader = new StringReader("foo");

        try {
            System.out.println(readerUtil.readToString(reader));
            reader = new StringReader("foo1");
            System.out.println(readerUtil.readToString(reader));
            reader = new StringReader("foo2");
            System.out.println(readerUtil.readToString(reader));
            reader = new StringReader("foo3");
            System.out.println(readerUtil.readToString(reader));
            reader = new StringReader("foo4");
            System.out.println(readerUtil.readToString(reader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
