package it.gov.pagopa.pu.bff.service.spontaneous_form;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.SpontaneousFormService;
import it.gov.pagopa.pu.bff.dto.generated.PagedSpontaneousForm;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagedSpontaneousFormMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SpontaneousFormRetrieverServiceImpl implements SpontaneousFormRetrieverService {
  private final SpontaneousFormService spontaneousFormService;
  private final PagedSpontaneousFormMapper pagedSpontaneousFormMapper;

  public SpontaneousFormRetrieverServiceImpl(SpontaneousFormService spontaneousFormService, PagedSpontaneousFormMapper pagedSpontaneousFormMapper) {
    this.spontaneousFormService = spontaneousFormService;
    this.pagedSpontaneousFormMapper = pagedSpontaneousFormMapper;
  }

  @Override
  public List<SpontaneousForm> getSpontaneousForms(Long organizationId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return spontaneousFormService.findAllByOrganizationId(organizationId, accessToken);
  }

  @Override
  public SpontaneousForm getSpontaneousFormAndValidate(Long spontaneousFormId, DebtPositionTypeOrg debtPositionTypeOrg, String accessToken) {
    SpontaneousForm spontaneousForm = spontaneousFormService.getSpontaneousForm(spontaneousFormId, accessToken);
    validateSpontaneousForm(spontaneousForm, debtPositionTypeOrg);
    return spontaneousForm;
  }

  private static void validateSpontaneousForm(SpontaneousForm spontaneousForm, DebtPositionTypeOrg debtPositionTypeOrg) {
    if (spontaneousForm == null){
      throw new ResourceNotFoundException("SpontaneousForm with id %d not found".formatted(debtPositionTypeOrg.getSpontaneousFormId()));
    }

    if (!debtPositionTypeOrg.getOrganizationId().equals(spontaneousForm.getOrganizationId())){
      throw new ConflictException(
        "OrganizationId %d of DebtPositionTypeOrg does not match OrganizationId %d of SpontaneousForm %d"
          .formatted(
            debtPositionTypeOrg.getOrganizationId(),
            spontaneousForm.getOrganizationId(),
            debtPositionTypeOrg.getSpontaneousFormId()
          ));
    }
  }

  @Override
  public PagedSpontaneousForm getPagedSpontaneousForms(Long organizationId, String code, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return pagedSpontaneousFormMapper.map(spontaneousFormService.findAllByOrganizationIdAndCode(organizationId, code, pageable, accessToken));
  }
}
