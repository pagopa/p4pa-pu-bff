package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.AssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class PagedAssessmentExtendedDTOMapperTest {

  @Mock
  private AssessmentExtendedDTOMapper assessmentExtendedDTOMapperMock;

  private PagedAssessmentExtendedDTOMapper pagedAssessmentExtendedDTOMapper;
  private final PodamFactory podamFactory = new PodamFactoryImpl();

  @BeforeEach
  void setUp() {
    pagedAssessmentExtendedDTOMapper = new PagedAssessmentExtendedDTOMapper(assessmentExtendedDTOMapperMock);
  }

  @AfterEach
  void afterEach(){
    Mockito.verifyNoMoreInteractions(assessmentExtendedDTOMapperMock);
  }

  @Test
  void givenPagedAssessmentsViewAndDebtPositionTypeOrgMapWhenMapThenReturnPagedAssessmentsExtendedDTO() {
    //given
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setDebtPositionTypeOrgCode("CODE01");

    AssessmentsExtendedDTO assessmentsExtendedDTO = AssessmentsExtendedDTO.builder()
      .assessmentId(assessments.getAssessmentId())
      .assessmentName(assessments.getAssessmentName())
      .creationDate(assessments.getCreationDate())
      .updateDate(assessments.getUpdateDate())
      .debtPositionTypeOrgCode(assessments.getDebtPositionTypeOrgCode())
      .flagManualGeneration(assessments.getFlagManualGeneration())
      .organizationId(assessments.getOrganizationId())
      .links(assessments.getLinks())
      .printed(assessments.getPrinted())
      .status(assessments.getStatus())
      .updateOperatorExternalId(assessments.getUpdateOperatorExternalId())
      .updateTraceId(assessments.getUpdateTraceId())
      .descriptionDebtPositionTypeOrgCode("description")
      .build();

    PagedAssessmentsView pagedAssessmentsView = PagedAssessmentsView.builder()
      .content(List.of(assessments))
      .size(1L)
      .number(1)
      .totalElements(1L)
      .totalPages(1L)
      .build();

    Map<String, String> debtPositionTypeOrgMap = Map.of(
      "CODE01", "description"
    );

    Mockito.when(assessmentExtendedDTOMapperMock.mapToAssessmentsExtendedDTO(assessments, "description")).thenReturn(assessmentsExtendedDTO);
    //when
    PagedAssessmentsExtendedDTO result = pagedAssessmentExtendedDTOMapper.map(pagedAssessmentsView, debtPositionTypeOrgMap);
    //then
    Assertions.assertNotNull(result);
    TestUtils.checkNotNullFields(result);
    TestUtils.checkNotNullFields(result.getContent().getFirst());
    TestUtils.reflectionEqualsByName(pagedAssessmentsView.getContent().getFirst(), result.getContent().getFirst());
    Assertions.assertEquals(1L, result.getSize());
    Assertions.assertEquals(1, result.getNumber());
    Assertions.assertEquals(1L, result.getTotalElements());
    Assertions.assertEquals(1L, result.getTotalPages());
  }

  @Test
  void givenNullPagedAssessmentsViewWhenMapThenReturnPagedAssessmentsExtendedDTO(){
    //given
    Map<String, String> debtPositionTypeOrgMap = Map.of(
      "CODE01", "description"
    );
    //when
    PagedAssessmentsExtendedDTO result = pagedAssessmentExtendedDTOMapper.map(null, debtPositionTypeOrgMap);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(new PagedAssessmentsExtendedDTO(), result);
  }

  @Test
  void givenEmptyPagedAssessmentsViewWhenMapThenReturnEmptyCollections(){
    //given
    Map<String, String> debtPositionTypeOrgMap = Map.of(
      "CODE01", "description"
    );
    //when
    PagedAssessmentsExtendedDTO result = pagedAssessmentExtendedDTOMapper.map(new PagedAssessmentsView(), debtPositionTypeOrgMap);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(Collections.emptyList(), result.getContent());
  }

  @Test
  void givenUnknownDebtPositionTypeOrgCodeWhenMapThenUseEmptyDescription() {
    // given
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setDebtPositionTypeOrgCode("UNKNOWN_CODE");

    AssessmentsExtendedDTO expectedDTO = AssessmentsExtendedDTO.builder()
      .assessmentId(assessments.getAssessmentId())
      .assessmentName(assessments.getAssessmentName())
      .creationDate(assessments.getCreationDate())
      .updateDate(assessments.getUpdateDate())
      .debtPositionTypeOrgCode(assessments.getDebtPositionTypeOrgCode())
      .flagManualGeneration(assessments.getFlagManualGeneration())
      .organizationId(assessments.getOrganizationId())
      .links(assessments.getLinks())
      .printed(assessments.getPrinted())
      .status(assessments.getStatus())
      .updateOperatorExternalId(assessments.getUpdateOperatorExternalId())
      .updateTraceId(assessments.getUpdateTraceId())
      .descriptionDebtPositionTypeOrgCode("")
      .build();

    PagedAssessmentsView pagedAssessmentsView = PagedAssessmentsView.builder()
      .content(List.of(assessments))
      .size(1L)
      .number(1)
      .totalElements(1L)
      .totalPages(1L)
      .build();

    Map<String, String> debtPositionTypeOrgMap = Map.of();

    Mockito.when(assessmentExtendedDTOMapperMock.mapToAssessmentsExtendedDTO(assessments, ""))
      .thenReturn(expectedDTO);

    // when
    PagedAssessmentsExtendedDTO result = pagedAssessmentExtendedDTOMapper.map(pagedAssessmentsView, debtPositionTypeOrgMap);

    // then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedDTO, result.getContent().getFirst());
  }

}
