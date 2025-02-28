package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.InstallmentService;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.mapper.InstallmentDetailDTOMapper;
import it.gov.pagopa.pu.bff.mapper.InstallmentViewMapper;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverServiceImpl;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class InstallmentRetrieverServiceImplTest {

  @Mock
  private InstallmentService installmentServiceMock;

  @Mock
  private InstallmentViewMapper installmentViewMapperMock;

  @Mock
  private InstallmentDetailDTOMapper installmentDetailDTOMapperMock;

  private InstallmentRetrieverServiceImpl installmentRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    installmentRetrieverService = new InstallmentRetrieverServiceImpl(installmentViewMapperMock, installmentServiceMock, installmentDetailDTOMapperMock);
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(installmentServiceMock, installmentViewMapperMock);
  }


  @Test
  void givenValidUserWhenGetInstallmentsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelInstallmentView pagedModelInstallmentView = new PagedModelInstallmentView();
    PagedInstallmentView expectedPagedInstallmentView = new PagedInstallmentView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(filtersDTO.getOrganizationId(), loggedUser))
        .thenReturn(true);

      Mockito.when(installmentServiceMock.getInstallments(filtersDTO, pageable, accessToken))
        .thenReturn(pagedModelInstallmentView);

      Mockito.when(installmentViewMapperMock.mapToPagedInstallmentView(pagedModelInstallmentView))
        .thenReturn(expectedPagedInstallmentView);

      PagedInstallmentView result = installmentRetrieverService.getInstallments(filtersDTO, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedPagedInstallmentView, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenInvalidUserWhenGetInstallmentsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(filtersDTO.getOrganizationId(), loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      assertThrows(AuthorizationDeniedException.class, () ->
        installmentRetrieverService.getInstallments(filtersDTO, pageable, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenValidUserWhenGetInstallmentDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    Long installmentId = 2L;
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO installmentDetailDTO = new it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO();
    InstallmentDetailDTO expectedResult = new InstallmentDetailDTO();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser))
        .thenReturn(true);
      Mockito.when(installmentServiceMock.getInstallmentDetail(installmentId, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(installmentDetailDTO);
      Mockito.when(installmentDetailDTOMapperMock.mapToInstallmentDetailDTO(installmentDetailDTO))
        .thenReturn(expectedResult);

      InstallmentDetailDTO result = installmentRetrieverService.getInstallmentDetail(organizationId, installmentId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser));
      Mockito.verify(installmentServiceMock).getInstallmentDetail(installmentId, loggedUser.getMappedExternalUserId(), accessToken);
      Mockito.verify(installmentDetailDTOMapperMock).mapToInstallmentDetailDTO(installmentDetailDTO);
    }
  }

  @Test
  void givenInvalidUserWhenGetInstallmentDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    Long installmentId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        installmentRetrieverService.getInstallmentDetail(organizationId, installmentId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(installmentServiceMock, installmentDetailDTOMapperMock);
    }
  }

}
