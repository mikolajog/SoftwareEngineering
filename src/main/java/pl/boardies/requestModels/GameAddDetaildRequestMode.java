package pl.boardies.requestModels;

public class GameAddDetaildRequestMode {

    private String title;

    private String description;

    private String img;
    

    //getters
    public String getTitle() {
    	return title;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public String getImg() {
    	return img;
    
    }

    
    //setters 
    public void setTitle(String title) {
    	this.title = title;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }
    
    public void setImg(String img) {
    	this.img = img;
    }
    

    
    
    public GameAddDetaildRequestMode (String title, String description, String img) {
    	this.title = title;
    	this.description = description;
    	this.img = img;
    }
    
    @Override
    public String toString() {
    	return "Game: " + this.title + " " + this.description + " " + this.img;
    }
    
    public GameAddDetaildRequestMode () {}
 
}



