package pl.boardies.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.boardies.models.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Integer> {
	Session findBySessionId (String sessionId);
	Session findByUserIdAndSessionId (int userId, String sessionId);
}
