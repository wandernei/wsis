package example.serverSystem.app;

import example.serverSystem.WeatherStationController;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;

public class RunService extends ResourceConfig {
    public RunService() throws IOException {
//        String[] args = {""};
//        WeatherStationController.main(args);

        // Define the package which contains the service classes.
        packages("example.serverSystem.services");
    }
}
