package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.InvalidOrganizationException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@Service
@Slf4j
public class OrganizationClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrganizationClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public void updateOrganization(OrganizationDetailDTO organizationDetailDTO, String accessToken) {
    try {
      organizationApisHolder.getOrganizationApi(accessToken)
        .updateOrganization(organizationDetailDTO);
    } catch (HttpClientErrorException.NotFound e) {
      throw new ResourceNotFoundException("Organization with organizationId "+ organizationDetailDTO.getOrganizationId()+" not found");
    } catch (HttpClientErrorException.BadRequest e) {
      throw new InvalidOrganizationException(Objects.requireNonNull(e.getResponseBodyAs(OrganizationErrorDTO.class)).getMessage());
    }
  }
}
