package at.yawk.mojangnames;

import at.yawk.mojangapi.EndpointProvider;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.URLTemplateLoader;
import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

/**
 * @author yawkat
 */
@Configuration
public class Config {
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4)
            .appendLiteral('-')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .appendLiteral(' ')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .appendLiteral(" UTC")
            .toFormatter();

    @Bean
    EndpointProvider endpointProvider() {
        return EndpointProvider.getProvider();
    }

    @Bean
    ViewResolver viewResolver() {
        return new HandlebarsViewResolver() {
            {
                registerHelper("instant", new Helper<Instant>() {
                    @Override
                    public CharSequence apply(Instant context, Options options) throws IOException {
                        return context == null ? "" : context.atOffset(ZoneOffset.UTC).format(FORMATTER);
                    }
                });
            }

            @Override
            protected URLTemplateLoader createTemplateLoader(ApplicationContext context) {
                URLTemplateLoader templateLoader = new ClassPathTemplateLoader();
                // Override prefix and suffix.
                templateLoader.setPrefix(getPrefix());
                templateLoader.setSuffix(getSuffix());
                return templateLoader;
            }
        };
    }
}
