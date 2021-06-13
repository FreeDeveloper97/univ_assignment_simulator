package finalterm;
import genDevs.modeling.*;

import java.util.ArrayList;

import GenCol.*;
import simView.*;

public class check extends ViewableAtomic
{
	private final static int RESERVE = 1;
	private final static int CANCLE = 2;
	private final static int CONFIRM = 3;
	protected int whatTodo;
	protected String whatAirplane;
	protected boolean isFull;
	
	protected ArrayList<Integer> userIds;
	protected int seatTotal_A;
	protected int seatTotal_B;
	protected int count_airA;
	protected int count_airB;
	protected int foodCount;
	protected int[][] seatMap_A;
	protected int[][] seatMap_B;
	
	protected msg_user msg_user;
	protected msg_reserve msg_reserve;
	

	public check()
	{
		this("check", 40, 40);
	}

	public check(String name, int seatTotal_A, int seatTotal_B)
	{
		super(name);
    
		addInport("user_in");
		addInport("reserve_in");
		addInport("cancle_in");
		addInport("confirm_in");
		
		addOutport("check_out");
		addOutport("reserve_out");		
		addOutport("cancle_out");
		addOutport("confirm_out");
		
		this.seatTotal_A = seatTotal_A;
		this.seatTotal_B = seatTotal_B;
	}
  
	public void initialize()
	{
		userIds = new ArrayList<Integer>();
		count_airA = 0;
		count_airB = 0;
		foodCount = 0;
		isFull = false;
		seatMap_A = new int[5][seatTotal_A/5];
		seatMap_B = new int[5][seatTotal_B/5];
		
		msg_user = new msg_user("none", 0, 0);
		msg_reserve = new msg_reserve("none", 0, 0, "none", new int[0][0], false);
		
		holdIn("waitUser", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("waitUser")) {
			for (int i = 0; i < x.getLength(); i++) {
				
				if (messageOnPort(x, "user_in", i)) {
					msg_user = (msg_user)x.getValOnPort("user_in", i);
					setWhatTodo(); //예매, 취소, 확인 중 한가지가 골라진다
					if(whatTodo == RESERVE) {
						setAirplane();
					}
					printUserIds();
					holdIn("start", 0);
				}
			}
		}
		
		else if (phaseIs("waitReserve")) {
			for (int i = 0; i < x.getLength(); i++) {
				whatTodo = RESERVE;
				if (messageOnPort(x, "reserve_in", i)) {
					msg_reserve = (msg_reserve)x.getValOnPort("reserve_in", i);
					updateData();
					
					holdIn("sent", 0);
				}
			}
		}
		
		else if (phaseIs("waitCancle")) {
			for (int i = 0; i < x.getLength(); i++) {
				
				if (messageOnPort(x, "cancle_in", i)) {
					

				}
			}
		}
		
		else if (phaseIs("waitConfirm")) {
			for (int i = 0; i < x.getLength(); i++) {
				
				if (messageOnPort(x, "confirm_in", i)) {
					

				}
			}
		}
		
	}
  
	public void deltint()
	{
		if (phaseIs("start")) {			
			if(whatTodo == RESERVE) {
				holdIn("waitReserve", INFINITY);
			}
			else if(whatTodo == CANCLE) {
//				holdIn("waitCancle", INFINITY);
				holdIn("waitUser", INFINITY);
			}
			else if(whatTodo == CONFIRM) {
//				holdIn("waitConfirm", INFINITY);
				holdIn("waitUser", INFINITY);
			}
		}
		
		else if(phaseIs("sent")) {
			if(!isFull) {
				holdIn("waitUser", INFINITY);
			} else {
				holdIn("finish", INFINITY);
			}
			
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("start")) {
			if(whatTodo == RESERVE) {
				int[][] seatMap;
				if(whatAirplane == "A") {
					seatMap = seatMap_A;
				} else {
					seatMap = seatMap_B;
				}
				
				m.add(makeContent("reserve_out", new msg_reserve(
						"User_"+msg_user.user_id+" : RESERVE!", msg_user.user_id, 
						msg_user.age, whatAirplane, seatMap, false)));
			}
			
			else if(whatTodo == CANCLE) {
				System.out.println("CANCLE CKICK!");
				m.add(makeContent("check_out", new msg_status(
						"CANCLE FINISH!", isFull)));
			}
			
			else if(whatTodo == CONFIRM) {
				System.out.println("CONFIRM CKICK!");
				m.add(makeContent("check_out", new msg_status(
						"CONFIRM FINISH!", isFull)));
			}
		}
		else if(phaseIs("sent")) {
			if(whatTodo == RESERVE) {
				m.add(makeContent("check_out", new msg_status(
						"RESERVE FINISH!", isFull)));
			}
			
		}
		return m;
	}

	//algorithm 모음
	public void setWhatTodo() {
		int user_id = msg_user.user_id;
		//첫번째 유저인 경우 -> whatTodo = RESERVE 설정
		if(userIds.size() == 0) {
			userIds.add(user_id);
			whatTodo = RESERVE;
		}
		else {
			while(true) {
				int randNum = (int)(Math.random()*10+1);
				//없는경우 - 50%, 40% 확률로 whatTodo 설정
				if(!userIds.contains(user_id)) {
					if(1 <= randNum && randNum <= 5) {
						userIds.add(user_id);
						whatTodo = RESERVE;
						break;
					} else if(6 <= randNum && randNum <= 9){
						whatTodo = CONFIRM;
						break;
					} 
				}
				//있는경우 - 50%, 10%, 40% 확률로 whatTodo 설정
				else {
					if(6 <= randNum && randNum <= 9) {
						whatTodo = CONFIRM;
						break;
					} else if(10 == randNum){
						whatTodo = CANCLE;
						break;
					}
				}
			}
		}
	}
	
	public void setAirplane() {
		if(count_airA < seatTotal_A && count_airB < seatTotal_B) {
			double persent = Math.random();
			if(persent <= 0.5) {
				whatAirplane = "A";
			} else {
				whatAirplane = "B";
			}
		}
		else if(count_airA == seatTotal_A && count_airB < seatTotal_B) {
			whatAirplane = "B";
		}
		else if(count_airA < seatTotal_A && count_airB == seatTotal_B) {
			whatAirplane = "A";
		}
	}
	
	public void updateData() {
		if(msg_reserve.airplane == "A") {
			seatMap_A = msg_reserve.seatMap;
			count_airA += 1;
		} else {
			seatMap_B = msg_reserve.seatMap;
			count_airB += 1;
		}
		
		if(msg_reserve.food) {
			foodCount += 1;
		}
		System.out.println("total count : "+(count_airA+count_airB));
		if(count_airA + count_airB == seatTotal_A + seatTotal_B) {
			isFull = true;
		}
	}
	
	public void printUserIds() {
		System.out.println(userIds);
	}
}

