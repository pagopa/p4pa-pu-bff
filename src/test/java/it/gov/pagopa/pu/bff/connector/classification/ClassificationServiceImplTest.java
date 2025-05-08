package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.ClassificationSearchClient;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationServiceImplTest {

  @Mock
  private ClassificationSearchClient classificationSearchClientMock;
  private ClassificationServiceImpl classificationService;

  @BeforeEach
  void setUp() {
    classificationService = new ClassificationServiceImpl(classificationSearchClientMock);
  }

  @Test
  void whenGetClassificationDetailThenInvokeClient() {
    Long organizationId = 1L;
    Long classificationId = 123L;
    String accessToken = "ACCESSTOKEN";
    ClassificationDetailViewDTO expectedClassificationDetail = new ClassificationDetailViewDTO();

    when(classificationSearchClientMock.getClassificationDetail(Mockito.same(organizationId), Mockito.same(classificationId), Mockito.same(accessToken)))
      .thenReturn(expectedClassificationDetail);

    ClassificationDetailViewDTO result = classificationService.getClassificationDetail(organizationId, classificationId, accessToken);

    assertSame(expectedClassificationDetail, result);
  }
}

