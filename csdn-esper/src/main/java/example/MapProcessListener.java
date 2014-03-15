package example;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class MapProcessListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			Object o1 = newEvents[0].get("id");
			if (o1 != null) {
				int id = (Integer) o1;
				System.out.println("id: " + id);
			}
			Object o2 = newEvents[0].get("sumCount");
			if (o2 != null) {
				int sumId = (Integer) o2;
				System.out.println("sumCount: " + sumId);
			}
		}
	}
}
