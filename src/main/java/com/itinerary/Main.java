package com.itinerary;

import io.vertx.core.Vertx;

public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle(), res -> {
            if (res.succeeded()) {
                System.out.println("Main Verticle deployed successfully");
            } else {
                System.out.println("Deployment failed: " + res.cause());
            }
        });
    }
}
