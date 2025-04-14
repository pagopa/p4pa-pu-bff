package it.gov.pagopa.pu.bff.connector.debt_position;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgWithCountClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgWithCount;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgServiceTest {

  @Mock
  private DebtPositionTypeOrgClient debtPositionTypeOrgClientMock;
  @Mock
  private DebtPositionTypeOrgWithCountClient debtPositionTypeOrgWithCountClientMock;

  private DebtPositionTypeOrgService service;

  @BeforeEach
  void setUp() {
    service = new DebtPositionTypeOrgServiceImpl(debtPositionTypeOrgClientMock, debtPositionTypeOrgWithCountClientMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgsThenInvokeClient() {
    Long organizationId = 1L;
    String operatorExternalUserId = "operatorExternalUserId";
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionTypeOrg expectedResult = new CollectionModelDebtPositionTypeOrg();

    when(debtPositionTypeOrgClientMock.getDebtPositionTypeOrgs(Mockito.same(organizationId), Mockito.same(operatorExternalUserId), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionTypeOrg result = service.getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetDebtPositionTypeOrgThenInvokeClient() {
    Long debtPositionTypeOrgId = 1L;
    String accessToken = "ACCESSTOKEN";
    DebtPositionTypeOrg expectedResult = new DebtPositionTypeOrg();

    when(debtPositionTypeOrgClientMock.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken))
      .thenReturn(expectedResult);

    DebtPositionTypeOrg result = service.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetDebtPositionTypeOrgWithCountThenInvokeClient() {
    Long organizationId = 1L;
    String code = "code";
    String description = "description";
    Pageable pageable = PageRequest.of(0, 10);
    String accessToken = "ACCESSTOKEN";
    PagedModelDebtPositionTypeOrgWithCount expectedResult = new PagedModelDebtPositionTypeOrgWithCount();

    when(debtPositionTypeOrgWithCountClientMock.getDebtPositionTypeOrgWithCount(Mockito.same(organizationId), Mockito.same(code), Mockito.same(description), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeOrgWithCount result = service.getDebtPositionTypeOrgWithCount(organizationId, code, description, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetDebtPositionTypeOrgCountByOrganizationIdThenInvokeClient() {
    Long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionTypeOrgCountByOrganizationId expectedResult = new CollectionModelDebtPositionTypeOrgCountByOrganizationId();

    when(debtPositionTypeOrgClientMock.getDebtPositionTypeOrgCountByOrganizationId(List.of(organizationId), accessToken)).thenReturn(expectedResult);

    CollectionModelDebtPositionTypeOrgCountByOrganizationId result = service.getDebtPositionTypeOrgCountByOrganizationId(List.of(organizationId), accessToken);

    assertSame(expectedResult, result);
  }

}
