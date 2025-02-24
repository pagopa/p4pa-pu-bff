package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionView.StatusEnum;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DebtPositionControllerTest {

  @Mock
  private DebtPositionRetrieverService debtPositionRetrieverServiceMock;

  @InjectMocks
  private DebtPositionController debtPositionController;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    Authentication authentication = new UsernamePasswordAuthenticationToken("fakeUser", "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionViewsThenOk() {
    long organizationId = 1L;
    OffsetDateTime creationDateFrom = OffsetDateTime.now();
    OffsetDateTime creationDateTo = OffsetDateTime.now();
    String fiscalCode = "fiscalCode";
    Long debtPositionTypeOrgId = 2L;
    StatusEnum status = StatusEnum.REPORTED;

    PagedDebtPositionView expectedResult = podamFactory.manufacturePojo(PagedDebtPositionView.class);

    Mockito.when(debtPositionRetrieverServiceMock.getDebtPositionViews(
        Mockito.argThat(f ->
          f.getOrganizationId().equals(organizationId)
            && f.getCreationDateFrom().equals(creationDateFrom)
            && f.getCreationDateTo().equals(creationDateTo)
            && f.getFiscalCode().equals(fiscalCode)
            && f.getDebtPositionTypeOrgId().equals(debtPositionTypeOrgId)
            && f.getStatus().equals(status)
        ),
        Mockito.argThat(p->p.getPageNumber()==0 && p.getPageSize()==10 && p.getSort().isUnsorted()),
        Mockito.any(), Mockito.anyString()))
      .thenReturn(expectedResult);

    ResponseEntity<PagedDebtPositionView> response = debtPositionController.getDebtPositionViews(
      organizationId,
      creationDateFrom,
      creationDateTo,
      fiscalCode,
      debtPositionTypeOrgId,
      status,
      PageRequest.of(0,10));

    Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult,response.getBody());
  }
}
