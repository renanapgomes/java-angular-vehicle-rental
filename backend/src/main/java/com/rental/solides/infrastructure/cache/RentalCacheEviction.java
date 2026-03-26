package com.rental.solides.infrastructure.cache;

import com.rental.solides.application.port.RentalMutationNotifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class RentalCacheEviction implements RentalMutationNotifier {

	@Override
	@CacheEvict(cacheNames = "availableVehicles", allEntries = true)
	public void onRentalChanged() {
		// invalidação delegada ao proxy do Spring
	}
}
