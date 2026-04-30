package it.gov.pagopa.pu.bff.service.installment_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.registries.InstallmentRegistryService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelInstallmentRegistry;
import it.gov.pagopa.pu.registries.dto.generated.InstallmentRegistry;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class InstallmentRegistryRetrieverServiceImpl implements InstallmentRegistryRetrieverService {
  private final InstallmentRegistryService installmentRegistryService;
  private final DebtPositionRetrieverService debtPositionRetrieverService;

  public InstallmentRegistryRetrieverServiceImpl(InstallmentRegistryService installmentRegistryService, DebtPositionRetrieverService debtPositionRetrieverService) {
    this.installmentRegistryService = installmentRegistryService;
    this.debtPositionRetrieverService = debtPositionRetrieverService;
  }

  @Override
  public List<InstallmentRegistry> getInstallmentRegistries(Long organizationId, Long debtPositionId, String nav, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    debtPositionRetrieverService.validateOperator(debtPositionId, organizationId, loggedUser, accessToken);

    CollectionModelInstallmentRegistry collection = installmentRegistryService.getInstallmentRegistries(debtPositionId, nav, accessToken);
    if (collection == null || collection.getEmbedded() == null) {
      return Collections.emptyList();
    }
    return collection.getEmbedded().getInstallmentRegistries();
  }
}
