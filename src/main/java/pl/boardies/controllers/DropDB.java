package pl.boardies.controllers;

import org.springframework.web.bind.annotation.RestController;

import pl.boardies.models.Boardies;
import pl.boardies.models.Game;
import pl.boardies.models.Swap;
import pl.boardies.models.User;
import pl.boardies.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class DropDB {

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


    @RequestMapping("/dropdb")
    @ResponseBody
    public String dropdb() {

        String s = "Dropped";

        userRepository.deleteAll();

        gameRepository.deleteAll();

        boardiesRepository.deleteAll();

        swapRepository.deleteAll();

        sessionRepository.deleteAll();

        return s;
    }

}
