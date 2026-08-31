package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.client.generated.OrgSubUnitSearchControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.SubUnitType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitSearchClientTest {
  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private OrgSubUnitSearchControllerApi orgSubUnitSearchControllerApiMock;

  private OrgSubUnitSearchClient orgSubUnitSearchClient;

  @BeforeEach
  void setUp() {
    orgSubUnitSearchClient = new OrgSubUnitSearchClient(organizationApisHolderMock);
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(
      organizationApisHolderMock
    );
  }

  @Test
  void whenFindByOrganizationIdAndFiltersThenInvokeWithAccessToken() {
    String accessToken = "accessToken";
    Long organizationId = 1L;
    String operatorExternalUserId = "operatorExternalUserId";
    String subUnitCode = "subUnitCode";
    OrgSubUnitStatus status = OrgSubUnitStatus.ACTIVE;
    SubUnitType subUnitType = SubUnitType.UO;
    Pageable pageable = PageRequest.ofSize(10);

    PagedModelOrgSubUnit expectedResult = podamFactory.manufacturePojo(PagedModelOrgSubUnit.class);

    when(organizationApisHolderMock.getOrgSubUnitSearchControllerApi(accessToken))
      .thenReturn(orgSubUnitSearchControllerApiMock);
    when(orgSubUnitSearchControllerApiMock
      .crudOrgSubUnitFindByOrganizationIdAndFilters(organizationId, operatorExternalUserId, subUnitCode, status, subUnitType, 0, 10, Collections.emptyList())
    )
      .thenReturn(expectedResult);

    PagedModelOrgSubUnit result = orgSubUnitSearchClient.findByOrganizationIdAndFilters(
      organizationId,
      operatorExternalUserId,
      subUnitCode,
      status,
      subUnitType,
      pageable,
      accessToken
    );

    assertSame(expectedResult, result);
  }
}
