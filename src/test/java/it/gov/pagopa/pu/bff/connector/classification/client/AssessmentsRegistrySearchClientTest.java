package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.controller.generated.AssessmentsRegistrySearchControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentsRegistrySearchClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private AssessmentsRegistrySearchControllerApi assessmentsRegistrySearchControllerApiMock;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private AssessmentsRegistrySearchClient assessmentsRegistrySearchClient;

  @BeforeEach
  void setUp() {
    assessmentsRegistrySearchClient = new AssessmentsRegistrySearchClient(classificationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      classificationApisHolderMock,assessmentsRegistrySearchControllerApiMock
    );
  }

  @Test
  void whenFindAssessmentsRegistriesByFiltersThenInvokeWithAccessToken() {
    AssessmentsRegistryFiltersDTO filters = podamFactory.manufacturePojo(AssessmentsRegistryFiltersDTO.class);
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    PagedModelAssessmentsRegistry expectedResult = new PagedModelAssessmentsRegistry();

    when(classificationApisHolderMock.getAssessmentsRegistrySearchControllerApi(accessToken))
      .thenReturn(assessmentsRegistrySearchControllerApiMock);
    when(assessmentsRegistrySearchControllerApiMock.crudAssessmentsRegistriesFindAssessmentsRegistriesByFilters(
                    filters.getOrganizationId(), filters.getDebtPositionTypeOrgCodes(),filters.getSectionCode(),filters.getSectionDescription(),
            filters.getOfficeCode(),filters.getOfficeDescription(),filters.getAssessmentCode(),filters.getAssessmentDescription(),
            filters.getOperatingYear(),filters.getStatus(),0,10,sortList))
      .thenReturn(expectedResult);

    PagedModelAssessmentsRegistry result = assessmentsRegistrySearchClient.findAssessmentsRegistriesByFilters(
      filters, PageRequest.of(0,10,
        Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken);

    assertSame(expectedResult, result);
  }
}

