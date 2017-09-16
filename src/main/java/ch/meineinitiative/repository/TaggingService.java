package ch.meineinitiative.repository;

import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "Simple-Gateway", url = "https://api.neofonie.de/rest/txt/analyzer", configuration = TaggingService.FormConfiguration.class
)
public interface TaggingService {

    @RequestMapping(method = RequestMethod.POST, path = "/", consumes = "application/x-www-form-urlencoded")
    String tag(@RequestParam("title") String title, @RequestParam("services") String services, Map<String, ?> text, @RequestHeader("X-Api-Key") String accessToken);

    @Configuration
    public static class FormConfiguration {

        @Bean
        @Primary
        @Scope("prototype")
        public Encoder feignFormEncoder() {
            return new FormEncoder();
        }
    }
}
