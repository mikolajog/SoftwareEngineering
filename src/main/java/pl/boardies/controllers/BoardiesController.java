package pl.boardies.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import pl.boardies.models.Boardies;
import pl.boardies.models.Game;
import pl.boardies.models.Hash;
import pl.boardies.models.User;
import pl.boardies.repositories.BoardiesRepository;
import pl.boardies.repositories.GameRepository;
import pl.boardies.repositories.SessionRepository;
import pl.boardies.repositories.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
public class BoardiesController {
	
	@Autowired
	public GameRepository gameRepository;

	@Autowired
	public BoardiesRepository boardiesRepository;

    @Autowired
    public SessionRepository sessionRepository;
    @Autowired
    public UserRepository userRepository;
    


    @PostMapping("/boardies/add")
    public boolean addBoardie(@RequestParam int id_game, @RequestParam int id_user, @RequestParam double price){
        if(boardiesRepository.save(new Boardies(userRepository.findById(id_user).get(), gameRepository.findById(id_game).get(), price, 1)) != null) {
        	return true;
        }
        return false;
    }

    @DeleteMapping(value = "/boardies/delete/{id}/{userId}/{sessionId}")
    public boolean deleteBoardie(@PathVariable(value = "id") int id, @PathVariable(value = "userId") int userId, @PathVariable(value = "sessionId") String sessionId) {
    	if(boardiesRepository.findByIdAndUserId(id, userId) == null || sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return false;
    	boardiesRepository.deleteById(id);
        return true;
    }


    @PostMapping(value = "/boardies/changeStatus/{id}/{userId}/{sessionId}/{status}")
    public boolean changeBoardieStatus(@PathVariable(value = "id") int id, @PathVariable(value = "userId") int userId, @PathVariable(value = "sessionId") String sessionId,  @PathVariable(value = "status") int status) {
        if(boardiesRepository.findByIdAndUserId(id, userId) == null || sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return false;
        Boardies boardie = boardiesRepository.findById(id).get();
        boardie.setStatus(status);
        boardiesRepository.save(boardie);
        return true;
    }
    
    @GetMapping("boardies/getCheapestPrice/{gameId}")
    public double getCheapestPrice(@PathVariable(value="gameId") int gameId) {
    	List<Boardies> list = boardiesRepository.findByGameIdOrderByPriceAsc(gameId);
    	if(list == null || list.size() == 0)
        	return 0.0;
    	return list.get(0).getPrice();
    }
    
    @GetMapping("boardies/getIdsOfUsersBoardies/{userId}")
    public List<Integer> getIdsOfUsersBoardies(@PathVariable(value="userId") int userId) {
    	List<Boardies> list = boardiesRepository.findByUserId(userId);
    	if(list == null || list.size() == 0)
        	return null;
    	List<Integer> temp = new ArrayList<>();
    	for(Boardies elem : list) {
    		temp.add(elem.getGame().getId());
    	}
    	return temp;
    }

    @GetMapping("/boardies/search/{title}")
    public List<Boardies> verify(@PathVariable(value = "title") String title){
        List<Boardies> foundGames = new ArrayList<>();
        for(Boardies i: boardiesRepository.findAllDistinctAndAvailable()) {
            if (i.getGame().getTitle().toLowerCase().contains(title.toLowerCase()) && i.getStatus() == 1){
                foundGames.add(i);
            }
        }
        return foundGames;
    }

    @GetMapping("/boardies/showAll")
    public List<Boardies> showAll(){
        return (List<Boardies>) boardiesRepository.findAll();
    }
    
    @PostMapping("/boardies/showAllAvailable")
    @ResponseBody
    public List<Boardies> showAllAvailable(@RequestParam int id_game) throws NoSuchAlgorithmException {
    	return boardiesRepository.findByGameIdAndStatus(id_game, 1);
    }
    
    @PostMapping("/boardies/showAllAvailableButMine")
    @ResponseBody
    public List<Boardies> showAllAvailableButMine(@RequestParam int id_game, @RequestParam int id_user) throws NoSuchAlgorithmException {
    	return boardiesRepository.findByGameAndUserIdAndState1(gameRepository.findById(id_game).get(), userRepository.findById(id_user).get());
    }
    
    @PostMapping("/showUsersBoardies")
    @ResponseBody
    public List<Boardies> showUsersBoardies(@RequestParam int userId) throws NoSuchAlgorithmException {
    	return boardiesRepository.findByUserId(userId);
    }
    @PostMapping("/showAvailableUsersBoardies")
    @ResponseBody
    public List<Boardies> showAvailableUsersBoardies(@RequestParam int userId) throws NoSuchAlgorithmException {
    	return boardiesRepository.findByUserIdAndStatus(userId, 1);
    }
    
    @GetMapping("boardies/showAllDistinct")
    public List<Boardies> showAllDistinct(){
        return (List<Boardies>) boardiesRepository.findAllDistinct();
    }

    @GetMapping("boardies/showAllDistinctAndAvailable")
    public List<Boardies> showAllDistinctAndAvailable(){
    	List<Boardies> lb = boardiesRepository.findAllDistinctAndAvailable();
    	/*
    	for (Iterator<Boardies> it = lb.iterator(); it.hasNext();) {
    	    Boardies b = it.next();
    	    if(b.getStatus() != 1)
    	    	it.remove();
    	} 
    	*/
    	
    	return lb;
        //return (List<Boardies>) boardiesRepository.findAllDistinctAndAvailable();
    }
}
