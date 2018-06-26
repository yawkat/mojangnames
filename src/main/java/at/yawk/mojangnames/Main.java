package at.yawk.mojangnames;

import at.yawk.cricket.loader.DependencyException;
import at.yawk.cricket.loader.DependencyLoader;
import at.yawk.logging.jul.FormatterBuilder;
import at.yawk.logging.jul.Loggers;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author yawkat
 */
@Log
@Configuration
@EnableAutoConfiguration
@Component
@ComponentScan
public class Main {
    static {
        Logger root = Logger.getLogger("");
        ConsoleHandler handler = new ConsoleHandler();
        FormatterBuilder.createTimeLevel().build(handler);
        Loggers.replaceHandlers(root, handler);
    }

    public static void main(String[] args) throws DependencyException {
        start(args);
    }

    private static void start(String[] args) {
        new SpringApplication(Main.class).run(args);
    }
}
