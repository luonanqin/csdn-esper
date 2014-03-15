package example;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class TimeOldEventsListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (oldEvents != null)
			System.out.println("oldEvents count: " + oldEvents.length);
	}
}
