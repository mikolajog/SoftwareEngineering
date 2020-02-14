package pl.boardies.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.boardies.models.Boardies;
import pl.boardies.models.Game;
import pl.boardies.models.User;

@Repository
public interface BoardiesRepository extends CrudRepository<Boardies, Integer> {
	Boardies findByIdAndUserId(int id, int userId);
	List<Boardies> findByUserId(int userId);
	List<Boardies> findByGameId(int gameId);
	List<Boardies> findByGameIdAndStatus(int gameId, int status);
	List<Boardies> findByGameIdOrderByPriceAsc(int gameId);
	List<Boardies> findByUserIdAndStatus(int userId, int status);
	
	@Query("FROM Boardies WHERE game = ?1 AND user != ?2 AND status = 1 ORDER BY price ASC")
	List<Boardies> findByGameAndUserIdAndState1(Game game, User user);
	
	@Query(value = "SELECT `id`, min(`price`) AS `price`, `status`, `user_id`, `game_id` FROM `boardies` GROUP BY `game_id`", nativeQuery = true)
	List<Boardies> findAllDistinct();
	
	@Query(value = "SELECT `id`, min(`price`) AS `price`, `status`, `user_id`, `game_id` FROM `boardies` WHERE STATUS = 1 GROUP BY `game_id`", nativeQuery = true)
	List<Boardies> findAllDistinctAndAvailable();
}
