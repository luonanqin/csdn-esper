package example;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class AverageListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			EventBean event = newEvents[0];
			System.out.println("avg price(win:length(2)): " + event.get("avg(price)"));
		}
	}
}
