package com.mycompany.sunrisedentalclinic;

import com.mycompany.sunrisedentalclinic.service.AppointmentService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class RestServer {

    private static final String BASE_URI =
            "http://localhost:8080/api/";

    public static void main(String[] args) {

        ResourceConfig config =
                new ResourceConfig()
                        .register(AppointmentService.class);

        HttpServer server =
                GrizzlyHttpServerFactory.createHttpServer(
                        URI.create(BASE_URI),
                        config
                );

        System.out.println(
                "REST server started at " + BASE_URI
        );

        System.out.println(
                "Press Enter to stop the server."
        );

        try {
            System.in.read();
        } catch (Exception e) {
            System.out.println(
                    "Server stopped."
            );
        }

        server.shutdownNow();
    }
}
