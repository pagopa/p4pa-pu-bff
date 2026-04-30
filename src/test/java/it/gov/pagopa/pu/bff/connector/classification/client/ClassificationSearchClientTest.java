package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.ClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.controller.generated.ClassificationSearchControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelClassification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationSearchClientTest {
  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private ClassificationSearchControllerApi classificationSearchControllerApiMock;

  private ClassificationSearchClient classificationSearchClient;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    classificationSearchClient = new ClassificationSearchClient(classificationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(classificationApisHolderMock, classificationSearchControllerApiMock);
  }

  @Test
  void givenValidParamsWhenGetClassificationsThenReturnExpected() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    ClassificationFiltersDTO classificationFiltersDTO = podamFactory.manufacturePojo(ClassificationFiltersDTO.class);
    PageRequest pageable = PageRequest.of(0, 10);

    PagedModelClassification expectedResult = new PagedModelClassification();

    when(classificationApisHolderMock.getClassificationSearchControllerApi(accessToken))
      .thenReturn(classificationSearchControllerApiMock);

    when(classificationSearchControllerApiMock.crudClassificationsFindByFilters(organizationId,
      classificationFiltersDTO.getIuv(),
      classificationFiltersDTO.getIuf(),
      classificationFiltersDTO.getDebtPositionTypeOrgCodes(),
      classificationFiltersDTO.getLabels(),
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable)))
      .thenReturn(expectedResult);

    PagedModelClassification result = classificationSearchClient.getClassifications(organizationId, classificationFiltersDTO, pageable, accessToken);

    assertSame(expectedResult, result);
  }
}
