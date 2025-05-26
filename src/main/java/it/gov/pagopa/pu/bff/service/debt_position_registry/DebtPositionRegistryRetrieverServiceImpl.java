package it.gov.pagopa.pu.bff.service.debt_position_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.registries.DebtPositionRegistryService;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelDebtPositionRegistry;
import it.gov.pagopa.pu.registries.dto.generated.DebtPositionRegistry;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DebtPositionRegistryRetrieverServiceImpl implements DebtPositionRegistryRetrieverService {
    private final DebtPositionRegistryService debtPositionRegistryService;
    private final DebtPositionRetrieverService debtPositionRetrieverService;

    public DebtPositionRegistryRetrieverServiceImpl(DebtPositionRegistryService debtPositionRegistryService,
                                                    DebtPositionRetrieverService debtPositionRetrieverService) {
        this.debtPositionRegistryService = debtPositionRegistryService;
      this.debtPositionRetrieverService = debtPositionRetrieverService;
    }

    @Override
    public List<DebtPositionRegistry> getDebtPositionRegistry(Long organizationId, Long debtPositionId, UserInfo loggedUser, String accessToken) {
        debtPositionRetrieverService.validateOperator(debtPositionId, organizationId, loggedUser, accessToken);

        CollectionModelDebtPositionRegistry collection = debtPositionRegistryService.findDebtPositionRegistries(debtPositionId, accessToken);
        if (collection == null || collection.getEmbedded() == null) {
            return Collections.emptyList();
        }
        return collection.getEmbedded().getDebtPositionRegistries();
    }
}
