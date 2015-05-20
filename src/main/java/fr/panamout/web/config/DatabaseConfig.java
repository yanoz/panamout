package fr.panamout.web.config;

import org.hibernate.validator.constraints.NotEmpty;

public class DatabaseConfig {

    @NotEmpty
    public String driver;

    @NotEmpty
    public String url;

    @NotEmpty
    public String username;

    @NotEmpty
    public String password;
}