package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserInfoLimitedScope;
import it.gov.pagopa.pu.bff.dto.generated.UserInfoDTO;
import it.gov.pagopa.pu.bff.exception.InvalidUserInfoException;
import org.springframework.stereotype.Component;

@Component
public class UserInfoDTOMapper {

  public UserInfoDTO mapToDTO(UserInfo userInfo) {
    if (userInfo == null) {
      return null;
    }

    if (userInfo instanceof UserInfoLimitedScope) {
      throw new InvalidUserInfoException("INVALID_USER_INFO", "Limited scope user information is not supported");
    }

    return UserInfoDTO.builder()
      .userId(userInfo.getUserId())
      .mappedExternalUserId(userInfo.getMappedExternalUserId())
      .fiscalCode(userInfo.getFiscalCode())
      .familyName(userInfo.getFamilyName())
      .name(userInfo.getName())
      .issuer(userInfo.getIssuer())
      .organizationAccess(userInfo.getOrganizationAccess())
      .organizations(userInfo.getOrganizations())
      .brokerId(userInfo.getBrokerId())
      .brokerFiscalCode(userInfo.getBrokerFiscalCode())
      .canManageUsers(userInfo.getCanManageUsers())
      .systemUser(userInfo.getSystemUser())
      .traceId(userInfo.getTraceId())
      .type(userInfo.getType())
      .build();
  }

}
