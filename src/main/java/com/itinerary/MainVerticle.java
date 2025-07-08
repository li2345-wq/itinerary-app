package com.itinerary;

import com.itinerary.auth.AuthRouter;
import com.itinerary.auth.JwtUtil;
import com.itinerary.db.MongoService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        // Load config.json
        JsonObject config = new JsonObject(Files.readString(Paths.get("src/main/resources/config.json")));

        String mongoUri = config.getString("mongodbUri");
        String jwtSecret = config.getString("jwtSecret");
        int port = config.getInteger("serverPort");

        // Init services
        MongoService mongoService = new MongoService(vertx, mongoUri);
        JwtUtil jwtUtil = new JwtUtil(vertx, jwtSecret);
        AuthRouter authRouter = new AuthRouter(mongoService, jwtUtil);

        // Setup router
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        // Register API routes
        authRouter.registerRoutes(router);

        // Serve signup and login HTML pages
        router.get("/signup").handler(ctx -> ctx.response().sendFile("public/signup.html"));
        router.get("/login").handler(ctx -> ctx.response().sendFile("public/login.html"));

        // Serve static files from resources/public/
        router.route("/*").handler(StaticHandler.create("public"));

        // Start HTTP server
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(port)
                .onSuccess(server -> {
                    System.out.println("✅ Server running at http://localhost:" + port);
                    startPromise.complete();
                })
                .onFailure(err -> {
                    System.err.println("❌ Failed to start server: " + err.getMessage());
                    startPromise.fail(err);
                });
    }
}
