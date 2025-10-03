package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.SpontaneousFormSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelSpontaneousForm;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class SpontaneousFormSearchClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private SpontaneousFormSearchControllerApi spontaneousFormSearchControllerApiMock;

  private SpontaneousFormSearchClient spontaneousFormSearchClient;

  @BeforeEach
  void setUp() {
    spontaneousFormSearchClient = new SpontaneousFormSearchClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      debtPositionApisHolderMock,
      spontaneousFormSearchControllerApiMock
    );
  }

  @Test
  void whenFindAllByOrganizationIdThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    CollectionModelSpontaneousForm collectionModelSpontaneousForm = podamFactory.manufacturePojo(CollectionModelSpontaneousForm.class);

    when(debtPositionApisHolderMock.getSpontaneousFormSearchControllerApi(accessToken))
        .thenReturn(spontaneousFormSearchControllerApiMock);
    when(spontaneousFormSearchControllerApiMock.crudSpontaneousFormsFindAllByOrganizationId(
        organizationId))
        .thenReturn(collectionModelSpontaneousForm);

    List<SpontaneousForm> result = spontaneousFormSearchClient.findAllByOrganizationId(organizationId, accessToken);

    assertSame(collectionModelSpontaneousForm.getEmbedded().getSpontaneousForms(), result);
  }

  @Test
  void givenNoSpontaneousFormsWhenFindAllByOrganizationIdThenEmptyList() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    CollectionModelSpontaneousForm collectionModelSpontaneousForm = podamFactory.manufacturePojo(CollectionModelSpontaneousForm.class);
    collectionModelSpontaneousForm.getEmbedded().setSpontaneousForms(Collections.emptyList());

    when(debtPositionApisHolderMock.getSpontaneousFormSearchControllerApi(accessToken))
        .thenReturn(spontaneousFormSearchControllerApiMock);
    when(spontaneousFormSearchControllerApiMock.crudSpontaneousFormsFindAllByOrganizationId(
        organizationId))
        .thenReturn(collectionModelSpontaneousForm);

    List<SpontaneousForm> result = spontaneousFormSearchClient.findAllByOrganizationId(organizationId, accessToken);

    assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void givenNoEmbeddedWhenFindAllByOrganizationIdThenEmptyList() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;

    CollectionModelSpontaneousForm collectionModelSpontaneousForm = podamFactory.manufacturePojo(CollectionModelSpontaneousForm.class);
    collectionModelSpontaneousForm.setEmbedded(null);

    when(debtPositionApisHolderMock.getSpontaneousFormSearchControllerApi(accessToken))
        .thenReturn(spontaneousFormSearchControllerApiMock);
    when(spontaneousFormSearchControllerApiMock.crudSpontaneousFormsFindAllByOrganizationId(
        organizationId))
        .thenReturn(collectionModelSpontaneousForm);

    List<SpontaneousForm> result = spontaneousFormSearchClient.findAllByOrganizationId(organizationId, accessToken);

    assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void givenNoCollectionModelSpontaneousFormWhenFindAllByOrganizationIdThenEmptyList() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;

    when(debtPositionApisHolderMock.getSpontaneousFormSearchControllerApi(accessToken))
        .thenReturn(spontaneousFormSearchControllerApiMock);
    when(spontaneousFormSearchControllerApiMock.crudSpontaneousFormsFindAllByOrganizationId(
        organizationId))
        .thenReturn(null);

    List<SpontaneousForm> result = spontaneousFormSearchClient.findAllByOrganizationId(organizationId, accessToken);

    assertTrue(CollectionUtils.isEmpty(result));
  }
}