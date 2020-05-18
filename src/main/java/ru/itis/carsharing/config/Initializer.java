package ru.itis.carsharing.config;

import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.stereotype.Component;

@Component
@EnableJdbcHttpSession
public class Initializer
        extends AbstractHttpSessionApplicationInitializer {
    public Initializer() {
        super(ApplicationContextConfig.class);
    }
}