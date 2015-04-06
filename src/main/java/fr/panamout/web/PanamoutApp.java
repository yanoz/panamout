package fr.panamout.web;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import fr.panamout.web.config.PanamoutConfig;
import fr.panamout.web.injection.PanamoutModule;
import fr.panamout.web.resources.ImportResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yann on 4/5/15.
 */
public class PanamoutApp extends Service<PanamoutConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanamoutApp.class);

    /**
     * Start Panamout application.
     */
    public static void main(String[] arguments) throws Exception {

        String[] args = arguments;
        if (arguments.length == 1 && "server".equals(arguments[0])) {
            args = new String[2];
            args[0] = "server";
            args[1] = "/var/panamout/panamout.yml";
            System.out.println("Load default configuration file: /var/panamout/panamout.yml\n" + //
                    "(You can overide default file with command parameters: server [file])\n");
        }
        new PanamoutApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<PanamoutConfig> bootstrap) {
        bootstrap.setName("PanamOut application");
        bootstrap.addBundle(new AssetsBundle("/docs/", "/docs", "index.html"));
    }

    public void configureLogback() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(context);
        context.reset();
        try {
            jc.doConfigure(getLogbackConfigFileUrl());
        } catch (JoranException | MalformedURLException | FileNotFoundException e) {
            System.out.println("File ./logback.xml not found!");
            System.exit(1);
        }
    }

    private URL getLogbackConfigFileUrl() throws MalformedURLException, FileNotFoundException {
        URL logbackFileUrl = Thread.currentThread().getContextClassLoader().getResource("logback.xml");
        if (logbackFileUrl != null) {
            return logbackFileUrl;
        }
        System.out.println("Load logging configuration file: /var/panamout/logback.xml");
        File configFile = new File("/var/panamout/logback.xml");
        if (!configFile.isFile()) {
            throw new FileNotFoundException("File /var/panamout/logback.xml not found!");
        }
        return configFile.toURI().toURL();
    }


    @Override
    public void run(PanamoutConfig config, Environment environment) {
        configureLogback();
        Injector injector = Guice.createInjector(new PanamoutModule(config));

        try {
            environment.addResource(injector.getInstance(ImportResource.class));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
