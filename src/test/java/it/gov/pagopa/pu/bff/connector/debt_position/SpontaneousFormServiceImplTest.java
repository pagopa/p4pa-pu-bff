package it.gov.pagopa.pu.bff.connector.debt_position;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.client.SpontaneousFormSearchClient;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class SpontaneousFormServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private SpontaneousFormSearchClient spontaneousFormSearchClient;

  private SpontaneousFormService service;

  @BeforeEach
  void setUp() {
    service = new SpontaneousFormServiceImpl(spontaneousFormSearchClient);
  }

  @Test
  void whenFindAllByOrganizationIdThenInvokeClient() {
    Long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";
    List<SpontaneousForm> expectedResult = podamFactory.manufacturePojo(List.class,SpontaneousForm.class);

    when(spontaneousFormSearchClient.findAllByOrganizationId(organizationId,accessToken))
      .thenReturn(expectedResult);

    List<SpontaneousForm> result = service.findAllByOrganizationId(organizationId, accessToken);

    assertSame(expectedResult, result);
  }
}