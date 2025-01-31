package com.isika.ServiceBookingSystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isika.ServiceBookingSystem.Entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findAllByCompanyId(Long companyId);
	
	List<Reservation> findAllByUserId(Long userId);
}