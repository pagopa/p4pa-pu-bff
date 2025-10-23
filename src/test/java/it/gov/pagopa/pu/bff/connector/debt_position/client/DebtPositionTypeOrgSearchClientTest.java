package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.OperatorDetailsFiltersDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgSearchClientTest {
  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApiMock;
  private DebtPositionTypeOrgSearchClient debtPositionTypeOrgSearchClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgSearchClient = new DebtPositionTypeOrgSearchClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgByDebtPositionTypeIdThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    PagedModelDebtPositionTypeOrg expectedResult = new PagedModelDebtPositionTypeOrg();

    long debtPositionTypeId = 1L;

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);

    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindByDebtPositionTypeId(
      debtPositionTypeId, 1, 1, Collections.emptyList()))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeOrg result = debtPositionTypeOrgSearchClient.getDebtPositionTypeOrgByDebtPositionTypeId(debtPositionTypeId,
      PageRequest.of(1, 1), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenExistingDebtPositionTypeOrgWhenFindDebtPositionTypeOrgThenInvokeWithAccessToken() {
    Long organizationId = 1L;
    String debtPositionTypeOrgCode="debtPositionTypeOrgCode";
    String mappedExternalUserId = "mappedExternalUserId";
    String accessToken = "ACCESS_TOKEN";
    DebtPositionTypeOrg expectedResult = new DebtPositionTypeOrg();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindDebtPositionTypeOrg(
      organizationId,debtPositionTypeOrgCode,mappedExternalUserId))
      .thenReturn(expectedResult);

    DebtPositionTypeOrg result = debtPositionTypeOrgSearchClient.findDebtPositionTypeOrg(organizationId,debtPositionTypeOrgCode,mappedExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNoExistingDebtPositionTypeOrgWhenFindDebtPositionTypeOrgThenNull() {
    Long organizationId = 1L;
    String debtPositionTypeOrgCode="debtPositionTypeOrgCode";
    String mappedExternalUserId = "mappedExternalUserId";
    String accessToken = "ACCESS_TOKEN";

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindDebtPositionTypeOrg(
      organizationId,debtPositionTypeOrgCode,mappedExternalUserId))
      .thenReturn(null);

    DebtPositionTypeOrg result = debtPositionTypeOrgSearchClient.findDebtPositionTypeOrg(organizationId,debtPositionTypeOrgCode,mappedExternalUserId, accessToken);

    assertNull(result);
  }

  @Test
  void whenFindDebtPositionTypeOrgByOrganizationIdAndIudsThenInvokeWithAccessToken() {
    Long organizationId = 1L;
    String accessToken = "ACCESS_TOKEN";
    Set<String> iuds = podamFactory.manufacturePojo(Set.class,String.class);
    CollectionModelDebtPositionTypeOrg collectionModelDebtPositionTypeOrg = podamFactory.manufacturePojo(CollectionModelDebtPositionTypeOrg.class);

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindDebtPositionTypeOrgByOrganizationIdAndIuds(
      organizationId,iuds))
      .thenReturn(collectionModelDebtPositionTypeOrg);

    List<DebtPositionTypeOrg> result = debtPositionTypeOrgSearchClient.findDebtPositionTypeOrgByOrganizationIdAndIuds(organizationId,iuds, accessToken);

    assertSame(collectionModelDebtPositionTypeOrg.getEmbedded().getDebtPositionTypeOrgs(), result);
  }

  @Test
  void givenNoDebtPositionTypeOrgsWhenFindDebtPositionTypeOrgByOrganizationIdAndIudsThenNull() {
    Long organizationId = 1L;
    String accessToken = "ACCESS_TOKEN";
    Set<String> iuds = podamFactory.manufacturePojo(Set.class,String.class);
    CollectionModelDebtPositionTypeOrg collectionModelDebtPositionTypeOrg = podamFactory.manufacturePojo(CollectionModelDebtPositionTypeOrg.class);
    collectionModelDebtPositionTypeOrg.getEmbedded().setDebtPositionTypeOrgs(null);

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
            .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindDebtPositionTypeOrgByOrganizationIdAndIuds(
            organizationId,iuds))
            .thenReturn(collectionModelDebtPositionTypeOrg);

    List<DebtPositionTypeOrg> result = debtPositionTypeOrgSearchClient.findDebtPositionTypeOrgByOrganizationIdAndIuds(organizationId,iuds, accessToken);

    assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void givenNoEmbeddedDebtPositionTypeOrgsWhenFindDebtPositionTypeOrgByOrganizationIdAndIudsThenNull() {
    Long organizationId = 1L;
    String accessToken = "ACCESS_TOKEN";
    Set<String> iuds = podamFactory.manufacturePojo(Set.class,String.class);
    CollectionModelDebtPositionTypeOrg collectionModelDebtPositionTypeOrg = podamFactory.manufacturePojo(CollectionModelDebtPositionTypeOrg.class);
    collectionModelDebtPositionTypeOrg.setEmbedded(null);

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
            .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindDebtPositionTypeOrgByOrganizationIdAndIuds(
            organizationId,iuds))
            .thenReturn(collectionModelDebtPositionTypeOrg);

    List<DebtPositionTypeOrg> result = debtPositionTypeOrgSearchClient.findDebtPositionTypeOrgByOrganizationIdAndIuds(organizationId,iuds, accessToken);

    assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void givenNoCollectionModelDebtPositionTypeOrgWhenFindDebtPositionTypeOrgByOrganizationIdAndIudsThenNull() {
    Long organizationId = 1L;
    String accessToken = "ACCESS_TOKEN";
    Set<String> iuds = podamFactory.manufacturePojo(Set.class,String.class);

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
            .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindDebtPositionTypeOrgByOrganizationIdAndIuds(
            organizationId,iuds))
            .thenReturn(null);

    List<DebtPositionTypeOrg> result = debtPositionTypeOrgSearchClient.findDebtPositionTypeOrgByOrganizationIdAndIuds(organizationId,iuds, accessToken);

    assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void givenOrgSilServiceIdWhenCountByOrgSilServiceIdThenInvokeWithAccessToken() {
    Long orgSilServiceId = 123L;
    String accessToken = "ACCESS_TOKEN";
    Long expectedCount = 5L;

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock
      .crudDebtPositionTypeOrgsCountByOrgSilServiceId(orgSilServiceId))
      .thenReturn(expectedCount);

    Long result = debtPositionTypeOrgSearchClient.countByOrgSilServiceId(orgSilServiceId, accessToken);

    assertEquals(expectedCount, result);
  }

  @Test
  void givenParametersWhenFindPagedDebtPositionTypeOrgThenInvokeWithAccessToken() {
    //given
    Long organizationId = 1L;
    Long debtPositionId = 1L;
    String mappedExternalUserId = "userId";
    String debtPositionTypeOrgCode = "code";
    String debtPositionTypeOrgDescription = "description";
    String accessToken = "ACCESS_TOKEN";

    OperatorDetailsFiltersDTO operatorDetailsFiltersDTO = new OperatorDetailsFiltersDTO(organizationId, mappedExternalUserId, debtPositionTypeOrgCode, debtPositionTypeOrgDescription, debtPositionId);

    PagedModelDebtPositionTypeOrg expectedResult = new PagedModelDebtPositionTypeOrg();
    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock
      .crudDebtPositionTypeOrgsFindPagedDebtPositionTypeOrg(organizationId, mappedExternalUserId, debtPositionTypeOrgCode, debtPositionTypeOrgDescription, debtPositionId, 0,1,List.of()))
      .thenReturn(expectedResult);

    //when
    PagedModelDebtPositionTypeOrg result = debtPositionTypeOrgSearchClient.findPagedDebtPositionTypeOrg(operatorDetailsFiltersDTO, Pageable.ofSize(1), accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenParametersWhenFindDebtPositionTypeOrgNotEnabledForOperatorThenInvokeWithAccessToken() {
    //given
    Long organizationId = 1L;
    Long debtPositionId = 2L;
    String mappedExternalUserId = "userId";
    String debtPositionTypeOrgCode = "code";
    String debtPositionTypeOrgDescription = "description";
    String accessToken = "ACCESS_TOKEN";

    OperatorDetailsFiltersDTO operatorDetailsFiltersDTO = new OperatorDetailsFiltersDTO(organizationId, mappedExternalUserId, debtPositionTypeOrgCode, debtPositionTypeOrgDescription, debtPositionId);

    PagedModelDebtPositionTypeOrg expectedResult = new PagedModelDebtPositionTypeOrg();
    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock
      .crudDebtPositionTypeOrgsFindDebtPositionTypeOrgNotEnabledForOperator(organizationId, mappedExternalUserId, debtPositionTypeOrgCode, debtPositionTypeOrgDescription, debtPositionId, 0,1,List.of()))
      .thenReturn(expectedResult);

    //when
    PagedModelDebtPositionTypeOrg result = debtPositionTypeOrgSearchClient.findDebtPositionTypeOrgNotEnabledForOperator(operatorDetailsFiltersDTO, Pageable.ofSize(1), accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenParametersWhenCountBySpontaneousFormIdThenInvokeWithAccessToken() {
    //given
    Long spontaneousFormId = 1L;
    String accessToken = "ACCESS_TOKEN";

    Long expectedResult = 2L;
    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock
      .crudDebtPositionTypeOrgsCountBySpontaneousFormId(spontaneousFormId))
      .thenReturn(expectedResult);

    //when
    Long result = debtPositionTypeOrgSearchClient.countBySpontaneousFormId(spontaneousFormId, accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedResult, result);
  }

}
