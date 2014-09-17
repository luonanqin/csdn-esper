package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 9/10/14.
 */
class LimitEvent {

	private int age;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String toString() {
		return "LimitEvent{" + "age=" + age + '}';
	}
}

class LimitFollowedListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println("\nResult: ");
			for (int i = 0; i < newEvents.length; i++) {
				EventBean event = newEvents[i];
				System.out.println("a=" + event.get("a") + " b=" + event.get("b"));
			}

			System.out.println();
		}
	}
}

public class LimitFollowedTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String limit = LimitEvent.class.getName();
		String follow = FollowedEvent.class.getName();

		/* 在每次触发完成前最多只保留2个a事件，触发条件为b的size值大于a的age */
		String epl = "every a=" + limit + " -[2]> b=" + follow + "(size > a.age)";
		System.out.println("EPL: " + epl + "\n");

		EPStatement stat = admin.createPattern(epl);
		stat.addListener(new LimitFollowedListener());

		System.out.println("First Send!\n");

		LimitEvent l1 = new LimitEvent();
		l1.setAge(1);
		System.out.println("Send Event: " + l1);
		runtime.sendEvent(l1);

		LimitEvent l2 = new LimitEvent();
		l2.setAge(2);
		System.out.println("Send Event: " + l2);
		runtime.sendEvent(l2);

		LimitEvent l3 = new LimitEvent();
		l3.setAge(0);
		System.out.println("Send Event: " + l3);
		runtime.sendEvent(l3);

		FollowedEvent f1 = new FollowedEvent();
		f1.setSize(3);
		System.out.println("Send Event: " + f1);
		runtime.sendEvent(f1);

		FollowedEvent f2 = new FollowedEvent();
		f2.setSize(4);
		System.out.println("Send Event: " + f2);
		runtime.sendEvent(f2);

		System.out.println();
		System.out.println("Second Send!\n");
		System.out.println("Send Event: "+l1);
		runtime.sendEvent(l1);
		System.out.println("Send Event: " + f1);
		runtime.sendEvent(f1);
	}
}
