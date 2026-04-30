package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.InvalidOrganizationException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.UpstreamErrorMapper;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class OrganizationClient {

  private final OrganizationApisHolder organizationApisHolder;
  private final UpstreamErrorMapper upstreamErrorMapper;

  public OrganizationClient(OrganizationApisHolder organizationApisHolder, UpstreamErrorMapper upstreamErrorMapper) {
    this.organizationApisHolder = organizationApisHolder;
    this.upstreamErrorMapper = upstreamErrorMapper;
  }

  public void updateOrganization(OrganizationDetailDTO organizationDetailDTO, String accessToken) {
    try {
      organizationApisHolder.getOrganizationApi(accessToken)
        .updateOrganization(organizationDetailDTO);
    } catch (HttpClientErrorException.NotFound e) {
      throw new ResourceNotFoundException("ORGANIZATION_NOT_FOUND", "Organization with organizationId " + organizationDetailDTO.getOrganizationId() + " not found");
    } catch (HttpClientErrorException.BadRequest e) {
      UpstreamErrorMapper.MappedUpstreamError mapped = upstreamErrorMapper.from(e);

      throw new InvalidOrganizationException(mapped.code(), mapped.description());
    }
  }
}
