package it.gov.pagopa.pu.bff.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceRequestBodyAuthConfig;
import it.gov.pagopa.pu.organization.dto.generated.SilServiceLegacyBasicAuthConfig;
import it.gov.pagopa.pu.organization.dto.generated.SilServiceLegacyJwtAuthConfig;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties({ "authConfig" })
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class OrgSilServiceExtended extends OrgSilService {
  private SilServiceLegacyBasicAuthConfig legacyBasicAuthConfig;
  private SilServiceLegacyJwtAuthConfig legacyJwtAuthConfig;

  //This method should not be used since the authConfig field has been remapped
  @Override
  public OrgSilServiceRequestBodyAuthConfig getAuthConfig() {
    throw new UnsupportedOperationException("This method is not supported in OrgSilServiceExtended.");
  }

  //This method should not be used since the authConfig field has been remapped
  @Override
  public void setAuthConfig(@Nullable OrgSilServiceRequestBodyAuthConfig authConfig) {
    throw new UnsupportedOperationException("This method is not supported in OrgSilServiceExtended.");
  }
}
