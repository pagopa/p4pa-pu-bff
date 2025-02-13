package it.gov.pagopa.pu.bff.util;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class UserUtils {

  private UserUtils() {
  }

  public static String getOperator(String operatorExternalId, UserInfo userInfo,
    UserInfo userInfoFromMappedExternaUserId) {
    if(!operatorExternalId.equals(userInfo.getMappedExternalUserId())){
      return userInfoFromMappedExternaUserId!=null?getOperatorString(userInfoFromMappedExternaUserId):operatorExternalId;
    }else{
      return getOperatorString(userInfo);
    }
  }

  public static String getOperatorString(UserInfo userInfo) {
    return String.format("%s %s", userInfo.getFamilyName(), userInfo.getName());
  }
}
