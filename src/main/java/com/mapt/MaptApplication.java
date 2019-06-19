package com.mapt;

import com.mapt.resources.HelloResource;
import io.dropwizard.Application;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MaptApplication extends Application<MaptConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MaptApplication().run(args);
    }

    @Override
    public String getName() {
        return "mapt";
    }

    @Override
    public void initialize(final Bootstrap<MaptConfiguration> bootstrap) {
    	bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/", "/", "index.html"));
    }

    @Override
    public void run(final MaptConfiguration configuration,
                    final Environment environment) {

        final HelloResource resource = new HelloResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().setUrlPattern("/api/*");
        environment.jersey().register(resource);
    }

}
