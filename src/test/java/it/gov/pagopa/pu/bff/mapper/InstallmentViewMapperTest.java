package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentViewDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedInstallmentsView;
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
    PagedInstallmentsView pagedInstallmentsView = new PagedInstallmentsView();
    InstallmentViewDTO installmentView = new InstallmentViewDTO();
    installmentView.setInstallmentId(1L);

    pagedInstallmentsView.setSize(10L);
    pagedInstallmentsView.setTotalElements(1L);
    pagedInstallmentsView.setTotalPages(1L);
    pagedInstallmentsView.setNumber(1L);
    pagedInstallmentsView.setContent(List.of(installmentView));

    PagedInstallmentView result = mapper.mapToPagedInstallmentView(pagedInstallmentsView);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    assertEquals(installmentView.getInstallmentId(), result.getContent().getFirst().getInstallmentId());
  }

  @Test
  void givenNoContentWhenMapToPagedInstallmentViewThenPartialMapping() {
    PagedInstallmentsView pagedInstallmentsView = new PagedInstallmentsView();

    pagedInstallmentsView.setSize(10L);
    pagedInstallmentsView.setTotalElements(1L);
    pagedInstallmentsView.setTotalPages(1L);
    pagedInstallmentsView.setNumber(1L);
    pagedInstallmentsView.setContent(List.of());

    PagedInstallmentView result = mapper.mapToPagedInstallmentView(pagedInstallmentsView);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }
}
