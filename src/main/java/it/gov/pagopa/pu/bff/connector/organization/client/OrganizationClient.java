package it.gov.pagopa.pu.bff.connector.organization.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.InvalidOrganizationException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.UpstreamErrorMapper;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class OrganizationClient {

  private final OrganizationApisHolder organizationApisHolder;
  private final ObjectMapper objectMapper;


  public OrganizationClient(OrganizationApisHolder organizationApisHolder, ObjectMapper objectMapper) {
    this.organizationApisHolder = organizationApisHolder;
    this.objectMapper = objectMapper;
  }

  public void updateOrganization(OrganizationDetailDTO organizationDetailDTO, String accessToken) {
    try {
      organizationApisHolder.getOrganizationApi(accessToken)
        .updateOrganization(organizationDetailDTO);
    } catch (HttpClientErrorException.NotFound e) {
      throw new ResourceNotFoundException("ORGANIZATION_NOT_FOUND", "Organization with organizationId "+ organizationDetailDTO.getOrganizationId()+" not found");
    } catch (HttpClientErrorException.BadRequest e) {
      OrganizationErrorDTO err = tryParseOrganizationError(e);

      String upstreamMessage = err != null ? err.getMessage() : null;

      UpstreamErrorMapper.MappedUpstreamError mapped =
        UpstreamErrorMapper.map(upstreamMessage, e.getMessage());

      throw new InvalidOrganizationException(mapped.code(), mapped.description());
    }
  }

  private OrganizationErrorDTO tryParseOrganizationError(HttpClientErrorException e) {
    try {
      String body = e.getResponseBodyAsString();
      if (body == null || body.isBlank()) return null;
      return objectMapper.readValue(body, OrganizationErrorDTO.class);
    } catch (Exception ignore) {
      return null;
    }
  }
}
