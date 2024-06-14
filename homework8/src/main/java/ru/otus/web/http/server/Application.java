package ru.otus.web.http.server;

public class Application {
    public static void main(String[] args) {
        int port = Integer.parseInt((String)System.getProperties().getOrDefault("port", "8189"));
        String staticResourcesPath = (String)System.getProperties()
                .getOrDefault("staticResourcesPath", "static/");
        new HttpServer(port, staticResourcesPath).start();
    }
}
