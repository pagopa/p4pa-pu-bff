package it.gov.pagopa.pu.bff.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.gov.pagopa.pu.organization.dto.generated.*;
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
public class OrgSilServiceDTOExtended extends OrgSilServiceDTO {

  private SilServiceLegacyBasicAuthConfigDTO legacyBasicAuthConfig;
  private SilServiceLegacyJwtAuthConfigDTO legacyJwtAuthConfig;

  //This method should not be used since the authConfig field has been remapped
  @Override
  public OrgSilServiceDTOAuthConfig getAuthConfig() {
    throw new UnsupportedOperationException("This method is not supported in OrgSilServiceDTOExtended.");
  }

  //This method should not be used since the authConfig field has been remapped
  @Override
  public void setAuthConfig(@Nullable OrgSilServiceDTOAuthConfig authConfig) {
    throw new UnsupportedOperationException("This method is not supported in OrgSilServiceDTOExtended.");
  }
}
