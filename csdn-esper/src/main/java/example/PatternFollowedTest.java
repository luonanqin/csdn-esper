package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 9/5/14.
 */
class FollowedEvent {

	private int size;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String toString() {
		return "FollowedEvent{" + "size=" + size + '}';
	}
}

class PatternFollowedListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println();
				EventBean event = newEvents[i];
				System.out.println("Result:");
				System.out.println(event.get("a") + " " + event.get("b"));
			}
		}
	}
}

public class PatternFollowedTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String followed = FollowedEvent.class.getName();

		String epl = "select * from pattern[every a=" + followed + " -> b=" + followed + "(size < a.size)]";
		System.out.println("EPL: " + epl + "\n");
		EPStatement stat = admin.createEPL(epl);
		stat.addListener(new PatternFollowedListener());

		FollowedEvent f1 = new FollowedEvent();
		f1.setSize(1);
		System.out.println("Send Event1: " + f1);
		runtime.sendEvent(f1);
		System.out.println();

		FollowedEvent f2 = new FollowedEvent();
		f2.setSize(3);
		System.out.println("Send Event2: " + f2);
		runtime.sendEvent(f2);
		System.out.println();

		FollowedEvent f3 = new FollowedEvent();
		f3.setSize(2);
		System.out.println("Send Event3: " + f3);
		runtime.sendEvent(f3);

		FollowedEvent f4 = new FollowedEvent();
		f4.setSize(0);
		System.out.println("Send Event4: " + f4);
		runtime.sendEvent(f4);
	}
}
