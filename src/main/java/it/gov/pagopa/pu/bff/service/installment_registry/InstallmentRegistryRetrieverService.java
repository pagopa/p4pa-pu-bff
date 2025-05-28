package it.gov.pagopa.pu.bff.service.installment_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.registries.dto.generated.InstallmentRegistry;

import java.util.List;

public interface InstallmentRegistryRetrieverService {
  List<InstallmentRegistry> getInstallmentRegistries(Long organizationId, Long debtPositionId, UserInfo loggedUser, String accessToken);
}
