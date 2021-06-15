package finalterm;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class cancle extends ViewableAtomic
{
	protected int seatTotal_A;
	protected int seatTotal_B;
	
	protected String whatAirplane;
	protected int userId;
	protected int x;
	protected int y;
	protected int[][] seatMap_A;
	protected int[][] seatMap_B;
	protected ArrayList<Integer> foodlist_A;
	protected ArrayList<Integer> foodlist_B;
	
	protected msg_cancle msg_cancle;

	public cancle()
	{
		this("cancle", 40, 40);
	}

	public cancle(String name, int seatTotal_A, int seatTotal_B) {
		super(name);
    
		addInport("check_in");
		addOutport("out");
		
		this.seatTotal_A = seatTotal_A;
		this.seatTotal_B = seatTotal_B;
	}
  
	public void initialize()
	{
		whatAirplane = "";
		userId = 0;
		x = 0;
		y = 0;
		seatMap_A = new int[5][seatTotal_A/5];
		seatMap_B = new int[5][seatTotal_B/5];
		foodlist_A = new ArrayList<Integer>();
		foodlist_B = new ArrayList<Integer>();

		msg_cancle = new msg_cancle("none", 0, "none", "none", 
				seatMap_A, seatMap_B, foodlist_A, foodlist_B);
		
		holdIn("wait", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "check_in", i)) {
					msg_cancle = (msg_cancle)x.getValOnPort("check_in", i);
					
					findLocation();
					cancleSeat();
					
					holdIn("cancleing", 0);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("cancleing"))
		{
			holdIn("wait", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		String seat = whatAirplane+"-"+x+y;
		if (phaseIs("cancleing")) {
			System.out.println("   ---------- cancled : "+seat+" ----------");
			m.add(makeContent("out", new msg_cancle(
					"User_"+msg_cancle.user_id+" cancled : "+seat, 0, whatAirplane, seat, 
					seatMap_A, seatMap_B, foodlist_A, foodlist_B)));
		}
		return m;
	}
	
	//algorithm 모음
	public void findLocation() {
		userId = msg_cancle.user_id;
		seatMap_A = msg_cancle.seatMap_A;
		seatMap_B = msg_cancle.seatMap_B;
		
		for(int i=0; i<seatTotal_A/5; i++) {
			for(int j=0; j<5; j++) {
				if(seatMap_A[j][i] == userId) {
					whatAirplane = "A";
					x = j;
					y = i;
					return;
				}
			}
		}
		
		for(int i=0; i<seatTotal_B/5; i++) {
			for(int j=0; j<5; j++) {
				if(seatMap_B[j][i] == userId) {
					whatAirplane = "B";
					x = j;
					y = i;
					return;
				}
			}
		}
	}
	
	public void cancleSeat() {
		foodlist_A = msg_cancle.foodlist_A;
		foodlist_B = msg_cancle.foodlist_B;
		if(whatAirplane == "A") {
			seatMap_A[x][y] = 0;
			if(foodlist_A.contains(userId)) {
				Iterator<Integer> iter = foodlist_A.iterator();
				while (iter.hasNext()) {
					int i = iter.next();
				 
					if (i == userId) {
						iter.remove();
					}
				}
//				foodlist_A.remove(userId);
			}
		} else {
			seatMap_B[x][y] = 0;
			if(foodlist_B.contains(userId)) {
				Iterator<Integer> iter = foodlist_B.iterator();
				while (iter.hasNext()) {
					int i = iter.next();
				 
					if (i == userId) {
						iter.remove();
					}
				}
//				foodlist_B.remove(userId);
			}
		}
	}
}

