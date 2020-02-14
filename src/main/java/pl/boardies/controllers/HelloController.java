package pl.boardies.controllers;

import org.springframework.web.bind.annotation.RestController;

import pl.boardies.models.Boardies;
import pl.boardies.models.Game;
import pl.boardies.models.Swap;
import pl.boardies.models.User;
import pl.boardies.repositories.BoardiesRepository;
import pl.boardies.repositories.GameRepository;
import pl.boardies.repositories.SwapRepository;
import pl.boardies.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
public class HelloController {

@Autowired
public UserRepository userRepository;

@Autowired
public SwapRepository swapRepository;

@Autowired
public GameRepository gameRepository;

@Autowired
public BoardiesRepository boardiesRepository;

  @RequestMapping("/gr")
  public String index() {
    return "Greetings from Spring Boot!";
  }
  
  @RequestMapping("/db")
  @ResponseBody
  public Game db() {
      StringBuilder response = new StringBuilder();
      
      User user = userRepository.findByUsernameIgnoreCase("pawioska");
      
      Game game = new Game("1","1","1");
      gameRepository.save(game);
      
      Boardies boardies = new Boardies(user,game,20.5, 1);
      boardiesRepository.save(boardies);

      Swap swap = new Swap(user,new Timestamp(1000000000),new Timestamp(2000000000),boardies);
      swapRepository.save(swap);

      for(User i: userRepository.findAll()) {
          response.append(i).append("<br>");
      }
      
      for(Game i: gameRepository.findAll()) {
          response.append(i).append("<br>");
      }
      
      for(Swap i: swapRepository.findAll()) {
          response.append(i).append("<br>");
      }
      
      for(Boardies i: boardiesRepository.findAll()) {
          response.append(i).append("<br>");
      }

      return game;
}

}
