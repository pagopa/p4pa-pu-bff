package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDetailsDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrganizationDetailMapperTest {
  private OrganizationDetailMapperImpl mapper;

  @BeforeEach
  void setUp() {
    mapper = new OrganizationDetailMapperImpl();
  }

  @Test
  void givenValidInputWhenMapToBffDtoThenBasicFieldsAreMapped() {
    var source = getSource();
    var result = mapper.mapToBffDTO(source);

    assertNotNull(result);
    assertEquals(source.getOrganizationId(), result.getOrganizationId());
    assertEquals(source.getFlagTreasury(), result.getFlagTreasury());
    assertEquals(source.getExternalOrganizationId(), result.getExternalOrganizationId());
    assertEquals(source.getIpaCode(), result.getIpaCode());
    assertEquals(source.getOrgFiscalCode(), result.getOrgFiscalCode());
    assertEquals(source.getOrgName(), result.getOrgName());
    assertEquals(source.getOrgTypeCode(), result.getOrgTypeCode());
    assertEquals(source.getOrgEmail(), result.getOrgEmail());
    assertEquals(source.getPostalIban(), result.getPostalIban());
    assertEquals(source.getIban(), result.getIban());
    assertEquals(source.getPassword(), result.getPassword());
    assertEquals(source.getSegregationCode(), result.getSegregationCode());
    assertEquals(source.getCbillInterBankCode(), result.getCbillInterBankCode());
  }

  @Test
  void givenValidInputWhenMapToBffDtoThenRemainingFieldsAreMapped() {
    var source = getSource();
    var result = mapper.mapToBffDTO(source);

    assertNotNull(result);
    assertEquals(source.getOrgLogo(), result.getOrgLogo());
    assertEquals(source.getStatus(), result.getStatus());
    assertEquals(source.getAdditionalLanguage(), result.getAdditionalLanguage());
    assertEquals(source.getStartDate(), result.getStartDate());
    assertEquals(source.getBrokerId(), result.getBrokerId());
    assertEquals(source.getIoApiKey(), result.getIoApiKey());
    assertEquals(source.getSendApiKey(), result.getSendApiKey());
    assertEquals(source.getGenerateNoticeApiKey(), result.getGenerateNoticeApiKey());
    assertEquals(source.getFlagNotifyIo(), result.getFlagNotifyIo());
    assertEquals(source.getFlagNotifyOutcomePush(), result.getFlagNotifyOutcomePush());
    assertEquals(source.getFlagPaymentNotification(), result.getFlagPaymentNotification());
    assertEquals(source.getPdndEnabled(), result.getPdndEnabled());
  }

  private static it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO getSource() {
    it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO source = new it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO();
    source.setOrganizationId(123L);
    source.setFlagTreasury(true);
    source.setExternalOrganizationId("EXT-ORG-001");
    source.setIpaCode("IPA123");
    source.setOrgFiscalCode("FISCAL123");
    source.setOrgName("Test Organization");
    source.setOrgTypeCode("TYPE1");
    source.setOrgEmail("test@org.it");
    source.setPostalIban("IT00P123456789");
    source.setIban("IT00I123456789");
    source.setPassword("secret");
    source.setSegregationCode("SEG123");
    source.setCbillInterBankCode("CBILL123");
    source.setOrgLogo("logo");
    source.setStatus(OrganizationStatus.ACTIVE);
    source.setAdditionalLanguage("EN");
    source.setStartDate(LocalDate.of(2025, 9, 5));
    source.setBrokerId(123L);
    source.setIoApiKey("IO-KEY");
    source.setSendApiKey("SEND-KEY");
    source.setGenerateNoticeApiKey("NOTICE-KEY");
    source.setFlagNotifyIo(true);
    source.setFlagNotifyOutcomePush(true);
    source.setFlagPaymentNotification(false);
    source.setPdndEnabled(true);
    return source;
  }

  @Test
  void givenNullSourceWhenMapToBffDtoThenReturnNull() {
    OrganizationDetailsDTO result = mapper.mapToBffDTO(null);
    assertNull(result);
  }
}
