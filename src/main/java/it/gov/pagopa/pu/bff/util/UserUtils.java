package it.gov.pagopa.pu.bff.util;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;

public class UserUtils {

  private UserUtils() {
  }

  public static String getOperator(String operatorExternalId, UserInfo userInfo,
    UserInfo userInfoFromMappedExternalUserId) {
    if(!operatorExternalId.equals(userInfo.getMappedExternalUserId())){
      return userInfoFromMappedExternalUserId!=null?getOperatorString(userInfoFromMappedExternalUserId):operatorExternalId;
    }else{
      return getOperatorString(userInfo);
    }
  }

  public static String getOperatorString(UserInfo userInfo) {
    return String.format("%s %s", userInfo.getFamilyName(), userInfo.getName());
  }
}
