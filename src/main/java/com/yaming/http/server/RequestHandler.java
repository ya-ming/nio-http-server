package com.yaming.http.server;

import com.yaming.http.utils.ByteBufferPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.Callable;

public class RequestHandler implements Callable<Void> {
    private static Logger logger = LogManager.getLogger(RequestHandler.class);

    private ByteBuffer buffer;
    private ByteBuffer contentBuffer;
    private HTTPServer server;
    private SelectionKey key;

    public RequestHandler(SelectionKey key, HTTPServer server, ByteBuffer buffer) {
        this.key = key;
        this.server = server;
        this.buffer = buffer;

        // read the single file to serve
//        String contentType =
//                URLConnection.getFileNameMap().getContentTypeFor("d:\\main.cpp");
//        Path file = FileSystems.getDefault().getPath("d:\\main.cpp");
//        byte[] data = new byte[0];
//        try {
//            data = Files.readAllBytes(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ByteBuffer input = ByteBuffer.wrap(data);
//
//        String header = "HTTP/1.0 200 OK\r\n"
//                + "Server: NonblockingSingleFileHTTPServer\r\n"
//                + "Content-length: " + input.limit() + "\r\n"
//                + "Content-type: " + contentType + "\r\n\r\n";
//        byte[] headerData = header.getBytes(Charset.forName("US-ASCII"));
//
//        ByteBuffer byteBuffer = ByteBuffer.allocate(
//                input.limit() + headerData.length);
//        byteBuffer.put(headerData);
//        byteBuffer.put(input);
//        byteBuffer.flip();
//        this.contentBuffer = byteBuffer;
    }

    @Override
    public Void call() throws Exception {
        logger.info("call()");
        ByteBufferPrinter.print(buffer);

        Request request = new Request(buffer.array());
        Response response = new Response();

        Servlet servlet = new Servlet();
        servlet.service(request, response);

        key.interestOps(SelectionKey.OP_WRITE);
        key.attach(response.generateResponse(200));

        server.wakeupSelector();

        logger.info("call() ends");
        return null;
    }
}
