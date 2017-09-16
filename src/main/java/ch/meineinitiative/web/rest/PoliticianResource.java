package ch.meineinitiative.web.rest;

import ch.meineinitiative.repository.SrfService;
import ch.meineinitiative.service.dto.NewsDTO;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Comment.
 */
@RestController
@RequestMapping("/api")
public class PoliticianResource {

    private final Logger log = LoggerFactory.getLogger(PoliticianResource.class);

    private static final String ENTITY_NAME = "comment";

    private final SrfService srfService;

    public PoliticianResource(SrfService srfService) {
        this.srfService = srfService;
    }

    @GetMapping("/politician")
    @Timed
    public NewsDTO getComment(@RequestParam("query") String query) {
        return srfService.findByTags(query, "VERaczKNt8KjWZs8ejMPAWXr1CXM4HJX");
    }
}
