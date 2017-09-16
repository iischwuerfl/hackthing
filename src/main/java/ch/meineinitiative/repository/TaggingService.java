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

@FeignClient(name = "Simple-Gateway", url = "https://api.neofonie.de/rest/txt/", configuration = TaggingService.FormConfiguration.class
)
public interface TaggingService {

    @RequestMapping(method = RequestMethod.GET, path = "/analyzer")
    String tag(@RequestParam("text") String text, @RequestParam("services") String services, @RequestHeader("X-Api-Key") String accessToken);

    @Configuration
    public class FormConfiguration {

        @Bean
        @Primary
        @Scope("prototype")
        public Encoder feignFormEncoder() {
            return new FormEncoder();
        }
    }
}
