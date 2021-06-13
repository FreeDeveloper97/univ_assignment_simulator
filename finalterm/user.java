package finalterm;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;
import Lab14.auction_msg;

public class user extends ViewableAtomic
{
	protected int user_id;
	protected msg_user msg_user;
	protected msg_status msg_status;
	protected boolean isFull;
	
	protected String printStatus;
  
	public user() 
	{
		this("user");
	}
  
	public user(String name)
	{
		super(name);
   
		addInport("check_in");
		addOutport("user_out");
	}
  
	public void initialize()
	{
		user_id = 0;
		msg_user = new msg_user("none", 0, 0);
		msg_status = new msg_status("none", false);
		isFull = false;
		
		holdIn("start", 0);
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "check_in", i))
				{
					msg_status = (msg_status)x.getValOnPort("check_in", i);
					check_msg();
					
					if(isFull) {
						printFinish();
						holdIn("finish", INFINITY);
					} else {
						holdIn("start", 0);
					}
				}
			}
		}
	}

	public void deltint()
	{
		if (phaseIs("start")) {
			holdIn("wait", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		int user_id = getUser_id();
		int age = getAge();
		m.add(makeContent("user_out", new msg_user("User_"+user_id, user_id, age)));
		return m;
	}
	
	//algorithm 모음
	public int getUser_id() {
		return (int)(Math.random()*99 + 1); //1 ~ 99 난수 생성
	}
	
	public int getAge() {
		return (int)(Math.random()*79+2); //2 ~ 80 난수 생성
	}
	
	public void check_msg() {
		isFull = msg_status.isFull;
	}
	
	public void printFinish() {
		System.out.println("Let's go to flight!");
	}

}
