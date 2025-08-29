package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassificationStatusMapperTest {

  @Test
  void givenInfoLabels_whenMapStatus_thenReturnInfo() {
    assertEquals("INFO", ClassificationStatusMapper.mapStatus(ClassificationsEnum.IUD_RT_IUF));
    assertEquals("INFO", ClassificationStatusMapper.mapStatus(ClassificationsEnum.RT_IUF));
    assertEquals("INFO", ClassificationStatusMapper.mapStatus(ClassificationsEnum.RT_TES));
    assertEquals("INFO", ClassificationStatusMapper.mapStatus(ClassificationsEnum.IUD_RT_IUF_TES));
    assertEquals("INFO", ClassificationStatusMapper.mapStatus(ClassificationsEnum.RT_IUF_TES));
  }

  @Test
  void givenWarningLabels_whenMapStatus_thenReturnWarning() {
    assertEquals("WARNING", ClassificationStatusMapper.mapStatus(ClassificationsEnum.RT_NO_IUF));
    assertEquals("WARNING", ClassificationStatusMapper.mapStatus(ClassificationsEnum.RT_NO_IUD));
    assertEquals("WARNING", ClassificationStatusMapper.mapStatus(ClassificationsEnum.IUF_NO_TES));
  }

  @Test
  void givenErrorLabels_whenMapStatus_thenReturnError() {
    assertEquals("ERROR", ClassificationStatusMapper.mapStatus(ClassificationsEnum.DOPPI));
    assertEquals("ERROR", ClassificationStatusMapper.mapStatus(ClassificationsEnum.IUV_NO_RT));
    assertEquals("ERROR", ClassificationStatusMapper.mapStatus(ClassificationsEnum.TES_NO_IUF_OR_IUV));
    assertEquals("ERROR", ClassificationStatusMapper.mapStatus(ClassificationsEnum.IUF_TES_DIV_IMP));
    assertEquals("ERROR", ClassificationStatusMapper.mapStatus(ClassificationsEnum.IUD_NO_RT));
    assertEquals("ERROR", ClassificationStatusMapper.mapStatus(ClassificationsEnum.TES_NO_MATCH));
    assertEquals("ERROR", ClassificationStatusMapper.mapStatus(ClassificationsEnum.UNKNOWN));
  }

  @Test
  void givenNullLabel_whenMapStatus_thenReturnError() {
    assertEquals("ERROR", ClassificationStatusMapper.mapStatus(null));
  }
}
