package sz.mycolonyback.app.config;

import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.springframework.boot.jooq.autoconfigure.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

	@Bean
	DefaultConfigurationCustomizer jooqRenderSettingsCustomizer() {
		return configuration -> configuration.set(new Settings()
			.withRenderQuotedNames(RenderQuotedNames.NEVER));
	}
}
