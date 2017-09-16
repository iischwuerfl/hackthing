package ch.meineinitiative.repository;

import ch.meineinitiative.service.dto.NewsDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Simple-Gateway", url = "http://srgssr-prod.apigee.net/integrationlayer/2.0/srf/searchResultList")
public interface SrfService {

    @RequestMapping(method = RequestMethod.GET, path = "/video")
    NewsDTO findByTags(@RequestParam("q") String queryString, @RequestParam("apikey") String accessToken);
}
