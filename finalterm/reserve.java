package finalterm;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;


import java.util.Random;

public class reserve extends ViewableAtomic
{
	protected int seatTotal;
	protected String whatAirplane;
	protected int[][] seatMap;
	protected int x;
	protected int y;
	protected boolean food;
	
	protected msg_reserve msg_reserve;

	public reserve()
	{
		this("reserve", 40);
	}

	public reserve(String name, int seatTotal) {
		super(name);
    
		addInport("check_in");
		addOutport("out");
		
		this.seatTotal = seatTotal;
	}
  
	public void initialize()
	{
		whatAirplane = "";
		seatMap = new int[5][seatTotal/5];
		x = 0;
		y = 0;
		food = false;
		msg_reserve = new msg_reserve("none", 0, 0, "none", new int[0][0], false);
		
		holdIn("wait", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "check_in", i)) {
					msg_reserve = (msg_reserve)x.getValOnPort("check_in", i);
					
					setSeat();
					checkFood();
					
					holdIn("reserving", 0);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("reserving"))
		{
			holdIn("wait", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("reserving")) {
			m.add(makeContent("out", new msg_reserve(
					"User_"+msg_reserve.user_id+" : "+whatAirplane+"-"+x+y, msg_reserve.user_id, 
					msg_reserve.age, whatAirplane, seatMap, food)));
		}
		return m;
	}
	
	//algorithm 모음
	public void setSeat() {
		whatAirplane = msg_reserve.airplane;
		seatMap = msg_reserve.seatMap;
		int xLength = 5;
		int yLength = seatTotal/5;
		
		while(true) {
			int newX = (int)(Math.random()*xLength);
			int newY = (int)(Math.random()*yLength);
			if(isEmpty(newX, newY)) {
				x = newX;
				y = newY;
				seatMap[newX][newY] = msg_reserve.user_id;
				break;
			}
		}
	}
	
	public boolean isEmpty(int x, int y) {
		if(seatMap[x][y] == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void checkFood() {
		int age = msg_reserve.age;
		if(age >= 2 && age <= 8) {
			food = true;
		} else if(age >= 65 && age <= 80) {
			food = true;
		} else {
			food = false;
		}
	}
}

