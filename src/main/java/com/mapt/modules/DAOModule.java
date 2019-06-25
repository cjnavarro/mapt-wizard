package com.mapt.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mapt.MaptConfiguration;
import com.mapt.core.User;
import com.mapt.db.UserDAO;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;

import javax.inject.Named;

public class DAOModule implements Module
{
	@Override
    public void configure(Binder binder) {}

    @Provides
    @Named("dao")
    public UserDAO provideDAO(MaptConfiguration configuration)
    {
    	HibernateBundle<MaptConfiguration> hibernate = new HibernateBundle<MaptConfiguration>(User.class)
    	    {
    	        @Override
    	        public DataSourceFactory getDataSourceFactory(MaptConfiguration configuration) {
    	            return configuration.getDataSourceFactory();
    	        }
    	    };
    	
    	return new UserDAO(hibernate.getSessionFactory());
    }
   
}
