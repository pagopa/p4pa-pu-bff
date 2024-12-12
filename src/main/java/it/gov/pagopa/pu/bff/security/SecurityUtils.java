package it.gov.pagopa.pu.bff.security;

import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

  private SecurityUtils() {
  }

  /**
   * It will return user's session data from ThreadLocal
   */
  public static UserInfo getLoggedUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      Object principal = authentication.getPrincipal();
      if (principal instanceof UserInfo) {
        return (UserInfo) principal;
      }
    }
    return null;
  }

  /**
   * It will return user's session roles on requested organization IPA code retrieving it from ThreadLocal
   */
  public static Set<String> getLoggedUserRoles(String organizationIpaCode) {
    UserInfo loggedUser = getLoggedUser();
    if (loggedUser != null) {
      return loggedUser.getOrganizations().stream()
        .filter(org -> org.getOrganizationIpaCode().equals(organizationIpaCode))
        .flatMap(org -> org.getRoles().stream())
        .collect(Collectors.toSet());
    }
    return Collections.emptySet();
  }

  /**
   * It will return true if the user's session has ROLE_ADMIN role on requested organization IPA code retrieving it from ThreadLocal
   */
  public static boolean isLoggedUserAdmin(String organizationIpaCode) {
    return getLoggedUserRoles(organizationIpaCode).contains("ROLE_ADMIN");
  }

}
