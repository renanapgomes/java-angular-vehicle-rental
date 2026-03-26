package com.rental.solides.application.port;

import com.rental.solides.domain.Rental;

public interface SaveRentalPort {
	Rental save(Rental rental);
}
