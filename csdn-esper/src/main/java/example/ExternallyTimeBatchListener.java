package example;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class ExternallyTimeBatchListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		System.out.println("avgTime: " + newEvents[0].get("at") + " " + newEvents[0].get("count"));
	}
}
