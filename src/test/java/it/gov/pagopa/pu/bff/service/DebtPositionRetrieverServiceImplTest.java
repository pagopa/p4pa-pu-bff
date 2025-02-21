package it.gov.pagopa.pu.bff.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionService;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.bff.mapper.DebtPositionViewMapper;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionView.DebtPositionOriginEnum;
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
  private DebtPositionViewMapper debtPositionViewMapperMock;
  private DebtPositionRetrieverService debtPositionRetrieverService;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    debtPositionRetrieverService = new DebtPositionRetrieverServiceImpl(debtPositionServiceMock,debtPositionViewMapperMock);
  }

  @Test
  void givenValidUserWhenGetDebtPositionViewsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    PageRequest pageRequest = PageRequest.of(0, 10);
    List<String> debtPositionOrigins = List.of(DebtPositionOriginEnum.ORDINARY.toString(),DebtPositionOriginEnum.ORDINARY_SIL.toString(), DebtPositionOriginEnum.SPONTANEOUS.toString());

    DebtPositionViewFiltersDTO debtPositionViewFiltersDTO = podamFactory.manufacturePojo(
      DebtPositionViewFiltersDTO.class);
    PagedModelDebtPositionView pagedModelDebtPositionView = podamFactory.manufacturePojo(
      PagedModelDebtPositionView.class);
    PagedDebtPositionView expectedResult = podamFactory.manufacturePojo(
      PagedDebtPositionView.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(debtPositionViewFiltersDTO.getOrganizationId(), loggedUser))
        .thenReturn(true);

      Mockito.when(debtPositionServiceMock.getDebtPositionViews(debtPositionViewFiltersDTO,debtPositionOrigins,"mappedExternalUserId", pageRequest,
          accessToken))
        .thenReturn(pagedModelDebtPositionView);
      Mockito.when(debtPositionViewMapperMock.mapToPagedDebtPositionView(pagedModelDebtPositionView))
        .thenReturn(expectedResult);

      PagedDebtPositionView result = debtPositionRetrieverService.getDebtPositionViews(debtPositionViewFiltersDTO,pageRequest,loggedUser,
        accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(
        debtPositionViewFiltersDTO.getOrganizationId(), loggedUser));
      Mockito.verify(debtPositionServiceMock).getDebtPositionViews(debtPositionViewFiltersDTO,debtPositionOrigins,"mappedExternalUserId", pageRequest,
        accessToken);
      Mockito.verify(debtPositionViewMapperMock).mapToPagedDebtPositionView(pagedModelDebtPositionView);
    }
  }

  @Test
  void givenInvalidUserWhenGetDebtPositionViewsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    PageRequest pageRequest = PageRequest.of(0, 10);

    DebtPositionViewFiltersDTO debtPositionViewFiltersDTO = podamFactory.manufacturePojo(
      DebtPositionViewFiltersDTO.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(debtPositionViewFiltersDTO.getOrganizationId(), loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionRetrieverService.getDebtPositionViews(debtPositionViewFiltersDTO,
          pageRequest, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(debtPositionViewFiltersDTO.getOrganizationId(), loggedUser));
      Mockito.verifyNoInteractions(debtPositionServiceMock,debtPositionViewMapperMock);
    }
  }
}
