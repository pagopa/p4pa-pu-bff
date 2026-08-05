package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.PdndClientService;
import it.gov.pagopa.pu.bff.service.pdnd_client.PdndClientRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdndClientRetrieverServiceImplTest {

  private static final Long ORGANIZATION_ID = 123L;
  private static final String SUB_UNIT_CODE = "SUB_UNIT_001";
  private static final String ACCESS_TOKEN = "fakeAccessToken";

  private static final UserInfo USER_INFO = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @Mock
  private AuthorizationService authorizationServiceMock;

  @Mock
  private PdndClientService pdndClientServiceMock;

  @InjectMocks
  private PdndClientRetrieverServiceImpl pdndClientRetrieverService;

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      authorizationServiceMock,
      pdndClientServiceMock
    );
  }

  @Test
  void givenAuthorizedAdminWhenGetPdndClientsThenReturnClients() {
    List<PdndClientNoSecretDTO> expectedResult = List.of(TestUtils.getPodamFactory().manufacturePojo(PdndClientNoSecretDTO.class));

    doNothing().when(authorizationServiceMock).validateAdminRole(ORGANIZATION_ID, USER_INFO);

    when(pdndClientServiceMock.getPdndClientsByOrganizationIdAndSubUnitCode(ORGANIZATION_ID, SUB_UNIT_CODE, ACCESS_TOKEN))
      .thenReturn(expectedResult);

    List<PdndClientNoSecretDTO> result = pdndClientRetrieverService.getPdndClientsByOrganizationIdAndSubUnitCode(ORGANIZATION_ID, SUB_UNIT_CODE, USER_INFO, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }

  @Test
  void givenUnauthorizedUserWhenGetPdndClientsThenPropagateException() {
    RuntimeException expectedException =
      new RuntimeException("User is not an organization admin");

    doThrow(expectedException)
      .when(authorizationServiceMock)
      .validateAdminRole(ORGANIZATION_ID, USER_INFO);

    RuntimeException result =
      assertThrows(RuntimeException.class, () -> pdndClientRetrieverService.getPdndClientsByOrganizationIdAndSubUnitCode(ORGANIZATION_ID, SUB_UNIT_CODE, USER_INFO, ACCESS_TOKEN));

    assertSame(expectedException, result);
  }
}
