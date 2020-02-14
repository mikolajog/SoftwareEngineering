package pl.boardies.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import pl.boardies.models.Boardies;
import pl.boardies.models.Game;
import pl.boardies.repositories.GameRepository;
import pl.boardies.requestModels.GameAddDetaildRequestMode;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
public class GamesController {
	
	@Autowired
	public GameRepository gameRepository;
    
//    @PostMapping("/games/add/{title}")
//    public int addGame(@RequestBody GameAddDetaildRequestMode game){
//    	Game foundGame = gameRepository.findByTitleIgnoreCase(game.getTitle());
//    	if(foundGame != null) return foundGame.getId();
//    	Game game1 = new Game(game.getTitle(), game.getDescription(), game.getImg());
//		gameRepository.save(game1);
//    	return game1.getId();
//    }
	
    @PostMapping("/games/add")
    public int addGame(@RequestParam String title, @RequestParam String img, @RequestParam String description){
    	Game foundGame = gameRepository.findByTitleIgnoreCase(title);
    	if(foundGame != null) return foundGame.getId();
    	Game game = new Game(title, description, img);
    	gameRepository.save(game);
    	return game.getId();
    }
    
    @GetMapping("/games/game/{gameId}")
    public Game getGame(@PathVariable int gameId) {
    	return gameRepository.findById(gameId).get();
    }

    @GetMapping("/games/search/{title}")
    public List<Game> verify(@PathVariable(value = "title") String title){
    	List<Game> foundGames = gameRepository.findByTitleContainsIgnoreCase(title);
        List<Integer> foundGamesIds = new ArrayList<>();
        for(Game i: foundGames) {
            foundGamesIds.add(i.getId());
        }
        return gameRepository.findByTitleContainsIgnoreCase(title);
    }
    
    @GetMapping("/games/showAll")
    public List<Game> showAll(){
        return (List<Game>) gameRepository.findAll();
    }
    
}
