package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentStatus;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentView;
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

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstallmentControllerTest {

  @Mock
  private InstallmentRetrieverService installmentRetrieverServiceMock;

  @InjectMocks
  private InstallmentController installmentController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      installmentRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetInstallmentsThenOk() {
    long organizationId = 1L;
    String iuv = "IUV123";
    String fiscalCode = "FiscalCode123";
    long debtPositionTypeOrgId = 2L;
    Pageable pageable = PageRequest.of(0, 10);

    OffsetDateTime dueDateTimeFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime dueDateTimeTo = OffsetDateTime.now();

    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(dueDateTimeFrom, dueDateTimeTo);

    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO(organizationId, loggedUser.getMappedExternalUserId(), paymentDateTimeFilter, iuv, fiscalCode, debtPositionTypeOrgId);

    PagedInstallmentView expectedResult = new PagedInstallmentView();
    expectedResult.setContent(List.of(InstallmentView.builder()
      .installmentId(100L)
      .paymentOptionId(200L)
      .iuv(iuv)
      .status(InstallmentStatus.PAID)
      .dueDate(OffsetDateTime.now())
      .amountCents(1000L)
      .remittanceInformation("Remittance Info")
      .debtorFiscalCodeHash(new byte[]{1, 2, 3})
      .debtPositionTypeOrgDescription("Description")
      .build()));
    expectedResult.setSize(10L);
    expectedResult.setTotalElements(1L);
    expectedResult.setTotalPages(1L);
    expectedResult.setNumber(0L);

    when(installmentRetrieverServiceMock.getInstallments(filtersDTO, pageable, loggedUser, accessToken))
      .thenReturn(expectedResult);

    ResponseEntity<PagedInstallmentView> response = installmentController.getInstallments(organizationId, dueDateTimeFrom, dueDateTimeTo, iuv, fiscalCode, debtPositionTypeOrgId, pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetInstallmentDetailThenOk() {
    long organizationId = 1L;
    long installmentId = 2L;
    InstallmentDetailDTO expectedResult = new InstallmentDetailDTO();

    Mockito.when(installmentRetrieverServiceMock.getInstallmentDetail(Mockito.eq(organizationId), Mockito.eq(installmentId),
      Mockito.same(loggedUser), Mockito.same(accessToken))).thenReturn(expectedResult);

    ResponseEntity<InstallmentDetailDTO> response = installmentController.getInstallmentDetail(organizationId, installmentId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
    Mockito.verify(installmentRetrieverServiceMock).getInstallmentDetail(Mockito.eq(organizationId), Mockito.eq(installmentId), Mockito.any(), Mockito.anyString());
  }

  @Test
  void givenNoInstallmentWhenGetInstallmentDetailThenNotFound() {
    long organizationId = 1L;
    long installmentId = 2L;

    ResponseEntity<InstallmentDetailDTO> response = installmentController.getInstallmentDetail(organizationId, installmentId);

    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assertions.assertNull(response.getBody());
    Mockito.verify(installmentRetrieverServiceMock).getInstallmentDetail(Mockito.eq(organizationId), Mockito.eq(installmentId), Mockito.any(), Mockito.anyString());
  }

}
