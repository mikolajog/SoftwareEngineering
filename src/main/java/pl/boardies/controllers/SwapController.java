package pl.boardies.controllers;

import org.springframework.web.bind.annotation.*;

import pl.boardies.models.Boardies;
import pl.boardies.models.Game;
import pl.boardies.models.Session;
import pl.boardies.models.Swap;
import pl.boardies.models.User;
import pl.boardies.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
public class SwapController {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public SwapRepository swapRepository;

    @Autowired
    public GameRepository gameRepository;

    @Autowired
    public BoardiesRepository boardiesRepository;

    @Autowired
    public SessionRepository sessionRepository;

    @PostMapping(value = "/swap/prepare")
    @ResponseBody
    public boolean prepareSwap(@RequestParam int boardieId, @RequestParam int userId, @RequestParam String sessionId) {
        if(!boardiesRepository.findById(boardieId).isPresent() || sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return false;
        if(boardiesRepository.findById(boardieId).get().getStatus() != 1) return false;
        if(userRepository.findById(userId).get().getBalance() < boardiesRepository.findById(boardieId).get().getPrice()) return false;
        Boardies boardies = boardiesRepository.findById(boardieId).get();
        Swap swap = new Swap(userRepository.findById(userId).get(), null, null, boardies);
        boardies.setStatus(2);
        User user = userRepository.findById(userId).get();
        user.changeBalance(-boardies.getPrice());
        boardiesRepository.save(boardies);
        swapRepository.save(swap);
        userRepository.save(user);
        return true;
    }
    
    @PostMapping(value="/swap/getMyAwaitingTo")
    @ResponseBody
    public List<Swap> getMyAwaitingTo(@RequestParam int userId, @RequestParam String sessionId) {
    	if(sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return null;
    	return swapRepository.findByLender(userId);
    }
    
    @PostMapping(value="/swap/getMyAwaitingFrom")
    @ResponseBody
    public List<Swap> getMyAwaitingFrom(@RequestParam int userId, @RequestParam String sessionId) {
    	if(sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return null;
    	return swapRepository.findByBorrower(userId);
    }
    
    @PostMapping(value="/swap/getMyPastTo")
    @ResponseBody
    public List<Swap> getMyPastTo(@RequestParam int userId, @RequestParam String sessionId) {
    	if(sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return null;
    	List<Swap> temp = swapRepository.findByBorrower(userId);
    	for (Iterator<Swap> it = temp.iterator(); it.hasNext();) {
    	    Swap s = it.next();
            Timestamp now = new Timestamp(System.currentTimeMillis());
    	    if(!now.after(s.getExpirationDate()))
    	    	it.remove();
    	} 
    	return temp;
    }
    
    @PostMapping(value="/swap/getMyPastFrom")
    @ResponseBody
    public List<Swap> getMyPastFrom(@RequestParam int userId, @RequestParam String sessionId) {
    	if(sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return null;
    	List<Swap> temp = swapRepository.findByLender(userId);
    	for (Iterator<Swap> it = temp.iterator(); it.hasNext();) {
    	    Swap s = it.next();
            Timestamp now = new Timestamp(System.currentTimeMillis());
    	    if(!now.after(s.getExpirationDate()))
    	    	it.remove();
    	} 
    	return temp;
    }
    
    
    @PostMapping(value="/swap/getMyCurrentTo")
    @ResponseBody
    public List<Swap> getMyCurrentTo(@RequestParam int userId, @RequestParam String sessionId) {
    	if(sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return null;
    	List<Swap> temp = swapRepository.findByBorrower(userId);
    	for (Iterator<Swap> it = temp.iterator(); it.hasNext();) {
    	    Swap s = it.next();
            Timestamp now = new Timestamp(System.currentTimeMillis());
    	    if(!now.after(s.getSwapDate()) || !now.before(s.getExpirationDate()))
    	    	it.remove();
    	} 
    	return temp;
    }
    
    @PostMapping(value="/swap/getMyCurrentFrom")
    @ResponseBody
    public List<Swap> getMyCurrentFrom(@RequestParam int userId, @RequestParam String sessionId) {
    	if(sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return null;
    	List<Swap> temp = swapRepository.findByLender(userId);
    	for (Iterator<Swap> it = temp.iterator(); it.hasNext();) {
    	    Swap s = it.next();
            Timestamp now = new Timestamp(System.currentTimeMillis());
    	    if(!now.after(s.getSwapDate()) || !now.before(s.getExpirationDate()))
    	    	it.remove();
    	} 
    	return temp;
    }
    
    @PostMapping(value = "/swap/prepareForSession")
    @ResponseBody
    public boolean prepareSwapForSession(@RequestParam int boardieId, @RequestParam String sessionId) {
        if(!boardiesRepository.findById(boardieId).isPresent()) return false;
        if(boardiesRepository.findById(boardieId).get().getStatus() != 1) return false;
        Session ses = sessionRepository.findBySessionId(sessionId);
        User user = userRepository.findById(ses.getUserId()).get();
        if(user.getBalance() < boardiesRepository.findById(boardieId).get().getPrice()) return false;
        Boardies boardies = boardiesRepository.findById(boardieId).get();
        Swap swap = new Swap(user, null, null, boardies);
        boardies.setStatus(2);
        user.changeBalance(-boardies.getPrice());
        boardiesRepository.save(boardies);
        swapRepository.save(swap);
        userRepository.save(user);
        return true;
    }

    @PostMapping(value = "/swap/confirm")
    @ResponseBody
    public boolean confirmSwap(@RequestParam  int boardieId, @RequestParam  int userId, @RequestParam  String sessionId) {
        if(!boardiesRepository.findById(boardieId).isPresent() || sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return false;
        if(boardiesRepository.findById(boardieId).get().getStatus() != 2) return false;
        Boardies boardies = boardiesRepository.findById(boardieId).get();
        Swap swap = swapRepository.findByBoardieId(boardieId);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        swap.setSwapDate(now);
        swap.setExpirationDate(Timestamp.from(now.toInstant().plus(14, ChronoUnit.DAYS)));
        boardies.setStatus(0);
        User user = userRepository.findById(userId).get();
        user.changeBalance(boardies.getPrice());
        boardiesRepository.save(boardies);
        swapRepository.save(swap);
        userRepository.save(user);
        return true;
    }

    @DeleteMapping(value = "/swap/delete/{swapId}/{userId}/{sessionId}")
    public boolean deleteSwap(@PathVariable(value = "swapId") int swapId, @PathVariable(value = "userId") int userId, @PathVariable(value = "sessionId") String sessionId) {
        if(!swapRepository.findById(swapId).isPresent() || sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return false;
        Swap swap = swapRepository.findById(swapId).get();

        if(swap.getBoardie().getUser().getId() != userId) return false;
        Boardies boardies = swap.getBoardie();
        boardies.setStatus(1);
        swapRepository.deleteById(swapId);
        boardiesRepository.save(boardies);
        return true;
    }


    @PostMapping(value = "/swap/cancel")
    @ResponseBody
    public boolean cancelSwap(@RequestParam  int swapId, @RequestParam  int userId, @RequestParam  String sessionId) {
        if(!swapRepository.findById(swapId).isPresent() || sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return false;
        Swap swap = swapRepository.findById(swapId).get();

        if(swap.getBorrower().getId() != userId) return false;
        Boardies boardies = swap.getBoardie();
        boardies.setStatus(1);
        User user = swap.getBorrower();
        user.changeBalance(boardies.getPrice()/2);
        swapRepository.deleteById(swapId);
        boardiesRepository.save(boardies);
        userRepository.save(user);
        return true;
    }
    
    @PostMapping(value = "/swap/cancelByLender")
    @ResponseBody
    public boolean cancelSwapByLender(@RequestParam  int swapId, @RequestParam  int userId, @RequestParam  String sessionId) {
        if(!swapRepository.findById(swapId).isPresent() || sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null) return false;
        Swap swap = swapRepository.findById(swapId).get();
        Boardies boardies = swap.getBoardie();
        boardies.setStatus(1);
        User user = swap.getBorrower();
        user.changeBalance(boardies.getPrice());
        swapRepository.deleteById(swapId);
        boardiesRepository.save(boardies);
        return true;
    }




}
