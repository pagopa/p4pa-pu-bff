package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PageMetadata;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentViewEmbedded;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InstallmentViewMapperTest {

  private final InstallmentViewMapper mapper = new InstallmentViewMapper();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedInstallmentViewThenCorrectMapping() {
    PagedModelInstallmentView pagedModel = new PagedModelInstallmentView();
    PagedModelInstallmentViewEmbedded embedded = new PagedModelInstallmentViewEmbedded();
    InstallmentView installmentView = new InstallmentView();
    installmentView.setInstallmentId(1L);

    embedded.setInstallmentViews(List.of(installmentView));
    pagedModel.setEmbedded(embedded);

    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModel.setPage(page);

    PagedInstallmentView result = mapper.mapToPagedInstallmentView(pagedModel);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    assertEquals(installmentView.getInstallmentId(), result.getContent().get(0).getInstallmentId());
  }

  @Test
  void givenNoContentWhenMapToPagedInstallmentViewThenPartialMapping() {
    PagedModelInstallmentView pagedModel = new PagedModelInstallmentView();
    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModel.setPage(page);

    PagedInstallmentView result = mapper.mapToPagedInstallmentView(pagedModel);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedInstallmentViewThenPartialMapping() {
    PagedModelInstallmentView pagedModel = new PagedModelInstallmentView();
    PagedModelInstallmentViewEmbedded embedded = new PagedModelInstallmentViewEmbedded();
    InstallmentView installmentView = new InstallmentView();
    installmentView.setInstallmentId(1L);

    embedded.setInstallmentViews(List.of(installmentView));
    pagedModel.setEmbedded(embedded);

    PagedInstallmentView result = mapper.mapToPagedInstallmentView(pagedModel);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    assertEquals(installmentView.getInstallmentId(), result.getContent().get(0).getInstallmentId());
  }

}
