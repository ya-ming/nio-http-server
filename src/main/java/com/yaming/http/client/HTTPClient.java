package com.yaming.http.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public class HTTPClient {
    public static int DEFAULT_PORT = 19;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java HTTPClient host [port]");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (RuntimeException ex) {
            port = DEFAULT_PORT;
        }

        try {
            // create a nio channel
            SocketAddress address = new InetSocketAddress(args[0], port);
            SocketChannel channel = SocketChannel.open(address);

            // allocate a buffer
            ByteBuffer buffer = ByteBuffer.allocate(74);

            // output channel
            WritableByteChannel out = Channels.newChannel(System.out);

            // read data from channel and write into buffer
            while (channel.read(buffer) != -1) {
                // flip the buffer
                buffer.flip();

                // read data from the buffer and write into output channel
                out.write(buffer);

                // clear the buffer
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
