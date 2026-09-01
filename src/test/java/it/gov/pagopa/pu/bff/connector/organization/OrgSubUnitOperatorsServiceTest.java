package it.gov.pagopa.pu.bff.connector.organization;

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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitOperatorsServiceTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private OrgSubUnitOperatorsSearchClient orgSubUnitOperatorsSearchClientMock;

  private OrgSubUnitOperatorsService service;

  @BeforeEach
  void setUp() {
    service = new OrgSubUnitOperatorsServiceImpl(orgSubUnitOperatorsSearchClientMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(orgSubUnitOperatorsSearchClientMock);
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
}
