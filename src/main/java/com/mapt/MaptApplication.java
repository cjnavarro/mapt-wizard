package com.mapt;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.mapt.auth.MaptAuthenticator;
import com.mapt.auth.MaptAuthorizer;
import com.mapt.aws.AWSModule;
import com.mapt.core.User;
import com.mapt.db.UserDAO;
import com.mapt.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MaptApplication extends Application<MaptConfiguration>
{
    public static void main(final String[] args) throws Exception
    {
        new MaptApplication().run(args);
    }

    @Override
    public String getName()
    {
        return "mapt";
    }
    
    private final HibernateBundle<MaptConfiguration> hibernate = new HibernateBundle<MaptConfiguration>(User.class)
    {
        @Override
        public DataSourceFactory getDataSourceFactory(MaptConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(final Bootstrap<MaptConfiguration> bootstrap)
    {
    	GuiceBundle<MaptConfiguration> awsBundle = GuiceBundle.<MaptConfiguration>newBuilder()
                .addModule(new AWSModule())
                .setConfigClass(MaptConfiguration.class)
                .enableAutoConfig(getClass().getPackage().getName())
                .build();
    	
        bootstrap.addBundle(awsBundle);
    	
    	bootstrap.addBundle(new ConfiguredAssetsBundle("/build/*", "/", "index.html"));
    	bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(final MaptConfiguration configuration, final Environment environment)
    {
    	final UserDAO dao = new UserDAO(hibernate.getSessionFactory());
        final UserResource resource = new UserResource(dao);  
        final MaptAuthenticator proxyAuth = new UnitOfWorkAwareProxyFactory(hibernate).create(MaptAuthenticator.class, UserDAO.class, dao);
        
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(proxyAuth)
                    .setAuthorizer(new MaptAuthorizer())
                    .setRealm("mapt")
                    .setPrefix("PumpkinSpice") // So Basic
                    .buildAuthFilter()));
        
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        //environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        
        environment.jersey().setUrlPattern("/api/*");
        environment.jersey().register(resource);
    }

}
