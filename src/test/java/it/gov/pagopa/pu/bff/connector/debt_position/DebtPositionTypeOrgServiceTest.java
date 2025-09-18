package it.gov.pagopa.pu.bff.connector.debt_position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgSearchClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgWithCountClient;
import it.gov.pagopa.pu.bff.dto.OperatorDetailsFiltersDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.SaveDebtPositionTypeOrgDTO;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgServiceTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private DebtPositionTypeOrgClient debtPositionTypeOrgClientMock;
  @Mock
  private DebtPositionTypeOrgWithCountClient debtPositionTypeOrgWithCountClientMock;
  @Mock
  private DebtPositionTypeOrgSearchClient debtPositionTypeOrgSearchClientMock;

  private DebtPositionTypeOrgService service;

  @BeforeEach
  void setUp() {
    service = new DebtPositionTypeOrgServiceImpl(debtPositionTypeOrgClientMock, debtPositionTypeOrgWithCountClientMock, debtPositionTypeOrgSearchClientMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgsThenInvokeClient() {
    Long organizationId = 1L;
    String operatorExternalUserId = "operatorExternalUserId";
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionTypeOrg expectedResult = new CollectionModelDebtPositionTypeOrg();

    when(debtPositionTypeOrgClientMock.getDebtPositionTypeOrgs(Mockito.same(organizationId), Mockito.same(operatorExternalUserId),  Mockito.same(true), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionTypeOrg result = service.getDebtPositionTypeOrgs(organizationId, true, operatorExternalUserId,  accessToken);

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

    when(debtPositionTypeOrgWithCountClientMock.getDebtPositionTypeOrgWithCount(Mockito.same(organizationId), Mockito.same(code), Mockito.same(description), Mockito.same(true), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeOrgWithCount result = service.getDebtPositionTypeOrgWithCount(organizationId, code, description, true, pageable, accessToken);

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

  @Test
  void whenDeleteDebtPositionTypeOrgThenInvokeClient() {
    Long debtPositionTypeOrgId = 1L;
    String accessToken = "ACCESSTOKEN";

    doNothing().when(debtPositionTypeOrgClientMock).deleteDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);

    service.deleteDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);

    verifyNoMoreInteractions(debtPositionTypeOrgClientMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgByDebtPositionTypeIdThenInvokeClient() {
    Long debtPositionTypeId = 1L;
    Pageable pageable = PageRequest.of(0, 10);
    String accessToken = "ACCESSTOKEN";
    PagedModelDebtPositionTypeOrg expectedResult = new PagedModelDebtPositionTypeOrg();

    when(debtPositionTypeOrgSearchClientMock.getDebtPositionTypeOrgByDebtPositionTypeId(Mockito.same(debtPositionTypeId), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeOrg result = service.getDebtPositionTypeOrgByDebtPositionTypeId(debtPositionTypeId, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenSaveDebtPositionTypeOrgThenInvokeClient() {
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(SaveDebtPositionTypeOrgDTO.class);
    String accessToken = "ACCESSTOKEN";
    DebtPositionTypeOrg expectedResult = new DebtPositionTypeOrg();

    when(debtPositionTypeOrgClientMock.saveDebtPositionTypeOrg(saveDebtPositionTypeOrgDTO,accessToken))
      .thenReturn(expectedResult);

    DebtPositionTypeOrg result = service.saveDebtPositionTypeOrg(saveDebtPositionTypeOrgDTO,accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenFindDebtPositionTypeOrgThenInvokeClient() {
    Long organizationId = 1L;
    String debtPositionTypeOrgCode="debtPositionTypeOrgCode";
    String mappedExternalUserId = "mappedExternalUserId";
    String accessToken = "ACCESSTOKEN";
    DebtPositionTypeOrg expectedResult = new DebtPositionTypeOrg();

    when(debtPositionTypeOrgSearchClientMock.findDebtPositionTypeOrg(organizationId,debtPositionTypeOrgCode,mappedExternalUserId,accessToken))
      .thenReturn(expectedResult);

    DebtPositionTypeOrg result = service.findDebtPositionTypeOrg(organizationId,debtPositionTypeOrgCode,mappedExternalUserId,accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenFindDebtPositionTypeOrgByOrganizationIdAndIudsThenInvokeClient() {
    Long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";
    Set<String> iuds = podamFactory.manufacturePojo(Set.class,String.class);
    List<DebtPositionTypeOrg> expectedResult = podamFactory.manufacturePojo(List.class,DebtPositionTypeOrg.class);

    when(debtPositionTypeOrgSearchClientMock.findDebtPositionTypeOrgByOrganizationIdAndIuds(organizationId,iuds,accessToken))
      .thenReturn(expectedResult);

    List<DebtPositionTypeOrg> result = service.findDebtPositionTypeOrgByOrganizationIdAndIuds(organizationId,iuds,accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenUpdateFlagActiveDebtPositionTypeOrgThenInvokeClient() {
    Long debtPositionTypeOrgId = 1L;
    String accessToken = "ACCESSTOKEN";

    doNothing().when(debtPositionTypeOrgClientMock).updateFlagActiveDebtPositionTypeOrg(debtPositionTypeOrgId, true, accessToken);

    service.updateFlagActiveDebtPositionTypeOrg(debtPositionTypeOrgId, true, accessToken);

    verifyNoMoreInteractions(debtPositionTypeOrgClientMock);
  }

  @Test
  void whenCountByOrgSilServiceIdThenInvokeClient() {
    Long orgSilServiceId = 123L;
    String accessToken = "ACCESS_TOKEN";
    Long expectedCount = 7L;

    when(debtPositionTypeOrgSearchClientMock.countByOrgSilServiceId(orgSilServiceId, accessToken))
      .thenReturn(expectedCount);

    Long result = service.countByOrgSilServiceId(orgSilServiceId, accessToken);

    assertEquals(expectedCount, result);
  }

  @Test
  void whenFindPagedDebtPositionTypeOrgThenInvokeClient() {
    Long organizationId = 1L;
    Long debtPositionTypeId = 1L;
    String accessToken = "ACCESS_TOKEN";
    String mappedExternalUserId = "mappedExternalUserId";
    String debtPositionTypeOrgCode = "code";
    String debtPositionTypeOrgDescription = "description";

    OperatorDetailsFiltersDTO operatorDetailsFiltersDTO = new OperatorDetailsFiltersDTO(organizationId, mappedExternalUserId, debtPositionTypeOrgCode, debtPositionTypeOrgDescription, debtPositionTypeId);
    PagedModelDebtPositionTypeOrg expectedResult = new PagedModelDebtPositionTypeOrg();

    when(debtPositionTypeOrgSearchClientMock.findPagedDebtPositionTypeOrg(operatorDetailsFiltersDTO, Pageable.ofSize(1), accessToken))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeOrg result = service.findPagedDebtPositionTypeOrg(operatorDetailsFiltersDTO, Pageable.ofSize(1), accessToken);

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void whenFindDebtPositionTypeOrgNotEnabledForOperatorThenInvokeClient() {
    Long organizationId = 1L;
    Long debtPositionTypeId = 2L;
    String accessToken = "ACCESS_TOKEN";
    String mappedExternalUserId = "mappedExternalUserId";
    String debtPositionTypeOrgCode = "code";
    String debtPositionTypeOrgDescription = "description";

    OperatorDetailsFiltersDTO operatorDetailsFiltersDTO = new OperatorDetailsFiltersDTO(organizationId, mappedExternalUserId, debtPositionTypeOrgCode, debtPositionTypeOrgDescription, debtPositionTypeId);
    PagedModelDebtPositionTypeOrg expectedResult = new PagedModelDebtPositionTypeOrg();

    when(debtPositionTypeOrgSearchClientMock.findDebtPositionTypeOrgNotEnabledForOperator(operatorDetailsFiltersDTO, Pageable.ofSize(1), accessToken))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeOrg result = service.findDebtPositionTypeOrgNotEnabledForOperator(operatorDetailsFiltersDTO, Pageable.ofSize(1), accessToken);

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

}

