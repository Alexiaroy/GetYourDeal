package com.isika.ServiceBookingSystem.Service.Client;

import java.util.List;

import com.isika.ServiceBookingSystem.Dto.AdDTO;
import com.isika.ServiceBookingSystem.Dto.AdDetailsForClientDTO;
import com.isika.ServiceBookingSystem.Dto.ReservationDTO;
import com.isika.ServiceBookingSystem.Dto.ReviewDTO;

public interface ClientService {

	List<AdDTO> getAllAds();
	
	List<AdDTO> searchAdByName(String name);

	boolean bookService(ReservationDTO reservationDTO);
	
	AdDetailsForClientDTO getAdDetailsByAdId(Long adId);
	
	List<ReservationDTO> getAllBookingsByUserId(Long userId);
	
	boolean giveReview(ReviewDTO reviewDTO);
}
