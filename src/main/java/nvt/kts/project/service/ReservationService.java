package nvt.kts.project.service;

import nvt.kts.project.model.Reservation;
import nvt.kts.project.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation save(Reservation r){
        return this.reservationRepository.save(r);
    }

    public List<Reservation> getReservationsIn(Long minutes){
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> all = this.reservationRepository.findAll();
        List<Reservation> result = new ArrayList<>();
        for(Reservation r: all){
            if(now.until(r.getStart(), ChronoUnit.MINUTES) == minutes){
                result.add(r);
            }
        }
        return result;
    }

}
