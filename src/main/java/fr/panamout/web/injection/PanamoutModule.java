package fr.panamout.web.injection;

import com.google.inject.AbstractModule;
import fr.panamout.web.config.PanamoutConfig;

/**
 * Created by yann on 4/5/15.
 */
public class PanamoutModule extends AbstractModule{
    public PanamoutModule(PanamoutConfig config) {

    }

    @Override
    protected void configure() {

    }
}
