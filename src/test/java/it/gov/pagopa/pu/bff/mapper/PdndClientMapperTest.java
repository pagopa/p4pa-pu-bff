package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.PdndClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PdndClientMapperTest {

  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @InjectMocks
  private PdndClientMapper pdndClientMapper;

  @Test
  void givenNullPdndClientWhenMapToPdndClientNoSecretDTOThenReturnNull() {
    PdndClientNoSecretDTO result = pdndClientMapper.mapToPdndClientNoSecretDTO(null);

    assertNull(result);
  }

  @Test
  void whenMapToPdndClientNoSecretDTOThenReturnPdndClientNoSecretDTO() {
    PdndClient pdndClient = podamFactory.manufacturePojo(PdndClient.class);

    PdndClientNoSecretDTO result = pdndClientMapper.mapToPdndClientNoSecretDTO(pdndClient);

    assertNotNull(result);
    assertEquals(pdndClient.getClientId(), result.getClientId());
    assertEquals(pdndClient.getOrganizationId(), result.getOrganizationId());
    assertEquals(pdndClient.getSubUnitCode(), result.getSubUnitCode());
    assertEquals(pdndClient.getClientName(), result.getClientName());
    assertEquals(pdndClient.getKid(), result.getKid());
    assertEquals(pdndClient.getPublicKey(), result.getPublicKey());
  }
}
