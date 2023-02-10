package banger.repository;


import banger.model.Rental;
import banger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, String> {
    List<Rental> getByUserAndToIsNull(User u);
    List<Rental> getByUserAndToIsNotNull(User u);
    List<Rental> findByUserAndFromAfterAndToBefore(User u, LocalDateTime from, LocalDateTime to);
}
