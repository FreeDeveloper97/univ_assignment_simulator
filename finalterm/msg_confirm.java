package finalterm;
import GenCol.*;

public class msg_confirm extends entity
{   
	int[][] seatMap_A;
	int[][] seatMap_B;
	int count_airA;
	int count_airB;
	int foodCount;
	
	public msg_confirm(String name, int[][] seatMap_A, int[][] seatMap_B, int count_airA, int count_airB, int foodCount)
	{  
		super(name);  
		this.seatMap_A = seatMap_A;
		this.seatMap_B = seatMap_B;
		this.count_airA = count_airA;
		this.count_airB = count_airB;
		this.foodCount = foodCount;
	}
	
}
