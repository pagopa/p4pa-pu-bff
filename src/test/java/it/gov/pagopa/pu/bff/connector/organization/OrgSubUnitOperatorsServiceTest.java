package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitOperatorsClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitOperatorsSearchClient;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitOperatorsServiceTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private OrgSubUnitOperatorsSearchClient orgSubUnitOperatorsSearchClientMock;
  @Mock
  private OrgSubUnitOperatorsClient orgSubUnitOperatorsClientMock;

  private OrgSubUnitOperatorsService service;

  @BeforeEach
  void setUp() {
    service = new OrgSubUnitOperatorsServiceImpl(orgSubUnitOperatorsSearchClientMock, orgSubUnitOperatorsClientMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(orgSubUnitOperatorsSearchClientMock, orgSubUnitOperatorsClientMock);
  }

  @Test
  void whenFindByOrganizationIdAndSubUnitCodeThenInvokeClient() {
    Long organizationId = 1L;
    String subUnitCode = "subUnitCode";
    String accessToken = "accessToken";
    Pageable pageable = PageRequest.ofSize(10);

    PagedModelOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedModelOrgSubUnitOperators.class);

    when(orgSubUnitOperatorsSearchClientMock.findByOrganizationIdAndSubUnitCode(
      organizationId,
      subUnitCode,
      pageable,
      accessToken
    )).thenReturn(expectedResult);

    PagedModelOrgSubUnitOperators result = service.findByOrganizationIdAndSubUnitCode(
      organizationId,
      subUnitCode,
      pageable,
      accessToken
    );

    assertSame(expectedResult, result);
  }

  @Test
  void whenAddOrgSubUnitsToOperatorThenInvokeClient() {
    Long organizationId = 1L;
    String mappedExternalUserId = "mappedExternalUserId";
    List<String> orgSubUnitCodes = List.of("SUB_UNIT_1", "SUB_UNIT_2");
    String accessToken = "accessToken";

    service.addOrgSubUnitsToOperator(organizationId, mappedExternalUserId, orgSubUnitCodes, accessToken);

    verify(orgSubUnitOperatorsClientMock).addOrgSubUnitsToOperator(organizationId, mappedExternalUserId, orgSubUnitCodes, accessToken);
  }
}
