package finalterm;
import java.awt.*;
import simView.*;

public class inhaAirplane extends ViewableDigraph
{
	final int seatTotal = 15;
	
	public inhaAirplane()
	{
		super("airplane");
    	
		ViewableAtomic u = new user("user");
		ViewableAtomic c = new check("check", seatTotal, seatTotal);
		ViewableAtomic r = new reserve("reserve", seatTotal);
		
		add(u);
		add(c);
		add(r);
  
		addCoupling(u, "user_out", c, "user_in");
		addCoupling(c, "check_out", u, "check_in");
		
		addCoupling(c, "reserve_out", r, "check_in");
		addCoupling(r, "out", c, "reserve_in");
		
		
		
	}

    
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(988, 646);
        ((ViewableComponent)withName("user")).setPreferredLocation(new Point(40, 250));
        ((ViewableComponent)withName("check")).setPreferredLocation(new Point(250, 250));
        ((ViewableComponent)withName("reserve")).setPreferredLocation(new Point(600, 50));
        
    }
}
