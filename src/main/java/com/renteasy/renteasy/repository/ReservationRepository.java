package com.renteasy.renteasy.repository;



import com.renteasy.renteasy.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
