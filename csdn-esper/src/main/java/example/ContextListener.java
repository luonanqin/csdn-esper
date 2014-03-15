package example;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class ContextListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			EventBean event = newEvents[0];
			System.out.println("id: " + event.get("id") + ", avgPrice: " + event.get("aPrice"));
		}
	}
}
