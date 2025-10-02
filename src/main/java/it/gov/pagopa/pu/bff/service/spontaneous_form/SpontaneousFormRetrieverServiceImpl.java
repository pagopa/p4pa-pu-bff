package it.gov.pagopa.pu.bff.service.spontaneous_form;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.SpontaneousFormService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SpontaneousFormRetrieverServiceImpl implements SpontaneousFormRetrieverService {
  private final SpontaneousFormService spontaneousFormService;

  public SpontaneousFormRetrieverServiceImpl(SpontaneousFormService spontaneousFormService) {
    this.spontaneousFormService = spontaneousFormService;
  }

  @Override
  public List<SpontaneousForm> getSpontaneousForms(Long organizationId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return spontaneousFormService.findAllByOrganizationId(organizationId, accessToken);
  }
}