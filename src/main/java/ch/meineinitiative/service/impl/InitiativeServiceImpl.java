package ch.meineinitiative.service.impl;

import ch.meineinitiative.domain.Initiative;
import ch.meineinitiative.domain.enumeration.Status;
import ch.meineinitiative.repository.InitiativeRepository;
import ch.meineinitiative.repository.SrfService;
import ch.meineinitiative.repository.TaggingService;
import ch.meineinitiative.service.InitiativeService;
import ch.meineinitiative.service.dto.InitiativeDTO;
import ch.meineinitiative.service.dto.NewsDTO;
import ch.meineinitiative.service.dto.TagDTO;
import ch.meineinitiative.service.mapper.InitiativeMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;


/**
 * Service Implementation for managing Initiative.
 */
@Service
@Transactional
public class InitiativeServiceImpl implements InitiativeService {

    private final Logger log = LoggerFactory.getLogger(InitiativeServiceImpl.class);

    private final InitiativeRepository initiativeRepository;

    private final InitiativeMapper initiativeMapper;

    private final TaggingService taggingService;

    private final SrfService srfService;

    @Autowired
    private ObjectMapper objectMapper;
    public static final Set<String> SET = new HashSet<>();

    static {
        SET.add("Volksinitiative");
        SET.add("Bundesverfassung");
        SET.add("Eidgenossenschaft");
    }

    public InitiativeServiceImpl(InitiativeRepository initiativeRepository, InitiativeMapper initiativeMapper, TaggingService taggingService, SrfService srfService) {
        this.initiativeRepository = initiativeRepository;
        this.initiativeMapper = initiativeMapper;
        this.taggingService = taggingService;
        this.srfService = srfService;
    }

    public static double tanimoto(String string1, String string2) {
        List<Pair<String, String>> pairsString1 = createPairs(string1);
        List<Pair<String, String>> pairsString2 = createPairs(string2);

        if (pairsString1.size() < pairsString2.size()) {
            List<Pair<String, String>> back = pairsString1;
            pairsString1 = pairsString2;
            pairsString2 = back;
        }

        int intersection = 0;
        int unionDelta = 0;

        for (Pair<String, String> stringStringPair : pairsString1) {
            boolean remove = pairsString2.remove(stringStringPair);
            if (remove) {
                intersection++;
            } else {
                unionDelta++;
            }
        }

        double unionSize = intersection + unionDelta;

        return (unionSize - intersection) / unionSize;
    }

    private static List<Pair<String, String>> createPairs(String string1) {
        List<Pair<String, String>> toReturn = new ArrayList<>(string1.length());
        for (int i = 0; i < string1.length() - 1; i++) {
            toReturn.add(Pair.of(string1.substring(i, i + 1), string1.substring(i + 1, i + 2)));
        }
        return toReturn;
    }

    /**
     * Save a initiative.
     *
     * @param initiativeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InitiativeDTO save(InitiativeDTO initiativeDTO) {
        log.debug("Request to save Initiative : {}", initiativeDTO);
        Initiative initiative = initiativeMapper.toEntity(initiativeDTO);
        if (StringUtils.isNotEmpty(initiative.getText())) {
            String tag = taggingService.tag(initiative.getText(),
                "entities,categories,tags", "a5a54e61-ad82-ecb9-dc44-b73c4d4b7741");

            initiative.setTag(tag);
        }

        initiative = initiativeRepository.save(initiative);
        return initiativeMapper.toDto(initiative);
    }

    /**
     * Get all the initiatives.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InitiativeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Initiatives");
        return initiativeRepository.findAll(pageable)
            .map(initiativeMapper::toDto);
    }

    /**
     * Get all the initiatives.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<InitiativeDTO> findAll(String title, Status status) {
        log.debug("Request to get all Initiatives");

        Comparator<Initiative> comparator = tanimotoComparator(title);

        return initiativeRepository.findAll().stream()
            .sorted(comparator)
            .filter(i -> i.getStatus() == status)
            .filter(i -> tanimoto(title, i.getTitle().replace("Eidgenössische Volksinitiative", "")) <= 0.8)
            .limit(5)
            .map(this::createDto).collect(Collectors.toList());
    }

    private InitiativeDTO createDto(Initiative initiative) {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        initiativeDTO.setTitle(initiative.getTitle());
        initiativeDTO.setId(initiative.getId());
        return initiativeDTO;
    }

    private Comparator<Initiative> tanimotoComparator(String title) {
        return (a, b) -> {
            String title1 = a.getTitle();
            String title2 = b.getTitle();

            return Double.compare(tanimoto(title, title1), tanimoto(title, title2));
        };
    }


    /**
     * Get one initiative by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public InitiativeDTO findOne(Long id) {
        log.debug("Request to get Initiative : {}", id);
        Initiative one = initiativeRepository.findOne(id);
        InitiativeDTO initiativeDTO = initiativeMapper.toDto(one);

        if (one.getTag() != null) {
            Set<String> tags = toTagSet(one.getTag());
            initiativeDTO.setTags(tags);

            String searchQuery = createSearchQuery(tags);
            if (StringUtils.isNotEmpty(searchQuery)) {
                NewsDTO news = srfService.findByTags(searchQuery, "VERaczKNt8KjWZs8ejMPAWXr1CXM4HJX");
                initiativeDTO.setNewsFeed(news);
            }
        }

        return initiativeDTO;
    }

    private String createSearchQuery(Set<String> strings) {
        return strings.stream().collect(joining(" "));
    }

    private Set<String> toTagSet(String tag) {
        try {
            Set<String> tags = new HashSet<>();

            TagDTO tagDTO = objectMapper.readValue(tag, TagDTO.class);

            Optional.ofNullable(tagDTO.getEntities())
                .map(l -> l.stream().map(TagDTO.Entity::getSurface).filter(InitiativeServiceImpl::isNotDefault))
                .ifPresent(s -> s.collect(toCollection(() -> tags)));

            Optional.ofNullable(tagDTO.getTags())
                .map(l -> l.stream().map(TagDTO.Tag::getTerm).filter(InitiativeServiceImpl::isNotDefault))
                .ifPresent(s -> s.collect(toCollection(() -> tags)));

            return tags;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptySet();
    }

    static boolean isNotDefault(String defaultStuff) {

        return !SET.contains(defaultStuff);

    }

    /**
     * Delete the  initiative by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Initiative : {}", id);
        initiativeRepository.delete(id);
    }
}
