package ch.meineinitiative.service.impl;

import ch.meineinitiative.domain.Initiative;
import ch.meineinitiative.repository.InitiativeRepository;
import ch.meineinitiative.service.InitiativeService;
import ch.meineinitiative.service.dto.InitiativeDTO;
import ch.meineinitiative.service.mapper.InitiativeMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing Initiative.
 */
@Service
@Transactional
public class InitiativeServiceImpl implements InitiativeService {

    private final Logger log = LoggerFactory.getLogger(InitiativeServiceImpl.class);

    private final InitiativeRepository initiativeRepository;

    private final InitiativeMapper initiativeMapper;

    public InitiativeServiceImpl(InitiativeRepository initiativeRepository, InitiativeMapper initiativeMapper) {
        this.initiativeRepository = initiativeRepository;
        this.initiativeMapper = initiativeMapper;
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
    public List<InitiativeDTO> findAll(String title) {
        log.debug("Request to get all Initiatives");

        Comparator<Initiative> comparator = tanimotoComparator(title);

        return initiativeRepository.findAll().stream()
            .sorted(comparator)
            .filter(i -> tanimoto(title, i.getTitle()) <= 0.7)
            .limit(5)
            .map(initiativeMapper::toDto).collect(Collectors.toList());
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
        Initiative initiative = initiativeRepository.findOne(id);
        return initiativeMapper.toDto(initiative);
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
