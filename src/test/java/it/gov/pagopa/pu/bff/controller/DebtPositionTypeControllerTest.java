package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.service.debtposition.DebtPositionTypeService;
import java.time.OffsetDateTime;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeControllerTest {

  @Mock
  private DebtPositionTypeService serviceMock;

  @InjectMocks
  private DebtPositionTypeController debtPositionTypeController;

  @BeforeEach
  void setUp() {
    Authentication authentication = new UsernamePasswordAuthenticationToken("fakeUser", "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionTypeWithCountThenOk() {
    long organizationId = 1L;
    PagedDebtPositionTypeWithCount expectedResult = new PagedDebtPositionTypeWithCount();
    expectedResult.setContent(List.of(DebtPositionTypeWithCount.builder()
      .debtPositionTypeId(1L)
      .code("code")
      .description("description")
      .updateDate(OffsetDateTime.now())
      .activeOrganizations(10)
      .build()));
    expectedResult.setSize(10L);
    expectedResult.setTotalElements(1L);
    expectedResult.setTotalPages(0L);
    expectedResult.setNumber(0L);

    Mockito.when(serviceMock.getDebtPositionTypeWithCount(Mockito.eq(organizationId),
        Mockito.argThat(p->p.getPageNumber()==0 && p.getPageSize()==10 && p.getSort().isUnsorted()),
        Mockito.any(), Mockito.anyString()))
      .thenReturn(expectedResult);

    ResponseEntity<PagedDebtPositionTypeWithCount> response = debtPositionTypeController.getDebtPositionTypeWithCount(organizationId,PageRequest.of(0,10));

    Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult,response.getBody());
  }
}

