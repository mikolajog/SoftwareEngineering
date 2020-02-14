package pl.boardies.controllers;

import org.springframework.web.bind.annotation.RestController;

import pl.boardies.models.Boardies;
import pl.boardies.models.Game;
import pl.boardies.models.Swap;
import pl.boardies.models.User;
import pl.boardies.models.Session;
import pl.boardies.repositories.BoardiesRepository;
import pl.boardies.repositories.GameRepository;
import pl.boardies.repositories.SwapRepository;
import pl.boardies.repositories.UserRepository;
import pl.boardies.repositories.SessionRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import static java.lang.String.valueOf;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
public class Test2 {

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


    @RequestMapping("/test2")
    @ResponseBody
    public String test() throws NoSuchAlgorithmException {
        StringBuilder response = new StringBuilder();
        long time = System.currentTimeMillis();

        User user = new User(valueOf(time*2),"password","name","surname","street","postalCode","city",valueOf(time*3),valueOf(time*4), 100.0);
        userRepository.save(user);

        Game game = new Game("title","description","img");
        gameRepository.save(game);

        Boardies boardies = new Boardies(user,game,20.5, 1);
        //boardiesRepository.save(boardies);

        Swap swap = new Swap(user,new Timestamp(1000000000),new Timestamp(2000000000),boardies);
        //swapRepository.save(swap);

        Session session = new Session(user.getId());
        sessionRepository.save(session);

        for(User i: userRepository.findAll()) {
            response.append(i).append("<br>");
        }
        response.append("---------------------------------------").append("<br>");
        for(Game i: gameRepository.findAll()) {
            response.append(i).append("<br>");
        }
        response.append("---------------------------------------").append("<br>");

        for(Swap i: swapRepository.findAll()) {
            response.append(i).append("<br>");
        }
        response.append("---------------------------------------").append("<br>");

        for(Boardies i: boardiesRepository.findAll()) {
            response.append(i).append("<br>");
        }
        response.append("---------------------------------------").append("<br>");

        for(Session i: sessionRepository.findAll()) {
            response.append(i).append("<br>");
        }


        return response.toString();
    }

}