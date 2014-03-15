package test;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class TestListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null)
			System.out.println(newEvents.length);
	}
}
