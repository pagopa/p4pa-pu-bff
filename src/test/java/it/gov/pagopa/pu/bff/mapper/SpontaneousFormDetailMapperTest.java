package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.SpontaneousFormDetailDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class SpontaneousFormDetailMapperTest {
  private SpontaneousFormDetailDTOMapperImpl mapper;

  @BeforeEach
  void setUp() {
    mapper = new SpontaneousFormDetailDTOMapperImpl();
  }

  @Test
  void whenMapThenReturnSpontaneousFormDetailDTO() {
    SpontaneousForm source = TestUtils.getPodamFactory().manufacturePojo(SpontaneousForm.class);
    SpontaneousFormDetailDTO result = mapper.map(source);

    TestUtils.reflectionEqualsByName(source, result);
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenNullSourceWhenMapThenReturnNull() {
    SpontaneousFormDetailDTO result = mapper.map(null);
    assertNull(result);
  }
}
