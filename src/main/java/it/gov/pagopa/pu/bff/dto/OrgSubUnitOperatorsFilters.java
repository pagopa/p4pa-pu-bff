package it.gov.pagopa.pu.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgSubUnitOperatorsFilters {

  private String mappedExternalUserId;
  private String fiscalCode;
  private String firstName;
  private String lastName;
}
