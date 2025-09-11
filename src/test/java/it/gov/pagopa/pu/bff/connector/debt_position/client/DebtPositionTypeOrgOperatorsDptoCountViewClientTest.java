package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperatorsDptoCountView;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperatorsDptoCountView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgOperatorsDptoCountViewClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApiMock;

  private DebtPositionTypeOrgOperatorsDptoCountViewClient debtPositionTypeOrgOperatorsDptoCountViewClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgOperatorsDptoCountViewClient = new DebtPositionTypeOrgOperatorsDptoCountViewClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock,
            debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApiMock);
  }

  @Test
  void whenFindByOrganizationIdAndOperatorExternalUserIdsThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    Set<String> operatorExternalUserIds = Set.of("op1","op2");
    CollectionModelDebtPositionTypeOrgOperatorsDptoCountView collectionModelDebtPositionTypeOrgOperators = podamFactory.manufacturePojo(CollectionModelDebtPositionTypeOrgOperatorsDptoCountView.class);

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApiMock);
    when(debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApiMock.crudDebtPositionTypeOrgOperatorsCountViewFindByOrganizationIdAndOperatorExternalUserIds(
            organizationId,operatorExternalUserIds))
      .thenReturn(collectionModelDebtPositionTypeOrgOperators);

    List<DebtPositionTypeOrgOperatorsDptoCountView> result = debtPositionTypeOrgOperatorsDptoCountViewClient.findByOrganizationIdAndOperatorExternalUserIds(organizationId, operatorExternalUserIds, accessToken);

    assertSame(collectionModelDebtPositionTypeOrgOperators.getEmbedded().getDebtPositionTypeOrgOperatorsDptoCountViews(), result);
  }

  @Test
  void givenNoDebtPositionTypeOrgOperatorsDptoCountViewWhenFindByOrganizationIdAndOperatorExternalUserIdsThenEmptyList() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    Set<String> operatorExternalUserIds = Set.of("op1","op2");
    CollectionModelDebtPositionTypeOrgOperatorsDptoCountView collectionModelDebtPositionTypeOrgOperators = podamFactory.manufacturePojo(CollectionModelDebtPositionTypeOrgOperatorsDptoCountView.class);
    collectionModelDebtPositionTypeOrgOperators.getEmbedded().setDebtPositionTypeOrgOperatorsDptoCountViews(Collections.emptyList());

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApiMock);
    when(debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApiMock.crudDebtPositionTypeOrgOperatorsCountViewFindByOrganizationIdAndOperatorExternalUserIds(
            organizationId,operatorExternalUserIds))
      .thenReturn(collectionModelDebtPositionTypeOrgOperators);

    List<DebtPositionTypeOrgOperatorsDptoCountView> result = debtPositionTypeOrgOperatorsDptoCountViewClient.findByOrganizationIdAndOperatorExternalUserIds(organizationId, operatorExternalUserIds, accessToken);

    assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void givenNoEmbeddedWhenFindByOrganizationIdAndOperatorExternalUserIdsThenInvokeWithEmptyList() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    Set<String> operatorExternalUserIds = Set.of("op1","op2");
    CollectionModelDebtPositionTypeOrgOperatorsDptoCountView collectionModelDebtPositionTypeOrgOperators = new CollectionModelDebtPositionTypeOrgOperatorsDptoCountView();
    collectionModelDebtPositionTypeOrgOperators.setEmbedded(null);

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApiMock);
    when(debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApiMock.crudDebtPositionTypeOrgOperatorsCountViewFindByOrganizationIdAndOperatorExternalUserIds(
            organizationId,operatorExternalUserIds))
      .thenReturn(collectionModelDebtPositionTypeOrgOperators);

    List<DebtPositionTypeOrgOperatorsDptoCountView> result = debtPositionTypeOrgOperatorsDptoCountViewClient.findByOrganizationIdAndOperatorExternalUserIds(organizationId, operatorExternalUserIds, accessToken);

    assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void givenNoCollectionModelDebtPositionTypeOrgOperatorsDptoCountViewWhenFindByOrganizationIdAndOperatorExternalUserIdsThenInvokeWithEmptyList() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    Set<String> operatorExternalUserIds = Set.of("operatorExternalUserId");

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApiMock);
    when(debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApiMock.crudDebtPositionTypeOrgOperatorsCountViewFindByOrganizationIdAndOperatorExternalUserIds(
            organizationId,operatorExternalUserIds))
      .thenReturn(null);

    List<DebtPositionTypeOrgOperatorsDptoCountView> result = debtPositionTypeOrgOperatorsDptoCountViewClient.findByOrganizationIdAndOperatorExternalUserIds(organizationId, operatorExternalUserIds, accessToken);

    assertTrue(CollectionUtils.isEmpty(result));
  }
}
