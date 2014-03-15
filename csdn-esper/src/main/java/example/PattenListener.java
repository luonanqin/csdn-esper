package example;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class PattenListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) 
			System.out.println(newEvents.length);
	}

}
