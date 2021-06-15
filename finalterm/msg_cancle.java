package finalterm;
import java.util.ArrayList;

import GenCol.*;

public class msg_cancle extends entity
{   
	int user_id;
	String whatAirplane;
	String cancleSeat;
	int[][] seatMap_A;
	int[][] seatMap_B;
	ArrayList<Integer> foodlist_A;
	ArrayList<Integer> foodlist_B;
	public msg_cancle(String name, int user_id, String whatAirplane, String cancleSeat,
			int[][] seatMap_A, int[][] seatMap_B, ArrayList<Integer> foodlist_A, ArrayList<Integer> foodlist_B)
	{  
		super(name);  
		this.user_id = user_id;
		this.whatAirplane = whatAirplane;
		this.cancleSeat = cancleSeat;
		this.seatMap_A = seatMap_A;
		this.seatMap_B = seatMap_B;
		this.foodlist_A = foodlist_A;
		this.foodlist_B = foodlist_B;
	}
	
}
