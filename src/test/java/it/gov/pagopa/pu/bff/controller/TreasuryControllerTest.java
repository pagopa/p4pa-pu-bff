package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import it.gov.pagopa.pu.bff.service.treasury.TreasuryRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.TreasuryView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TreasuryControllerTest {

  @Mock
  private TreasuryRetrieverService treasuryRetrieverServiceMock;

  @InjectMocks
  private TreasuryController treasuryController;

  private UserInfo userInfo;

  @BeforeEach
  void setUp() {
    userInfo = new UserInfo();
    userInfo.setMappedExternalUserId("fakeExternalUser");
    Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo, "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void givenCorrectRequestWhenGetTreasuriesThenOk() {
    long organizationId = 1L;
    String iuv = "IUV123";
    String iuf = "IUF123";
    long billAmountCents = 1000L;
    LocalDate billDate = LocalDate.now().minusDays(10);
    String provisionalCode = "PROV123";
    String billCode = "BILL123";
    String pspLastName = "PSPLastName";
    LocalDate regionValueDate = LocalDate.now().minusDays(5);
    String documentCode = "DOC123";
    Pageable pageable = PageRequest.of(0, 10);

    TreasuryViewFiltersDTO filtersDTO = new TreasuryViewFiltersDTO(organizationId, iuv, iuf, billAmountCents, billDate, provisionalCode, billCode, pspLastName, regionValueDate, documentCode);

    PagedTreasuryView expectedResult = new PagedTreasuryView();
    expectedResult.setContent(List.of(TreasuryView.builder()
      .treasuryId("100")
      .billAmountCents(1000L)
      .billDate(billDate)
      .iuv(iuv)
      .build()));
    expectedResult.setSize(10L);
    expectedResult.setTotalElements(1L);
    expectedResult.setTotalPages(1L);
    expectedResult.setNumber(0L);

    Mockito.when(treasuryRetrieverServiceMock.getTreasuries(filtersDTO, pageable, userInfo, "fakeAccessToken"))
      .thenReturn(expectedResult);

    ResponseEntity<PagedTreasuryView> response = treasuryController.getTreasuries(organizationId, iuv, iuf, billAmountCents, billDate, provisionalCode, billCode, pspLastName, regionValueDate, documentCode, pageable);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

}
