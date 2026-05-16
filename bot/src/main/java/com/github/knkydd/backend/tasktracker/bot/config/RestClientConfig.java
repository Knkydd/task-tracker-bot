package com.github.knkydd.backend.tasktracker.bot.config;

import com.github.knkydd.backend.tasktracker.bot.web.TaskAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${app.core.baseUri}")
    private String baseURI;

    @Bean
    public RestClient taskRestClient() {
        return RestClient.builder()
                .baseUrl(baseURI)
                .defaultHeader(MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public TaskAPI taskAPI(RestClient taskRestClient) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(taskRestClient))
                .build()
                .createClient(TaskAPI.class);
    }
}
