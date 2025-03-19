package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentView;
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
  void givenCorrectRequestWhenGetInstallmentsThenOk() {
    long organizationId = 1L;
    String iuv = "IUV123";
    String fiscalCode = "FiscalCode123";
    long debtPositionTypeOrgId = 2L;
    Pageable pageable = PageRequest.of(0, 10);
    OffsetDateTime dueDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime dueDateTo = OffsetDateTime.now();

    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(dueDateFrom, dueDateTo);

    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO(organizationId, userInfo.getMappedExternalUserId(), paymentDateTimeFilter, iuv, fiscalCode, debtPositionTypeOrgId);

    PagedInstallmentView expectedResult = new PagedInstallmentView();
    expectedResult.setContent(List.of(InstallmentView.builder()
      .installmentId(100L)
      .paymentOptionId(200L)
      .iuv(iuv)
      .status(InstallmentView.StatusEnum.PAID)
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

    when(installmentRetrieverServiceMock.getInstallments(filtersDTO, pageable, userInfo, "fakeAccessToken"))
      .thenReturn(expectedResult);

    ResponseEntity<PagedInstallmentView> response = installmentController.getInstallments(organizationId, dueDateFrom, dueDateTo, iuv, fiscalCode, debtPositionTypeOrgId, pageable);

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
      Mockito.any(), Mockito.anyString())).thenReturn(expectedResult);

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
