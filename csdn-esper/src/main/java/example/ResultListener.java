package example;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class ResultListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			Object obj = newEvents[0].get("aid");
			if (obj != null) {
				double id = (Double) obj;
				System.out.println("currentResult: " + id);
			}
		}
	}

}
