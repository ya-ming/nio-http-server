package com.yaming.http.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

public class ByteBufferPrinter {
    private static Logger logger = LogManager.getLogger(ByteBufferPrinter.class);

    public static void print(ByteBuffer buffer) {
        logger.info(buffer.toString());
        byte[] buf = buffer.array();
        String string = new String(buf);
        logger.info(string);
    }
}
