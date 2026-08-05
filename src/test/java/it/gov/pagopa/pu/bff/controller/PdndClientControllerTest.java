package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.pdnd_client.PdndClientRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdndClientControllerTest {

  @Mock
  private PdndClientRetrieverService pdndClientRetrieverServiceMock;

  @InjectMocks
  private PdndClientController controller;

  private static final Long ORGANIZATION_ID = 123L;
  private static final String ORG_SUB_UNIT_CODE = "SUB_UNIT_001";
  private static final String ACCESS_TOKEN = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(ACCESS_TOKEN, loggedUser);
  }

  @AfterEach
  void tearDown() {
    Mockito.verifyNoMoreInteractions(pdndClientRetrieverServiceMock);
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenOrganizationIdAndOrgSubUnitCodeWhenGetPdndClientsThenReturnClients() {
    PdndClientNoSecretDTO expectedClient = TestUtils.getPodamFactory().manufacturePojo(PdndClientNoSecretDTO.class);

    List<PdndClientNoSecretDTO> expectedClients = List.of(expectedClient);

    when(pdndClientRetrieverServiceMock.getPdndClientsByOrganizationIdAndSubUnitCode(ORGANIZATION_ID, ORG_SUB_UNIT_CODE, loggedUser, ACCESS_TOKEN))
      .thenReturn(expectedClients);

    ResponseEntity<List<PdndClientNoSecretDTO>> result = controller.getPdndClientsByOrgSubUnitCode(ORGANIZATION_ID, ORG_SUB_UNIT_CODE);

    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertSame(expectedClients, result.getBody());
  }
}
