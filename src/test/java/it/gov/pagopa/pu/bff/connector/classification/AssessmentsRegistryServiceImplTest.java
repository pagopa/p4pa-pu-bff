package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsRegistrySearchClient;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentsRegistryServiceImplTest {

  @Mock
  private AssessmentsRegistrySearchClient assessmentsRegistrySearchClientMock;
  private AssessmentsRegistryService assessmentsRegistryService;

  @BeforeEach
  void setUp() {
    assessmentsRegistryService = new AssessmentsRegistryServiceImpl(assessmentsRegistrySearchClientMock);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
            assessmentsRegistrySearchClientMock
    );
  }

  @Test
  void whenGetTreasuredClassificationThenInvokeClient() {
    AssessmentsRegistryFiltersDTO assessmentsRegistryFiltersDTO = new AssessmentsRegistryFiltersDTO();
    PageRequest pageable = PageRequest.of(0, 10);
    String accessToken = "ACCESSTOKEN";
    PagedModelAssessmentsRegistry expectedResult = new PagedModelAssessmentsRegistry();

    when(assessmentsRegistrySearchClientMock.findAssessmentsRegistriesByFilters(assessmentsRegistryFiltersDTO,pageable,accessToken))
      .thenReturn(expectedResult);

    PagedModelAssessmentsRegistry result = assessmentsRegistryService.findAssessmentsRegistriesByFilters(assessmentsRegistryFiltersDTO,pageable,accessToken);

    assertSame(expectedResult, result);
  }
}

