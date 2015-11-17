package dk.alexandra.organicity.controller;

import org.glassfish.jersey.server.ResourceConfig;

public class MyApplication extends ResourceConfig {

    public MyApplication() {
        super();

        packages("dk.alexandra.organicity.controller");
    }

}