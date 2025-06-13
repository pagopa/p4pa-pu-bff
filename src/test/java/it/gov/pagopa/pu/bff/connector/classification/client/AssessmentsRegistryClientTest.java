package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.controller.generated.AssessmentsRegistryApi;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentsRegistryClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private AssessmentsRegistryApi assessmentsRegistryApiMock;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private AssessmentsRegistryClient assessmentsRegistryClient;

  @BeforeEach
  void setUp() {
    assessmentsRegistryClient = new AssessmentsRegistryClient(classificationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      classificationApisHolderMock,assessmentsRegistryApiMock
    );
  }

  @Test
  void whenCreateAssessmentsRegistryThenInvokeWithAccessToken() {
    AssessmentsRegistry assessmentsRegistry = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    String accessToken = "ACCESSTOKEN";
    AssessmentsRegistry expectedResult = new AssessmentsRegistry();

    when(classificationApisHolderMock.getAssessmentsRegistryApi(accessToken))
      .thenReturn(assessmentsRegistryApiMock);
    when(assessmentsRegistryApiMock.createAssessmentsRegistry(
                    assessmentsRegistry))
      .thenReturn(expectedResult);

    AssessmentsRegistry result = assessmentsRegistryClient.createAssessmentsRegistry(assessmentsRegistry, accessToken);

    assertSame(expectedResult, result);
  }
}

