package it.gov.pagopa.pu.bff.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfoDTO {

  private String userId;
  private String mappedExternalUserId;
  @ToString.Exclude
  private String fiscalCode;
  @ToString.Exclude
  private String familyName;
  @ToString.Exclude
  private String name;
  @ToString.Exclude
  private String email;
  private String issuer;
  private String organizationAccess;
  @Builder.Default
  private List<UserOrganizationRolesDTO> organizations = new ArrayList<>();

}

