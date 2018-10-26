package com.yaming.http.client;

public class Main {
    public static void main(String[] args) {
        Client httpClient = new Client();
        try {
            httpClient.HttpPost("a", "b", "http://127.0.0.1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
