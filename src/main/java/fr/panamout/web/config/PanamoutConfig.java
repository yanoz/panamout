package fr.panamout.web.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by yann on 4/5/15.
 */
public class PanamoutConfig extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    public DatabaseConfig database = new DatabaseConfig();

}
