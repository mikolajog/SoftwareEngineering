package pl.boardies.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.boardies.models.Game;


@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {
	Game findByTitleIgnoreCase(String title);
	List<Game> findByTitleContainsIgnoreCase(String title);
}
