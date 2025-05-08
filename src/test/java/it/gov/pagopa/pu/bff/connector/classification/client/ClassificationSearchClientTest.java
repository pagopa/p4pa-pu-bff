package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.classification.controller.generated.ClassificationsApi;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationSearchClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private ClassificationsApi classificationsApiMock;
  private ClassificationSearchClient classificationSearchClient;

  @BeforeEach
  void setUp() {
    classificationSearchClient = new ClassificationSearchClient(classificationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(classificationApisHolderMock, classificationsApiMock);
  }

  @Test
  void whenGetClassificationDetailThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    Long classificationId = 123L;
    ClassificationDetailViewDTO expectedClassificationDetail = new ClassificationDetailViewDTO();

    when(classificationApisHolderMock.getClassificationsApi(accessToken))
      .thenReturn(classificationsApiMock);

    when(classificationsApiMock.getClassificationDetail(organizationId, classificationId))
      .thenReturn(expectedClassificationDetail);

    ClassificationDetailViewDTO result = classificationSearchClient.getClassificationDetail(organizationId, classificationId, accessToken);

    assertSame(expectedClassificationDetail, result);
  }

  @Test
  void whenGetClassificationDetailNotFoundThenReturnNull() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    Long classificationId = 123L;

    when(classificationApisHolderMock.getClassificationsApi(accessToken))
      .thenReturn(classificationsApiMock);

    when(classificationsApiMock.getClassificationDetail(organizationId, classificationId))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    ClassificationDetailViewDTO result = classificationSearchClient.getClassificationDetail(organizationId, classificationId, accessToken);

    assertNull(result);
    verify(classificationsApiMock).getClassificationDetail(organizationId, classificationId);
  }
}

