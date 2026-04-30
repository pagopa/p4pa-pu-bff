package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypePatchRequestBody;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.lang.reflect.Field;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeMapperTest {
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @InjectMocks
  private DebtPositionTypeMapper debtPositionTypeMapper;

  @Test
  void testMapDebtPositionTypeDetailDTO() {
    //given
    DebtPositionType debtPositionType = podamFactory.manufacturePojo(DebtPositionType.class);
    Taxonomy taxonomy = podamFactory.manufacturePojo(Taxonomy.class);

    //when
    DebtPositionTypeDetailDTO result = debtPositionTypeMapper.mapToDebtPositionTypeDetailDTO(debtPositionType, taxonomy);

    //verify
    Assertions.assertNotNull(result);
    TestUtils.checkNotNullFields(result);
    Assertions.assertEquals(debtPositionType.getDebtPositionTypeId(), result.getDebtPositionTypeId());
    Assertions.assertEquals(debtPositionType.getCode(), result.getCode());
    Assertions.assertEquals(debtPositionType.getDescription(), result.getDescription());
    Assertions.assertEquals(debtPositionType.getTaxonomyCode(), result.getTaxonomyCode());
    Assertions.assertEquals(debtPositionType.getFlagAnonymousFiscalCode(), result.getFlagAnonymousFiscalCode());
    Assertions.assertEquals(debtPositionType.getFlagMandatoryDueDate(), result.getFlagMandatoryDueDate());
    Assertions.assertEquals(debtPositionType.getFlagNotifyIo(), result.getFlagNotifyIo());
    Assertions.assertEquals(taxonomy.getOrganizationTypeDescription(), result.getOrgType());
    Assertions.assertEquals(taxonomy.getMacroAreaName(), result.getMacroArea());
    Assertions.assertEquals(taxonomy.getServiceType(), result.getServiceType());
    Assertions.assertEquals(taxonomy.getCollectionReason(), result.getCollectingReason());
  }

  @Test
  void givenNullDebtPositionTypeThenReturnNull() {
    //given
    Taxonomy taxonomy = podamFactory.manufacturePojo(Taxonomy.class);

    //verify
    Assertions.assertNull(debtPositionTypeMapper.mapToDebtPositionTypeDetailDTO(null, taxonomy));
  }

  @Test
  void givenNullTaxonomyThenReturnNull() {
    //given
    DebtPositionType debtPositionType = podamFactory.manufacturePojo(DebtPositionType.class);

    //verify
    Assertions.assertNull(debtPositionTypeMapper.mapToDebtPositionTypeDetailDTO(debtPositionType, null));
  }

  @Test
  void testMapToDebtPositionTypeRequestBody() {
    DebtPositionTypePatchRequestBody debtPositionTypePatchRequestBody = podamFactory.manufacturePojo(DebtPositionTypePatchRequestBody.class);
    DebtPositionType oldDpType =podamFactory.manufacturePojo(DebtPositionType.class);

    DebtPositionTypeRequestBody result = debtPositionTypeMapper.mapToDebtPositionTypeRequestBody(debtPositionTypePatchRequestBody, oldDpType);

    Assertions.assertNotNull(result);
    TestUtils.checkNotNullFields(result,"creationDate","updateDate","updateOperatorExternalId","updateTraceId","debtPositionTypeId","description","taxonomyCode","code","brokerId", "orgType", "macroArea", "serviceType", "collectingReason");
    TestUtils.reflectionEqualsByName(debtPositionTypePatchRequestBody,result);

    ReflectionUtils.doWithFields(DebtPositionType.class, oldEntityField -> {
      if(oldEntityField.getAnnotation(Nonnull.class) != null){
        oldEntityField.setAccessible(true);

        Field resultField = Objects.requireNonNull(ReflectionUtils.findField(DebtPositionTypeRequestBody.class, oldEntityField.getName()));
        resultField.setAccessible(true);
        Assertions.assertSame(
          oldEntityField.get(oldDpType),
          resultField.get(result)
        );
      }
    });
  }
}
