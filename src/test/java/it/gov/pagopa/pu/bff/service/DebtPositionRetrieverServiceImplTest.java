package it.gov.pagopa.pu.bff.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.bff.mapper.DebtPositionMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionViewMapper;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DebtPositionRetrieverServiceImplTest {
  @Mock
  private DebtPositionService debtPositionServiceMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private DebtPositionViewMapper debtPositionViewMapperMock;
  @Mock
  private DebtPositionMapper debtPositionMapperMock;
  private DebtPositionRetrieverService debtPositionRetrieverService;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    debtPositionRetrieverService = new DebtPositionRetrieverServiceImpl(debtPositionServiceMock,debtPositionTypeOrgServiceMock,debtPositionViewMapperMock,debtPositionMapperMock);
  }

  @Test
  void givenValidUserWhenGetDebtPositionViewsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    PageRequest pageRequest = PageRequest.of(0, 10);
    List<String> debtPositionOrigins = List.of(DebtPositionOrigin.ORDINARY.toString(),DebtPositionOrigin.ORDINARY_SIL.toString(), DebtPositionOrigin.SPONTANEOUS.toString());

    DebtPositionViewFiltersDTO debtPositionViewFiltersDTO = podamFactory.manufacturePojo(
      DebtPositionViewFiltersDTO.class);
    PagedModelDebtPositionView pagedModelDebtPositionView = podamFactory.manufacturePojo(
      PagedModelDebtPositionView.class);
    PagedDebtPositionView expectedResult = podamFactory.manufacturePojo(
      PagedDebtPositionView.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(debtPositionViewFiltersDTO.getOrganizationId(), loggedUser)).thenAnswer(a->null);

      Mockito.when(debtPositionServiceMock.getDebtPositionViews(debtPositionViewFiltersDTO,debtPositionOrigins,loggedUser.getMappedExternalUserId(), pageRequest,
          accessToken))
        .thenReturn(pagedModelDebtPositionView);
      Mockito.when(debtPositionViewMapperMock.mapToPagedDebtPositionView(pagedModelDebtPositionView))
        .thenReturn(expectedResult);

      PagedDebtPositionView result = debtPositionRetrieverService.getDebtPositionViews(debtPositionViewFiltersDTO,pageRequest,loggedUser,
        accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(
        debtPositionViewFiltersDTO.getOrganizationId(), loggedUser));
      Mockito.verify(debtPositionServiceMock).getDebtPositionViews(debtPositionViewFiltersDTO,debtPositionOrigins,loggedUser.getMappedExternalUserId(), pageRequest,
        accessToken);
      Mockito.verify(debtPositionViewMapperMock).mapToPagedDebtPositionView(pagedModelDebtPositionView);
    }
  }

  @Test
  void givenInvalidUserWhenGetDebtPositionViewsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    PageRequest pageRequest = PageRequest.of(0, 10);

    DebtPositionViewFiltersDTO debtPositionViewFiltersDTO = podamFactory.manufacturePojo(
      DebtPositionViewFiltersDTO.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(debtPositionViewFiltersDTO.getOrganizationId(), loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionRetrieverService.getDebtPositionViews(debtPositionViewFiltersDTO,
          pageRequest, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(debtPositionViewFiltersDTO.getOrganizationId(), loggedUser));
      Mockito.verifyNoInteractions(debtPositionServiceMock,debtPositionViewMapperMock);
    }
  }

  @Test
  void givenValidUserWhenGetDebtPositionDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long debtPositionId = 2L;

    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(
      DebtPositionDTO.class);
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(
      DebtPositionTypeOrg.class);
    DebtPositionDetailDTO expectedResult = podamFactory.manufacturePojo(
      DebtPositionDetailDTO.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a->null);

      Mockito.when(debtPositionServiceMock.getDebtPosition(debtPositionId, accessToken))
        .thenReturn(debtPositionDTO);
      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrg(debtPositionDTO.getDebtPositionTypeOrgId(),accessToken))
        .thenReturn(debtPositionTypeOrg);
      Mockito.when(debtPositionMapperMock.mapToDebtPositionDetailDTO(debtPositionDTO,debtPositionTypeOrg))
        .thenReturn(expectedResult);

      DebtPositionDetailDTO result = debtPositionRetrieverService.getDebtPositionDetail(debtPositionId,organizationId,loggedUser,accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(
        organizationId, loggedUser));
      Mockito.verify(debtPositionServiceMock).getDebtPosition(debtPositionId,accessToken);
      Mockito.verify(debtPositionTypeOrgServiceMock).getDebtPositionTypeOrg(debtPositionDTO.getDebtPositionTypeOrgId(),accessToken);
      Mockito.verify(debtPositionMapperMock).mapToDebtPositionDetailDTO(debtPositionDTO,debtPositionTypeOrg);
    }
  }

  @Test
  void givenValidUserAndNoDebtPositionWhenGetDebtPositionDetailThenReturnNull() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long debtPositionId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a->null);

      Mockito.when(debtPositionServiceMock.getDebtPosition(debtPositionId, accessToken))
        .thenReturn(null);

      DebtPositionDetailDTO result = debtPositionRetrieverService.getDebtPositionDetail(debtPositionId,organizationId,loggedUser,accessToken);

      assertNull(result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(
        organizationId, loggedUser));
      Mockito.verify(debtPositionServiceMock).getDebtPosition(debtPositionId,accessToken);
      Mockito.verifyNoInteractions(debtPositionTypeOrgServiceMock, debtPositionMapperMock);
    }
  }


  @Test
  void givenInvalidUserWhenGetDebtPositionDetailThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    Long organizationId = 1L;
    Long debtPositionId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionRetrieverService.getDebtPositionDetail(debtPositionId,organizationId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(debtPositionServiceMock,debtPositionTypeOrgServiceMock,debtPositionMapperMock);
    }
  }
}
