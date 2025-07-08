package com.itinerary.handlers;

import io.vertx.ext.mongo.MongoClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ItineraryHandler {

    private final MongoClient mongo;

    public ItineraryHandler(MongoClient mongo) {
        this.mongo = mongo;
    }

    public void createItem(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();
        body.put("visited", false);

        mongo.insert("itineraries", body, res -> {
            if (res.succeeded()) {
                ctx.response().setStatusCode(201).end("Created");
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    public void getItems(RoutingContext ctx) {
        String userId = ctx.request().getParam("userId");
        JsonObject query = new JsonObject().put("userId", userId);

        mongo.find("itineraries", query, res -> {
            if (res.succeeded()) {
                ctx.json(res.result());
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    public void updateItem(RoutingContext ctx) {
        String id = ctx.pathParam("id");
        JsonObject updates = ctx.body().asJsonObject();
        JsonObject query = new JsonObject().put("_id", id);
        JsonObject updateDoc = new JsonObject().put("$set", updates);

        mongo.updateCollection("itineraries", query, updateDoc, res -> {
            if (res.succeeded()) {
                ctx.response().end("Updated");
            } else {
                ctx.fail(res.cause());
            }
        });
    }

    public void deleteItem(RoutingContext ctx) {
        String id = ctx.pathParam("id");
        JsonObject query = new JsonObject().put("_id", id);

        mongo.removeDocument("itineraries", query, res -> {
            if (res.succeeded()) {
                ctx.response().end("Deleted");
            } else {
                ctx.fail(res.cause());
            }
        });
    }
}
