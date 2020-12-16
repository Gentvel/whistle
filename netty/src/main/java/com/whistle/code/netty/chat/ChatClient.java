package com.whistle.code.netty.chat;

import java.io.IOException;
import java.util.Scanner;

/**
 * TODO
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class ChatClient {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.sendMessage();
    }
}
