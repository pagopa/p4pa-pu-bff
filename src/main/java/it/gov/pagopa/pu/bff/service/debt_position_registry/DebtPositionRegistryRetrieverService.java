package it.gov.pagopa.pu.bff.service.debt_position_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.registries.dto.generated.DebtPositionRegistry;

import java.util.List;

public interface DebtPositionRegistryRetrieverService {
    List<DebtPositionRegistry> getDebtPositionRegistry(Long organizationId, Long debtPositionId, UserInfo loggedUser, String accessToken);
}
