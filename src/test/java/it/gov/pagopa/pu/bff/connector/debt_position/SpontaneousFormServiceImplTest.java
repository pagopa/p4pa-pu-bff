package it.gov.pagopa.pu.bff.connector.debt_position;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.client.SpontaneousFormEntityClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.SpontaneousFormSearchClient;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelSpontaneousForm;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
class SpontaneousFormServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private SpontaneousFormSearchClient spontaneousFormSearchClientMock;
  @Mock
  private SpontaneousFormEntityClient spontaneousFormEntityClientMock;

  private SpontaneousFormService service;

  @BeforeEach
  void setUp() {
    service = new SpontaneousFormServiceImpl(spontaneousFormSearchClientMock, spontaneousFormEntityClientMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(spontaneousFormSearchClientMock, spontaneousFormEntityClientMock);
  }

  @Test
  void whenFindAllByOrganizationIdThenInvokeClient() {
    Long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";
    List<SpontaneousForm> expectedResult = podamFactory.manufacturePojo(List.class,SpontaneousForm.class);

    when(spontaneousFormSearchClientMock.findAllByOrganizationId(organizationId,accessToken))
      .thenReturn(expectedResult);

    List<SpontaneousForm> result = service.findAllByOrganizationId(organizationId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetSpontaneousFormBySpontaneousFormIdThenInvokeClient() {
    String accessToken = "ACCESSTOKEN";
    Long spontaneousFormId = 1L;

    SpontaneousForm expectedResult = podamFactory.manufacturePojo(SpontaneousForm.class);

    when(spontaneousFormEntityClientMock.getSpontaneousForm(spontaneousFormId,accessToken))
      .thenReturn(expectedResult);

    SpontaneousForm result = service.getSpontaneousForm(spontaneousFormId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenFindAllByOrganizationIdAndCodeThenInvokeClient() {
    Long organizationId = 1L;
    String code = "code";
    Pageable pageable = PageRequest.ofSize(10);
    String accessToken = "ACCESSTOKEN";
    PagedModelSpontaneousForm expectedResult = podamFactory.manufacturePojo(PagedModelSpontaneousForm.class);

    when(spontaneousFormSearchClientMock.findAllByOrganizationIdAndCode(organizationId, code, pageable, accessToken))
        .thenReturn(expectedResult);

    PagedModelSpontaneousForm result = service.findAllByOrganizationIdAndCode(organizationId, code, pageable, accessToken);

    assertSame(expectedResult, result);
  }
}
