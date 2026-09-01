package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.org_sub_unit_operators.OrgSubUnitOperatorsRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitOperatorsControllerTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private OrgSubUnitOperatorsRetrieverService orgSubUnitOperatorsRetrieverServiceMock;

  private static final String ACCESS_TOKEN = "accessToken";
  private final UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

  @InjectMocks
  private OrgSubUnitOperatorsController orgSubUnitOperatorsController;

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(ACCESS_TOKEN, loggedUser);
  }
  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(orgSubUnitOperatorsRetrieverServiceMock);
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetOrgSubUnitOperatorsThenOk() {
    Long organizationId = 1L;
    String subUnitCode = "subUnitCode";
    Pageable pageable = PageRequest.ofSize(10);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);

    when(
      orgSubUnitOperatorsRetrieverServiceMock.getOrgSubUnitOperators(
        organizationId,
        subUnitCode,
        pageable,
        loggedUser,
        ACCESS_TOKEN
      )
    ).thenReturn(expectedResult);

    ResponseEntity<PagedOrgSubUnitOperators> response = orgSubUnitOperatorsController.getOrgSubUnitOperators(
      organizationId,
      subUnitCode,
      pageable
    );

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }
}
