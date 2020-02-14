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
import java.sql.Timestamp;

@Entity
@Table(name = "Swap")
public class Swap {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id; 

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "borrower_id", nullable = false)
    private User borrower;
    // @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore - to by bylo wyzej ewentualnie
    
    @Column
    private Timestamp swapDate;
    
    @Column
    private Timestamp expirationDate;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "boardie_id", nullable = false)
    private Boardies boardie;
    
    //getters
    public User getBorrower() {
    	return borrower;
    }
    
    public int getId() {
    	return id;
    }
    
    public Timestamp getSwapDate() {
    	return swapDate;
    }
    
    public Timestamp getExpirationDate() {
    	return expirationDate;
    }
    
    public Boardies getBoardie() {
    	return boardie;
    }


    
    //setters 
    public void setBorrower(User borrower) {
    	this.borrower = borrower;
    }
    
    public void setSwapDate(Timestamp swapDate) {
    	this.swapDate = swapDate;
    }
    
    public void setExpirationDate(Timestamp expirationDate) {
    	this.expirationDate = expirationDate;
    }
    
    public void setBoardie(Boardies boardie) {
    	this.boardie = boardie;
    }
    
    public Swap (User borrower, Timestamp swapDate, Timestamp expirationDate, Boardies boardie) {
    	this.borrower = borrower;
    	this.swapDate = swapDate;
    	this.expirationDate = expirationDate;
    	this.boardie = boardie;
    }
    
    @Override
    public String toString() {
    	return "SwapID: " + this.id + " GameID: " + this.boardie.getId() + " 'Nazwa gry' | BORROWER: " + this.borrower.getUsername() + " | LENDER: " + this.boardie.getUser().getId() + " | TERMINY: " + this.swapDate + " - " + this.expirationDate;
    }
    
    public Swap() {}
    
 
}


