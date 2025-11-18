package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserInfoLimitedScope;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.dto.generated.UserInfoDTO;
import it.gov.pagopa.pu.bff.exception.InvalidUserInfoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class UserInfoDTOMapperTest {

  private final UserInfoDTOMapper mapper = new UserInfoDTOMapper();

  @Test
  void givenNullUserInfoWhenMapToDTOThenReturnNull() {
    // Given
    UserInfo userInfo = null;

    // When
    UserInfoDTO result = mapper.mapToDTO(userInfo);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void givenLimitedScopeUserInfoWhenMapToDTOThenThrowException() {
    // Given
    UserInfo userInfo = new UserInfoLimitedScope();

    // When // Then
    assertThatThrownBy(() -> mapper.mapToDTO(userInfo))
      .isInstanceOf(InvalidUserInfoException.class)
      .hasMessage("Limited scope user information is not supported");
  }

  @Test
  void givenValidUserInfoWhenMapToDTOThenReturnMappedDTO() {
    // Given
    UserInfo userInfo = new UserInfo();
    userInfo.setUserId("123");
    userInfo.setMappedExternalUserId("mapped-id");
    userInfo.setFiscalCode("ABCDEF12G34H567I");
    userInfo.setFamilyName("Doe");
    userInfo.setName("John");
    userInfo.setIssuer("issuer-system");
    userInfo.setOrganizationAccess("ACCESS1");
    userInfo.setOrganizations(List.of(UserOrganizationRoles.builder().operatorId("OPID").organizationId(1L).organizationIpaCode("IPA").organizationFiscalCode("FISC").roles(Collections.emptyList()).build()));
    userInfo.setBrokerId(1L);
    userInfo.setBrokerFiscalCode("BRFISC");
    userInfo.setCanManageUsers(true);
    userInfo.setSystemUser(false);
    userInfo.setTraceId("trace-123");
    userInfo.setType("STANDARD");

    // When
    UserInfoDTO dto = mapper.mapToDTO(userInfo);

    // Then
    assertThat(dto).isNotNull();
    assertThat(dto.getUserId()).isEqualTo("123");
    assertThat(dto.getMappedExternalUserId()).isEqualTo("mapped-id");
    assertThat(dto.getFiscalCode()).isEqualTo("ABCDEF12G34H567I");
    assertThat(dto.getFamilyName()).isEqualTo("Doe");
    assertThat(dto.getName()).isEqualTo("John");
    assertThat(dto.getIssuer()).isEqualTo("issuer-system");
    assertThat(dto.getOrganizationAccess()).isEqualTo("ACCESS1");
    assertThat(dto.getBrokerId()).isEqualTo(1L);
    assertThat(dto.getBrokerFiscalCode()).isEqualTo("BRFISC");
    assertThat(dto.getCanManageUsers()).isTrue();
    assertThat(dto.getSystemUser()).isFalse();
    assertThat(dto.getTraceId()).isEqualTo("trace-123");
    assertThat(dto.getType()).isEqualTo("STANDARD");
  }
}
