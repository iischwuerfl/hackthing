package ch.meineinitiative.service.impl;

import ch.meineinitiative.domain.Initiative;
import ch.meineinitiative.repository.InitiativeRepository;
import ch.meineinitiative.service.dto.InitiativeDTO;
import ch.meineinitiative.service.mapper.InitiativeMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InitiativeServiceImplTest {
    @Mock
    private InitiativeRepository initiativeRepository;

    private InitiativeMapper initiativeMapper = new InitiativeMapper() {
        @Override
        public InitiativeDTO toDto(Initiative initiative) {
            InitiativeDTO initiativeDTO = new InitiativeDTO();
            initiativeDTO.setTitle(initiative.getTitle());
            return initiativeDTO;
        }

        @Override
        public Initiative toEntity(InitiativeDTO initiativeDTO) {
            return null;
        }

        @Override
        public List<Initiative> toEntity(List<InitiativeDTO> dtoList) {
            return null;
        }

        @Override
        public List<InitiativeDTO> toDto(List<Initiative> entityList) {
            return null;
        }
    };

    private InitiativeServiceImpl impl;

    @Before
    public void setUp() throws Exception {
        impl = new InitiativeServiceImpl(initiativeRepository, initiativeMapper, null);
    }

    private Initiative create(String title) {
        Initiative initiative = new Initiative();
        initiative.setTitle(title);
        return initiative;
    }
}
