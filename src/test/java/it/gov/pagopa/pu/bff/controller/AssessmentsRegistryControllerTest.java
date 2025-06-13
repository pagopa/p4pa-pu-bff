package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRegistryDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.assessments_registry.AssessmentsRegistryRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistryStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AssessmentsRegistryControllerTest {

  @Mock
  private AssessmentsRegistryRetrieverService assessmentsRegistryRetrieverServiceMock;

  @InjectMocks
  private AssessmentsRegistryController assessmentsRegistryController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      assessmentsRegistryRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext() {
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void whenGetAssessmentsRegistriesThenOk() {
    long organizationId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    String sectionCode = "sectionCode";
    String sectionDescription = "sectionDescription";
    String officeCode = "officeCode";
    String officeDescription = "officeDescription";
    String assessmentCode = "assessmentCode";
    String assessmentDescription = "assessmentDescription";
    String operatingYear = "operatingYear";
    AssessmentsRegistryStatus status = AssessmentsRegistryStatus.ACTIVE;
    AssessmentsRegistryFiltersDTO filters = AssessmentsRegistryFiltersDTO.builder()
      .organizationId(organizationId)
      .sectionCode(sectionCode)
      .sectionDescription(sectionDescription)
      .officeCode(officeCode)
      .officeDescription(officeDescription)
      .assessmentCode(assessmentCode)
      .assessmentDescription(assessmentDescription)
      .operatingYear(operatingYear)
      .status(status)
      .build();

    PagedAssessmentsRegistry expectedResult = new PagedAssessmentsRegistry();

    Mockito.when(assessmentsRegistryRetrieverServiceMock.getAssessmentsRegistries(
      Mockito.eq(filters), Mockito.eq(debtPositionTypeOrgCode),
      Mockito.argThat(p -> p.getPageNumber() == 0 && p.getPageSize() == 10 && p.getSort().isUnsorted()),
      Mockito.eq(loggedUser), Mockito.eq(accessToken)
    )).thenReturn(expectedResult);

    ResponseEntity<PagedAssessmentsRegistry> response = assessmentsRegistryController.getAssessmentsRegistries(
      organizationId, debtPositionTypeOrgCode, sectionCode, sectionDescription, officeCode, officeDescription,
      assessmentCode, assessmentDescription, operatingYear, status, PageRequest.of(0, 10));

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetAssessmentsRegistryThenOk() {
    long organizationId = 1L;
    long assessmentRegistryId = 100L;

    AssessmentsRegistryDTO expectedDTO = new AssessmentsRegistryDTO();
    expectedDTO.setAssessmentRegistryId(assessmentRegistryId);
    expectedDTO.setOrganizationId(organizationId);

    Mockito.when(assessmentsRegistryRetrieverServiceMock.getAssessmentsRegistry(
        Mockito.eq(organizationId),
        Mockito.eq(assessmentRegistryId),
        Mockito.same(loggedUser),
        Mockito.same(accessToken)))
      .thenReturn(expectedDTO);

    ResponseEntity<AssessmentsRegistryDTO> response = assessmentsRegistryController.getAssessmentsRegistry(organizationId, assessmentRegistryId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedDTO, response.getBody());
  }

  @Test
  void givenIncorrectRequestWhenGetAssessmentsRegistryThenNotFound() {
    long organizationId = 1L;
    long assessmentRegistryId = 999L;

    Mockito.when(assessmentsRegistryRetrieverServiceMock.getAssessmentsRegistry(
        organizationId, assessmentRegistryId, loggedUser, accessToken))
      .thenReturn(null);

    ResponseEntity<AssessmentsRegistryDTO> response = assessmentsRegistryController.getAssessmentsRegistry(organizationId, assessmentRegistryId);

    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assertions.assertNull(response.getBody());
  }
}


