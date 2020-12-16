package com.whistle.code.netty.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Gentvel
 * @version 1.0.0
 */
public class BioClient {
    public static void main(String[] args) throws IOException {
        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress(6888));
        final OutputStream outputStream = socket.getOutputStream();

        try (Scanner scanner = new Scanner(System.in)) {
            String s = scanner.nextLine();
            while (s != null && !"end".equals(s)) {
                outputStream.write(s.getBytes());
                s = scanner.nextLine();
            }
        }

        outputStream.close();
        socket.close();
    }
}
