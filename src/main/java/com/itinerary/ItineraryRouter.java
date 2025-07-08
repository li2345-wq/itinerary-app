package com.itinerary;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.mongo.MongoClient;
import com.itinerary.handlers.ItineraryHandler;

public class ItineraryRouter {

    public static void setup(Vertx vertx, Router router, MongoClient mongo) {
        ItineraryHandler handler = new ItineraryHandler(mongo);

        router.route("/api/itinerary*").handler(BodyHandler.create());

        router.post("/api/itinerary").handler(handler::createItem);
        router.get("/api/itinerary").handler(handler::getItems);
        router.put("/api/itinerary/:id").handler(handler::updateItem);
        router.delete("/api/itinerary/:id").handler(handler::deleteItem);
    }
}
