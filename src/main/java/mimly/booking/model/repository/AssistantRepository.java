package mimly.booking.model.repository;

import mimly.booking.model.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistantRepository extends JpaRepository<Assistant, Long> {

    Assistant findByUsername(String username);
}
