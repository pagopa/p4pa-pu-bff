package it.gov.pagopa.pu.bff.service.spontaneous_form;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.debt_position.SpontaneousFormService;
import it.gov.pagopa.pu.bff.dto.generated.PagedSpontaneousForm;
import it.gov.pagopa.pu.bff.dto.generated.SpontaneousFormDetailDTO;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagedSpontaneousFormMapper;
import it.gov.pagopa.pu.bff.mapper.SpontaneousFormDetailDTOMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PageMetadata;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelSpontaneousForm;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.gov.pagopa.pu.bff.util.Utilities.checkImmutableField;

@Service
public class SpontaneousFormRetrieverServiceImpl implements SpontaneousFormRetrieverService {
  private final SpontaneousFormService spontaneousFormService;
  private final PagedSpontaneousFormMapper pagedSpontaneousFormMapper;
  private final AuthorizationService authorizationService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final SpontaneousFormDetailDTOMapper spontaneousFormDetailDTOMapper;

  public SpontaneousFormRetrieverServiceImpl(SpontaneousFormService spontaneousFormService, PagedSpontaneousFormMapper pagedSpontaneousFormMapper,
                                             AuthorizationService authorizationService, DebtPositionTypeOrgService debtPositionTypeOrgService, SpontaneousFormDetailDTOMapper spontaneousFormDetailDTOMapper) {
    this.spontaneousFormService = spontaneousFormService;
    this.pagedSpontaneousFormMapper = pagedSpontaneousFormMapper;
    this.authorizationService = authorizationService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.spontaneousFormDetailDTOMapper = spontaneousFormDetailDTOMapper;
  }

  @Override
  public List<SpontaneousForm> getSpontaneousForms(Long organizationId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return spontaneousFormService.findAllByOrganizationId(organizationId, accessToken);
  }

  @Override
  public SpontaneousForm getSpontaneousFormAndValidate(Long spontaneousFormId, DebtPositionTypeOrg debtPositionTypeOrg, String accessToken) {
    return getSpontaneousFormAndValidate(spontaneousFormId, debtPositionTypeOrg.getOrganizationId(), accessToken);
  }

  private SpontaneousForm getSpontaneousFormAndValidate(Long spontaneousFormId, Long organizationId, String accessToken) {
    if(spontaneousFormId==null){
      throw new ValidationException("SpontaneousFormId must not be null");
    }
    SpontaneousForm spontaneousForm = spontaneousFormService.getSpontaneousForm(spontaneousFormId, accessToken);
    validateRetrievedSpontaneousForm(spontaneousForm, organizationId, spontaneousFormId);
    return spontaneousForm;
  }

  private static void validateRetrievedSpontaneousForm(SpontaneousForm spontaneousForm, Long organizationId, Long spontaneousFormId) {
    if (spontaneousForm == null){
      throw new ResourceNotFoundException("SPONTANEOUS_FORM_NOT_FOUND", "SpontaneousForm with id %d not found".formatted(spontaneousFormId));
    }

    if (!organizationId.equals(spontaneousForm.getOrganizationId())){
      throw new ConflictException("INVALID_ORGANIZATION",
        "OrganizationId %d does not match OrganizationId %d of SpontaneousForm %d".formatted(organizationId, spontaneousForm.getOrganizationId(), spontaneousFormId));
    }
  }

  @Override
  public PagedSpontaneousForm getPagedSpontaneousForms(Long organizationId, String code, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return pagedSpontaneousFormMapper.map(spontaneousFormService.findAllByOrganizationIdAndCode(organizationId, code, pageable, accessToken));
  }

  @Override
  public SpontaneousFormDetailDTO getSpontaneousFormDetail(Long organizationId, Long spontaneousFormId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    SpontaneousForm spontaneousForm = spontaneousFormService.getSpontaneousForm(spontaneousFormId, accessToken);
    validateRetrievedSpontaneousForm(spontaneousForm,organizationId,spontaneousFormId);
    return spontaneousFormDetailDTOMapper.map(spontaneousForm);
  }

  @Override
  public SpontaneousForm createSpontaneousForm(Long organizationId, SpontaneousForm spontaneousForm, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
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
      throw new ConflictException("SPONTANEOUS_FORM_ALREADY_EXISTS", "There is another SpontaneousForm with organizationId "+ spontaneousForm.getOrganizationId()+" and code "+ spontaneousForm.getCode());
    }
  }

  @Override
  public void deleteSpontaneousForm(Long organizationId, Long spontaneousFormId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    getSpontaneousFormAndValidate(spontaneousFormId, organizationId, accessToken);
    if(debtPositionTypeOrgService.isSpontaneousFormReferencedByDpto(spontaneousFormId, accessToken)){
      throw new ConflictException("SPONTANEOUS_FORM_IN_USE", "The SpontaneousForm having id "+ spontaneousFormId +" is referenced by some DebtPositionTypeOrgs");
    }
    spontaneousFormService.deleteSpontaneousForm(spontaneousFormId,accessToken);
  }

  @Override
  public void updateSpontaneousForm(Long organizationId, SpontaneousForm spontaneousForm, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    SpontaneousForm existingSpontaneousForm = getSpontaneousFormAndValidate(spontaneousForm.getSpontaneousFormId(), organizationId, accessToken);
    checkReadOnlyFields(existingSpontaneousForm, spontaneousForm);
    spontaneousFormService.updateSpontaneousForm(spontaneousForm,accessToken);
  }

  private void checkReadOnlyFields(SpontaneousForm existingSpontaneousForm, SpontaneousForm updatedSpontaneousForm) {
    List<String> modifiedFields = new ArrayList<>();
    checkImmutableField("organizationId", existingSpontaneousForm.getOrganizationId(), updatedSpontaneousForm.getOrganizationId(), modifiedFields);
    checkImmutableField("code", existingSpontaneousForm.getCode(), updatedSpontaneousForm.getCode(), modifiedFields);
    if(!CollectionUtils.isEmpty(modifiedFields)){
      throw new ValidationException("The following SpontaneousForm fields are readOnly. "+modifiedFields);
    }
  }
}
