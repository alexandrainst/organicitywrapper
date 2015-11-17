package dk.aarhus.organicity.controller;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * This is the main starting point for our application (as specified by web.xml.
 * This class is instantiated when the tomcat server is started
 * @author neerbek
 *
 */
public class WebApplicationLauncher extends ResourceConfig {

    public WebApplicationLauncher() {
        super();
        DataSourceListUpdater.initialize();		

        packages("dk.alexandra.organicity.controller");
    }

}