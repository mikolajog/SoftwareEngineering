package pl.boardies.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "Session")
public class Session {


    @Id
    private int userId;

    @Column
    private String sessionId;

    //getters
    public int getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    //setters
    private void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void generateSessionId (int userId) throws NoSuchAlgorithmException {
        setSessionId(Hash.hash(String.valueOf(userId), true));
    }

    public Session(int userId) throws NoSuchAlgorithmException {
        this.userId = userId;
        generateSessionId(userId);
    }

    @Override
    public String toString() {
        return "SESSION: " + this.getUserId() + " ||  SESSION ID: " + this.getSessionId();
    }
    public Session() {}

}


