package finalterm;
import GenCol.*;

public class msg_reserve extends entity {   
	int user_id;
	int age;
	String airplane;
	int[][] seatMap;
	boolean food;
	
	public msg_reserve(String name, int user_id, int age, String airplane, int[][] seatMap, boolean food) {  
		super(name);  
		this.user_id = user_id;
		this.age = age;
		this.airplane = airplane;
		this.seatMap = seatMap;
		this.food = food;
	}
}
