package it.gov.pagopa.pu.bff.service.spontaneous_form;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.SpontaneousFormService;
import it.gov.pagopa.pu.bff.dto.generated.PagedSpontaneousForm;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagedSpontaneousFormMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PageMetadata;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelSpontaneousForm;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
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
    validateRetrievedSpontaneousForm(spontaneousForm, debtPositionTypeOrg.getOrganizationId(), debtPositionTypeOrg.getSpontaneousFormId());
    return spontaneousForm;
  }

  private static void validateRetrievedSpontaneousForm(SpontaneousForm spontaneousForm, Long organizationId, Long spontaneousFormId) {
    if (spontaneousForm == null){
      throw new ResourceNotFoundException("SpontaneousForm with id %d not found".formatted(spontaneousFormId));
    }

    if (!organizationId.equals(spontaneousForm.getOrganizationId())){
      throw new ConflictException(
        "OrganizationId %d does not match OrganizationId %d of SpontaneousForm %d"
          .formatted(
            organizationId,
            spontaneousForm.getOrganizationId(),
            spontaneousFormId
          ));
    }
  }

  @Override
  public PagedSpontaneousForm getPagedSpontaneousForms(Long organizationId, String code, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return pagedSpontaneousFormMapper.map(spontaneousFormService.findAllByOrganizationIdAndCode(organizationId, code, pageable, accessToken));
  }

  @Override
  public SpontaneousForm getSpontaneousFormDetail(Long organizationId, Long spontaneousFormId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    SpontaneousForm spontaneousForm = spontaneousFormService.getSpontaneousForm(spontaneousFormId, accessToken);
    validateRetrievedSpontaneousForm(spontaneousForm,organizationId,spontaneousFormId);
    return spontaneousForm;
  }

  @Override
  public SpontaneousForm createSpontaneousForm(Long organizationId, SpontaneousForm spontaneousForm, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    validateSpontaneousForm(organizationId, spontaneousForm, accessToken);
    return spontaneousFormService.createSpontaneousForm(spontaneousForm,accessToken);
  }

  private void validateSpontaneousForm(Long organizationId, SpontaneousForm spontaneousForm, String accessToken) {
    if(spontaneousForm.getSpontaneousFormId()!=null){
      throw new ValidationException("SpontaneousFormId must be null");
    }
    if(!organizationId.equals(
        spontaneousForm.getOrganizationId())){
      throw new ValidationException("The SpontaneousForm's organizationId "+ spontaneousForm.getOrganizationId()+
          " does not match the given organizationId "+ organizationId);
    }

    PagedModelSpontaneousForm pagedModelSpontaneousForm = spontaneousFormService.findAllByOrganizationIdAndCode(spontaneousForm.getOrganizationId(), spontaneousForm.getCode(), PageRequest.ofSize(1),
        accessToken);
    if(Optional.ofNullable(pagedModelSpontaneousForm)
        .map(PagedModelSpontaneousForm::getPage)
        .map(PageMetadata::getTotalElements)
        .filter(total -> total > 0)
        .isPresent()){
      throw new ConflictException("There is another SpontaneousForm with organizationId "+ spontaneousForm.getOrganizationId()+" and code "+ spontaneousForm.getCode());
    }
  }
}
