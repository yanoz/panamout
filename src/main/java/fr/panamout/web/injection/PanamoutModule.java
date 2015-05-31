package fr.panamout.web.injection;

import com.google.inject.AbstractModule;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import fr.panamout.core.actors.Persister;
import fr.panamout.web.config.PanamoutConfig;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * Created by yann on 4/5/15.
 */
public class PanamoutModule extends AbstractModule{



    PanamoutConfig config;

    public PanamoutModule(PanamoutConfig config) {
        this.config = config;

    }

    @Override
    protected void configure() {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(config.database.driver);
        } catch (PropertyVetoException e) {
            //LOGGER.error(e.getMessage(), e);
        }

        cpds.setJdbcUrl(config.database.url);
        cpds.setUser(config.database.username);
        cpds.setPassword(config.database.password);
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        bind(DataSource.class).toInstance(cpds);

        requestStaticInjection(Persister.class);
    }
}
