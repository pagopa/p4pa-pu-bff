package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.AssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class AssessmentExtendedDTOMapperTest {

    private final PodamFactory podamFactory = new PodamFactoryImpl();

    private final AssessmentExtendedDTOMapper mapper = Mappers.getMapper(AssessmentExtendedDTOMapper.class);

    @Test
    void givenAssessmentsListAndMap_whenMapContentWithDescriptions_thenReturnMappedList() {
        // given
        Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
        assessments.setDebtPositionTypeOrgCode("CODE01");

        Map<String, String> descriptionMap = Map.of("CODE01", "Test Description");

        // when
        List<AssessmentsExtendedDTO> result = mapper.mapContentWithDescriptions(List.of(assessments), descriptionMap);

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        AssessmentsExtendedDTO dto = result.getFirst();
        Assertions.assertEquals("Test Description", dto.getDescriptionDebtPositionTypeOrgCode());
        Assertions.assertEquals(assessments.getAssessmentId(), dto.getAssessmentId());
        Assertions.assertEquals("", dto.getName());
        Assertions.assertEquals("", dto.getFamilyName());
        TestUtils.checkNotNullFields(dto);
    }

    @Test
    void givenPagedAssessmentsViewAndMap_whenMapToPagedAssessmentsExtendedDTO_thenReturnDTO() {
        // given
        Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
        assessments.setDebtPositionTypeOrgCode("CODE01");

        PagedAssessmentsView pagedView = PagedAssessmentsView.builder()
                .content(List.of(assessments))
                .size(10L)
                .number(1)
                .totalElements(1L)
                .totalPages(1L)
                .build();

        Map<String, String> descriptionMap = Map.of("CODE01", "Test Description");

        // when
        PagedAssessmentsExtendedDTO result = mapper.mapToPagedAssessmentsExtendedDTO(pagedView, descriptionMap);

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals("Test Description", result.getContent().getFirst().getDescriptionDebtPositionTypeOrgCode());
        Assertions.assertEquals(10L, result.getSize());
        Assertions.assertEquals(1, result.getNumber());
        Assertions.assertEquals(1L, result.getTotalElements());
        Assertions.assertEquals(1L, result.getTotalPages());
        TestUtils.checkNotNullFields(result);
    }

    @Test
    void givenNullContent_whenMapContentWithDescriptions_thenReturnEmptyList() {
        // when
        List<AssessmentsExtendedDTO> result = mapper.mapContentWithDescriptions(null, Map.of());

        // then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }
}
