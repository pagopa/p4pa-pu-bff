package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.client.generated.OrgSubUnitOperatorsSearchControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitOperatorsSearchClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private OrgSubUnitOperatorsSearchControllerApi orgSubUnitOperatorsSearchControllerApiMock;

  private OrgSubUnitOperatorsSearchClient orgSubUnitOperatorsSearchClient;

  @BeforeEach
  void setUp() {
    orgSubUnitOperatorsSearchClient = new OrgSubUnitOperatorsSearchClient(organizationApisHolderMock);
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(
      organizationApisHolderMock
    );
  }

  @Test
  void whenFindByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdInThenInvokeWithAccessToken() {
    String accessToken = "accessToken";
    Long organizationId = 1L;
    String subUnitCode = "subUnitCode";
    Set<String> mappedExternalUserIds = Set.of("mappedExternalUserId1", "mappedExternalUserId2");
    Pageable pageable = PageRequest.ofSize(10);

    PagedModelOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedModelOrgSubUnitOperators.class);

    when(organizationApisHolderMock.getOrgSubUnitOperatorsSearchControllerApi(accessToken))
      .thenReturn(orgSubUnitOperatorsSearchControllerApiMock);

    when(orgSubUnitOperatorsSearchControllerApiMock
      .crudOrgSubUnitOperatorsFindByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(
        organizationId, subUnitCode, mappedExternalUserIds, 0, 10, Collections.emptyList()))
      .thenReturn(expectedResult);

    PagedModelOrgSubUnitOperators result =
      orgSubUnitOperatorsSearchClient.findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(organizationId, subUnitCode, mappedExternalUserIds, pageable, accessToken);

    assertSame(expectedResult, result);
  }
}
