package com.mapt;

import io.dropwizard.Application;
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
        // TODO: application initialization
    }

    @Override
    public void run(final MaptConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
