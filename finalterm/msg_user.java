package finalterm;
import GenCol.*;

public class msg_user extends entity {   
	int user_id;
	int age;
	
	public msg_user(String name, int user_id, int age) {  
		super(name);  
		this.user_id = user_id;
		this.age = age;
	}
}
