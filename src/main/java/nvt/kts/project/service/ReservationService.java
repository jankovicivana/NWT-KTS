package nvt.kts.project.service;

import nvt.kts.project.model.Reservation;
import nvt.kts.project.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation save(Reservation r){
        return this.reservationRepository.save(r);
    }
}
