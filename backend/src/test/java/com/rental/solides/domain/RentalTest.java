package com.rental.solides.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RentalTest {

	@Test
	void countInclusiveRentalDays_threeDays() {
		LocalDate start = LocalDate.of(2025, 1, 1);
		LocalDate end = LocalDate.of(2025, 1, 3);
		assertThat(Rental.countInclusiveRentalDays(start, end)).isEqualTo(3);
	}

	@Test
	void countInclusiveRentalDays_sameDay_isOne() {
		LocalDate d = LocalDate.of(2025, 6, 10);
		assertThat(Rental.countInclusiveRentalDays(d, d)).isEqualTo(1);
	}

	@Test
	void countInclusiveRentalDays_endBeforeStart_throws() {
		assertThatThrownBy(() -> Rental.countInclusiveRentalDays(LocalDate.of(2025, 2, 5), LocalDate.of(2025, 2, 1)))
				.isInstanceOf(BusinessException.class);
	}

	@Test
	void calculateTotal_exampleFromPdf() {
		BigDecimal total = Rental.calculateTotal(new BigDecimal("100"), 3);
		assertThat(total).isEqualByComparingTo(new BigDecimal("300.00"));
	}

	@Test
	void periodsOverlap_touchingDays_overlap() {
		LocalDate a1 = LocalDate.of(2025, 1, 1);
		LocalDate a2 = LocalDate.of(2025, 1, 5);
		LocalDate b1 = LocalDate.of(2025, 1, 5);
		LocalDate b2 = LocalDate.of(2025, 1, 10);
		assertThat(Rental.periodsOverlap(a1, a2, b1, b2)).isTrue();
	}

	@Test
	void periodsOverlap_disjoint_noOverlap() {
		LocalDate a1 = LocalDate.of(2025, 1, 1);
		LocalDate a2 = LocalDate.of(2025, 1, 3);
		LocalDate b1 = LocalDate.of(2025, 1, 4);
		LocalDate b2 = LocalDate.of(2025, 1, 10);
		assertThat(Rental.periodsOverlap(a1, a2, b1, b2)).isFalse();
	}

	@Test
	void create_computesTotal() {
		UUID vid = UUID.randomUUID();
		UUID cid = UUID.randomUUID();
		Rental r = Rental.create(vid, cid, LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 3), new BigDecimal("100"));
		assertThat(r.getTotalAmount()).isEqualByComparingTo(new BigDecimal("300.00"));
		assertThat(r.getStatus()).isEqualTo(RentalStatus.ACTIVE);
	}
}
