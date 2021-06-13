package finalterm;
import GenCol.*;

public class msg_status extends entity {   
	boolean isFull;
	
	public msg_status(String name, boolean isFull) {  
		super(name);  
		this.isFull = isFull;
	}
}
