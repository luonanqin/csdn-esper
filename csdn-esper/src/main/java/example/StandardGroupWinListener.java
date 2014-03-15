package example;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class StandardGroupWinListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			EventBean event = newEvents[0];
			System.out.println("type: " + event.get("type") + ", sum(price): " + event.get("sum(price)"));
		}
	}
}
