package com.itinerary.models;

import io.vertx.core.json.JsonObject;

public class ItineraryItem {
    public String id;
    public String userId;
    public String location;
    public String dateTime;
    public boolean visited;

    public ItineraryItem() {}

    public ItineraryItem(JsonObject json) {
        this.id = json.getString("_id"); // Mongo returns _id
        this.userId = json.getString("userId");
        this.location = json.getString("location");
        this.dateTime = json.getString("dateTime");
        this.visited = json.getBoolean("visited", false);
    }

    public JsonObject toJson() {
        return new JsonObject()
                .put("userId", userId)
                .put("location", location)
                .put("dateTime", dateTime)
                .put("visited", visited);
    }
}
