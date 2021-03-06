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
	protected ArrayList<Integer> foodlist_A;
	protected ArrayList<Integer> foodlist_B;
	protected int[][] seatMap_A;
	protected int[][] seatMap_B;
	
	protected msg_user msg_user;
	protected msg_reserve msg_reserve;
	protected msg_confirm msg_confirm;
	protected msg_cancle msg_cancle;
	

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
		foodlist_A = new ArrayList<Integer>();
		foodlist_B = new ArrayList<Integer>();
		isFull = false;
		seatMap_A = new int[5][seatTotal_A/5];
		seatMap_B = new int[5][seatTotal_B/5];
		
		msg_user = new msg_user("none", 0, 0);
		msg_reserve = new msg_reserve("none", 0, 0, "none", new int[0][0], false);
		msg_confirm = new msg_confirm("none", seatMap_A, seatMap_B, 0, 0, 0, 0);
		msg_cancle = new msg_cancle("none", 0, "none", "none", 
				seatMap_A, seatMap_B, foodlist_A, foodlist_B);
		
		holdIn("waitUser", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("waitUser")) {
			for (int i = 0; i < x.getLength(); i++) {
				
				if (messageOnPort(x, "user_in", i)) {
					msg_user = (msg_user)x.getValOnPort("user_in", i);
					setWhatTodo(); //??????, ??????, ?????? ??? ???????????? ????????????
					if(whatTodo == RESERVE) {
						setAirplane();
					}
					
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
				whatTodo = CANCLE;
				if (messageOnPort(x, "cancle_in", i)) {
					msg_cancle = (msg_cancle)x.getValOnPort("cancle_in", i);
					updateCancled();

					holdIn("sent", 0);
				}
			}
		}
		
		else if (phaseIs("waitConfirm")) {
			for (int i = 0; i < x.getLength(); i++) {
				whatTodo = CONFIRM;
				if (messageOnPort(x, "confirm_in", i)) {
					msg_confirm = (msg_confirm)x.getValOnPort("confirm_in", i);
					
					holdIn("sent", 0);
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
				holdIn("waitCancle", INFINITY);
			}
			else if(whatTodo == CONFIRM) {
				holdIn("waitConfirm", INFINITY);
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
				m.add(makeContent("cancle_out", new msg_cancle(
						"User_"+msg_user.user_id+" : CANCLE!", msg_user.user_id, "none", "none", 
						seatMap_A, seatMap_B, foodlist_A, foodlist_B)));
			}
			
			else if(whatTodo == CONFIRM) {
				m.add(makeContent("confirm_out", new msg_confirm(
						"User_"+msg_user.user_id+" : CONFIRM!", seatMap_A, seatMap_B, 
						count_airA, count_airB, foodlist_A.size(), foodlist_B.size())));
			}
		}
		else if(phaseIs("sent")) {
			if(whatTodo == RESERVE) {
				m.add(makeContent("check_out", new msg_status(
						"RESERVE FINISH!", isFull)));
			}
			else if(whatTodo == CONFIRM) {
				m.add(makeContent("check_out", new msg_status(
						"CONFIRM FINISH!", isFull)));
			}
			else if(whatTodo == CANCLE) {
				m.add(makeContent("check_out", new msg_status(
						"CANCLE FINISH!", isFull)));
			}
			
		}
		return m;
	}

	//algorithm ??????
	public void setWhatTodo() {
		int user_id = msg_user.user_id;
		//????????? ????????? ?????? -> whatTodo = RESERVE ??????
		if(userIds.size() == 0) {
			userIds.add(user_id);
			whatTodo = RESERVE;
		}
		else {
			while(true) {
				int randNum = (int)(Math.random()*10+1);
				//???????????? - 50%, 40% ????????? whatTodo ??????
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
				//???????????? - 50%, 10%, 40% ????????? whatTodo ??????
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
			if(msg_reserve.airplane == "A") {
				foodlist_A.add(msg_reserve.user_id);
			} else {
				foodlist_B.add(msg_reserve.user_id);
			}
			
		}
		System.out.println("total count : "+(count_airA+count_airB));
		if(count_airA + count_airB == seatTotal_A + seatTotal_B) {
			isFull = true;
		}
	}
	
	public void updateCancled() {
		if(msg_cancle.whatAirplane == "A") {
			seatMap_A = msg_cancle.seatMap_A;
			foodlist_A = msg_cancle.foodlist_A;
			count_airA -= 1;
		} else {
			seatMap_B = msg_cancle.seatMap_B;
			foodlist_B = msg_cancle.foodlist_B;
			count_airB -= 1;
		}
		userIds.remove(msg_cancle.user_id);
		System.out.println("total count : "+(count_airA+count_airB));
	}
}

