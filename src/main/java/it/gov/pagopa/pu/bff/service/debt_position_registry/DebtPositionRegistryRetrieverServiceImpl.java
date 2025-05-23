package it.gov.pagopa.pu.bff.service.debt_position_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.registries.DebtPositionRegistryService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelDebtPositionRegistry;
import it.gov.pagopa.pu.registries.dto.generated.DebtPositionRegistry;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DebtPositionRegistryRetrieverServiceImpl implements DebtPositionRegistryRetrieverService {
    private final DebtPositionRegistryService debtPositionRegistryService;

    public DebtPositionRegistryRetrieverServiceImpl(DebtPositionRegistryService debtPositionRegistryService) {
        this.debtPositionRegistryService = debtPositionRegistryService;
    }

    @Override
    public List<DebtPositionRegistry> getDebtPositionRegistry(Long organizationId, Long debtPositionId, UserInfo loggedUser, String accessToken) {
        AuthorizationService.validateUserForOrganizationId(organizationId,loggedUser);
        // TODO authorization depends on task https://pagopa.atlassian.net/browse/P4ADEV-2972

        CollectionModelDebtPositionRegistry collection = debtPositionRegistryService.findDebtPositionRegistries(debtPositionId, accessToken);
        if (collection == null || collection.getEmbedded() == null) {
            return Collections.emptyList();
        }
        return collection.getEmbedded().getDebtPositionRegistries();
    }
}
