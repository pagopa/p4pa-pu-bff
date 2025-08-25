package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.treasury.TreasuryRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
import it.gov.pagopa.pu.classification.dto.generated.TreasuryView;
import org.junit.jupiter.api.AfterEach;
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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TreasuryControllerTest {

  @Mock
  private TreasuryRetrieverService treasuryRetrieverServiceMock;

  @InjectMocks
  private TreasuryController treasuryController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      treasuryRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetTreasuriesThenOk() {
    long organizationId = 1L;
    String iuv = "IUV123";
    String iuf = "IUF123";
    long billAmountCents = 1000L;
    LocalDate billDateFrom = LocalDate.now().minusDays(20);
    LocalDate billDateTo = LocalDate.now().minusDays(10);
    OffsetDateTime billDateTimeFrom = billDateFrom.atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime();
    OffsetDateTime billDateTimeTo = billDateTo.atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime();

    String provisionalCode = "PROV123";
    String provisionalAe = "PROVAE123";
    String billCode = "BILL123";
    String billYear = "2025";
    String pspLastName = "PSPLastName";
    LocalDate regionValueDateFrom = LocalDate.now().minusDays(10);
    LocalDate regionValueDateTo = LocalDate.now().minusDays(5);
    OffsetDateTime regionValueDateTimeFrom = regionValueDateFrom.atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime();
    OffsetDateTime regionValueDateTimeTo = regionValueDateTo.atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime();
    String documentCode = "DOC123";
    String documentYear = "2025";
    Pageable pageable = PageRequest.of(0, 10);

    LocalDateIntervalFilter billDateFilter = new LocalDateIntervalFilter(billDateFrom, billDateTo);
    LocalDateIntervalFilter regionValueDateFilter = new LocalDateIntervalFilter(regionValueDateFrom, regionValueDateTo);

    TreasuryViewFiltersDTO filtersDTO = new TreasuryViewFiltersDTO(
      organizationId, iuv, iuf, billAmountCents, billDateFilter, provisionalCode, provisionalAe, billCode, billYear, pspLastName, regionValueDateFilter, documentCode, documentYear
    );

    PagedTreasuryView expectedResult = new PagedTreasuryView();
    expectedResult.setContent(List.of(TreasuryView.builder()
      .treasuryId("100")
      .organizationId(organizationId)
      .billYear(billYear)
      .billCode(billCode)
      .billAmountCents(billAmountCents)
      .billDate(billDateTo)
      .iuv(iuv)
      .pspLastName(pspLastName)
      .build()));
    expectedResult.setSize(10L);
    expectedResult.setTotalElements(1L);
    expectedResult.setTotalPages(1L);
    expectedResult.setNumber(0L);

    Mockito.when(treasuryRetrieverServiceMock.getTreasuries(filtersDTO, pageable, loggedUser, accessToken))
      .thenReturn(expectedResult);

    ResponseEntity<PagedTreasuryView> response = treasuryController.getTreasuries(
      organizationId, iuv, iuf, billAmountCents, billDateTimeFrom, billDateTimeTo, provisionalCode, provisionalAe, billCode, billYear, pspLastName, regionValueDateTimeFrom, regionValueDateTimeTo, documentCode, documentYear, pageable
    );

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetTreasuryDetailThenOk() {
    long organizationId = 1L;
    String treasuryId = "TREASURY123";
    Treasury expectedTreasury = Treasury.builder()
      .treasuryId(treasuryId)
      .organizationId(organizationId)
      .billAmountCents(1000L)
      .billDate(LocalDate.now().minusDays(10))
      .billYear("2025")
      .billCode("123456789")
      .ingestionFlowFileId(100L)
      .pspLastName("PSPLastName")
      .orgBtCode("orgBtCode")
      .orgIstatCode("orgIstatCode")
      .build();

    Mockito.when(treasuryRetrieverServiceMock.getTreasuryDetail(Mockito.eq(organizationId), Mockito.eq(treasuryId), Mockito.same(loggedUser), Mockito.same(accessToken)))
      .thenReturn(expectedTreasury);

    ResponseEntity<Treasury> response = treasuryController.getTreasuryDetail(organizationId, treasuryId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedTreasury, response.getBody());
  }

  @Test
  void givenIncorrectRequestWhenGetTreasuryDetailThenNotFound() {
    long organizationId = 1L;
    String treasuryId = "INVALID_TREASURY_ID";

    Mockito.when(treasuryRetrieverServiceMock.getTreasuryDetail(organizationId, treasuryId, loggedUser, accessToken))
      .thenReturn(null);

    ResponseEntity<Treasury> response = treasuryController.getTreasuryDetail(organizationId, treasuryId);

    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assertions.assertNull(response.getBody());
  }

}
