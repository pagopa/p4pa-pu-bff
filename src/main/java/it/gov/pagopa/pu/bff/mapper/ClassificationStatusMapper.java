package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;

public final class ClassificationStatusMapper {

  private ClassificationStatusMapper() {
    // utility class, no instances
  }

  public static String mapStatus(ClassificationsEnum label) {
    if (label == null) {
      return "ERROR";
    }

    return switch (label) {
      case IUD_RT_IUF, RT_IUF, RT_TES, IUD_RT_IUF_TES, RT_IUF_TES -> "INFO";

      case RT_NO_IUF, RT_NO_IUD, IUF_NO_TES -> "WARNING";

      case DOPPI, IUV_NO_RT, TES_NO_IUF_OR_IUV,
        IUF_TES_DIV_IMP, IUD_NO_RT, TES_NO_MATCH, UNKNOWN -> "ERROR";
    };
  }
}
