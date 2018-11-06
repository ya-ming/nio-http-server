package com.yaming.http.server;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HTTPServer {
    private static Logger logger = LogManager.getLogger(HTTPServer.class);

    private int port = 80;
    private ExecutorService pool;
    public Selector selector;

    public HTTPServer(int port) {

        this.port = port;

        int cpu = Runtime.getRuntime().availableProcessors();
        this.pool = Executors.newFixedThreadPool(cpu);
    }

    public void run() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverChannel.socket();
        selector = Selector.open();
        InetSocketAddress localPort = new InetSocketAddress(port);
        serverSocket.bind(localPort);
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();

                try {
                    if (key.isAcceptable()) {
                        accept(selector, key);
                    } else if (key.isWritable()) {
                        write(key);
                    } else if (key.isReadable()) {
                        read(key);
                    }
                } catch (IOException ex) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                    }
                }
            }
        }
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel channel = server.accept();

        logger.info("accept " + channel);

        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        logger.info("read " + channel);

        ByteBuffer buffer = ByteBuffer.allocate(20480);
        channel.read(buffer);

        pool.submit(new RequestHandler(key, this, buffer));
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        logger.info("write " + channel);
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        if (buffer.hasRemaining()) {
            channel.write(buffer);
        } else { // we're done
            channel.close();
        }
    }

    public void wakeupSelector() {
        selector.wakeup();
    }
}