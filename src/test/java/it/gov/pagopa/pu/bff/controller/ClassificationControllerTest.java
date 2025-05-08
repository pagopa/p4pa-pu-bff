package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.classification.ClassificationRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationControllerTest {

  @Mock
  private ClassificationRetrieverService classificationRetrieverServiceMock;

  @InjectMocks
  private ClassificationController classificationController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(classificationRetrieverServiceMock);
  }

  @AfterEach
  void clearContext() {
    SecurityUtilsTest.clearSecurityContext();
  }


  @Test
  void testGetClassificationDetail() {
    Long organizationId = 1L;
    Long classificationId = 1L;
    ClassificationDetailViewDTO mockDetailView = new ClassificationDetailViewDTO();
    when(classificationRetrieverServiceMock.getClassificationDetail(
      organizationId, classificationId, loggedUser, accessToken))
      .thenReturn(mockDetailView);

    ResponseEntity<ClassificationDetailViewDTO> response = classificationController.getClassificationDetail(organizationId, classificationId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockDetailView, response.getBody());
    verify(classificationRetrieverServiceMock).getClassificationDetail(
      organizationId, classificationId, loggedUser, accessToken);
  }

  @Test
  void givenIncorrectRequestWhenGetClassificationDetailThenNotFound() {
    long organizationId = 1L;
    long classificationId = 999L;

    when(classificationRetrieverServiceMock.getClassificationDetail(organizationId, classificationId, loggedUser, accessToken))
      .thenReturn(null);

    ResponseEntity<ClassificationDetailViewDTO> response = classificationController.getClassificationDetail(organizationId, classificationId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assertions.assertNull(response.getBody());
  }
}

