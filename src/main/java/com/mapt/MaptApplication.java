package com.mapt;

import com.mapt.core.User;
import com.mapt.db.UserDAO;
import com.mapt.resources.HelloResource;
import io.dropwizard.Application;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
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
    
    private final HibernateBundle<MaptConfiguration> hibernate = new HibernateBundle<MaptConfiguration>(User.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(MaptConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(final Bootstrap<MaptConfiguration> bootstrap) {
    	bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/*", "/", "index.html"));
    	bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(final MaptConfiguration configuration,
                    final Environment environment) {
    	
    	final UserDAO dao = new UserDAO(hibernate.getSessionFactory());
    	
        final HelloResource resource = new HelloResource(
                configuration.getTemplate(),
                configuration.getDefaultName(),
                dao
        );
        
        environment.jersey().setUrlPattern("/api/*");
        environment.jersey().register(resource);
    }

}
