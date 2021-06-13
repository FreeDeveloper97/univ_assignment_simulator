package finalterm;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;


import java.util.Random;

public class confirm extends ViewableAtomic
{
	protected int seatTotal_A;
	protected int seatTotal_B;
	protected int[][] seatMap_A;
	protected int[][] seatMap_B;
	
	protected msg_confirm msg_confirm;

	public confirm()
	{
		this("reserve", 40, 40);
	}

	public confirm(String name, int seatTotal_A, int seatTotal_B) {
		super(name);
    
		addInport("check_in");
		addOutport("out");
		
		this.seatTotal_A = seatTotal_A;
		this.seatTotal_B = seatTotal_B;
	}
  
	public void initialize()
	{
		seatMap_A = new int[5][seatTotal_A/5];
		seatMap_B = new int[5][seatTotal_B/5];
		
		msg_confirm = new msg_confirm("none", new int[0][0], new int[0][0], 0, 0, 0);
		
		holdIn("wait", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "check_in", i)) {
					msg_confirm = (msg_confirm)x.getValOnPort("check_in", i);
					
					printSeats();
					
					holdIn("confirming", 0);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("confirming"))
		{
			holdIn("wait", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("confirming")) {
			m.add(makeContent("out", new msg_confirm(
					"CONFIRM FINISH", seatMap_A, seatMap_B, 0,0,0)));
		}
		return m;
	}
	
	//algorithm 모음
	public void printSeats() {
		seatMap_A = msg_confirm.seatMap_A;
		seatMap_B = msg_confirm.seatMap_B;
		
		System.out.println("-------Airplane__A-------");
		for(int i=0; i<seatTotal_A/5; i++) {
			for(int j=0; j<5; j++) {
				if(seatMap_A[j][i] == 0) {
					System.out.print("[---]");
				} else {
					System.out.printf("[#%02d]", seatMap_A[j][i]);
				}
			}
			System.out.println();
		}
		System.out.println("Empty : " + (seatTotal_A-msg_confirm.count_airA));
		System.out.println("Food : " + msg_confirm.foodCount+"\n");
		
		System.out.println("-------Airplane__B-------");
		for(int i=0; i<seatTotal_B/5; i++) {
			for(int j=0; j<5; j++) {
				if(seatMap_B[j][i] == 0) {
					System.out.print("[---]");
				} else {
					System.out.printf("[#%02d]", seatMap_B[j][i]);
				}
			}
			System.out.println();
		}
		System.out.println("Empty : " + (seatTotal_B-msg_confirm.count_airB));
		System.out.println("Food : " + msg_confirm.foodCount);
	}
}

