package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.web.webclient.ScrapperClient;
import ru.tinkoff.edu.java.bot.web.webclient.ScrapperClientImpl;

@Configuration
public class ClientConfig {

    @Value("${scrapper.baseurl:" + ScrapperClientImpl.BASE_URL + "}")
    private String scrapperBaseUrl;

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClientImpl(scrapperBaseUrl);
    }

}
