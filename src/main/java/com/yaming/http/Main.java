package com.yaming.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Main {
    private static Logger logger = LogManager.getLogger(Main.class);


    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(
                    "Usage: java HTTPServer file port encoding");
            return;
        }

        try {
            // set the port to listen on
            int port;
            try {
                port = Integer.parseInt(args[1]);
                if (port < 1 || port > 65535) port = 80;
            } catch (RuntimeException ex) {
                port = 80;
            }

            logger.info("Server is listening at port " + port);

            String encoding = "UTF-8";
            if (args.length > 2) encoding = args[2];

            HTTPServer server
                    = new HTTPServer(port);
            server.run();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
