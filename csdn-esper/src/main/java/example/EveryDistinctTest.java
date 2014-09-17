package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 9/15/14.
 */
class EveryDistinctEvent {

	private int num;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String toString() {
		return "EveryDistinctEvent{" + "num=" + num + '}';
	}
}

class EveryDistinctListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println("\nResult: ");
			for (int i = 0; i < newEvents.length; i++) {
				EventBean event = newEvents[i];
				System.out.println(event.get("a"));
			}
		}
	}
}

public class EveryDistinctTest {

	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String everyDistinct = EveryDistinctEvent.class.getName();
		String limit = LimitEvent.class.getName();

		String epl1 = "every-distinct(a.num) a=" + everyDistinct;
		System.out.println("EPL1: " + epl1);
		EPStatement stat1 = admin.createPattern(epl1);
		stat1.addListener(new EveryDistinctListener());

		EveryDistinctEvent ed1 = new EveryDistinctEvent();
		ed1.setNum(1);

		EveryDistinctEvent ed2 = new EveryDistinctEvent();
		ed2.setNum(2);

		EveryDistinctEvent ed3 = new EveryDistinctEvent();
		ed3.setNum(1);

		System.out.println("\nSend Event: " + ed1);
		runtime.sendEvent(ed1);
		System.out.println("\nSend Event: " + ed2);
		runtime.sendEvent(ed2);
		System.out.println("\nSend Event: " + ed3);
		runtime.sendEvent(ed3);

		stat1.destroy();

		String epl2 = "every-distinct(a.num) (a=" + everyDistinct + " and not " + limit + ")";
		System.out.println("\nEPL2: " + epl2);
		EPStatement stat2 = admin.createPattern(epl2);
		stat2.addListener(new EveryDistinctListener());

		LimitEvent l1 = new LimitEvent();

		System.out.println("\nSend Event: " + ed1);
		runtime.sendEvent(ed1);
		System.out.println("\nSend Event: " + ed2);
		runtime.sendEvent(ed2);
		System.out.println("\nSend Event: " + l1);
		runtime.sendEvent(l1);
		System.out.println("\nSend Event: " + ed3);
		runtime.sendEvent(ed3);

		stat2.destroy();

		String epl3 = "every-distinct(a.num, 3 sec) a=" + everyDistinct;
		System.out.println("\nEPL3: " + epl3);
		EPStatement stat3 = admin.createPattern(epl3);
		stat3.addListener(new EveryDistinctListener());

		System.out.println("\nSend Event: " + ed1);
		runtime.sendEvent(ed1);
		System.out.println("\nSend Event: " + ed2);
		runtime.sendEvent(ed2);
		System.out.println("\nSleep 3 seconds!");
		Thread.sleep(3000);
		System.out.println("\nSend Event: " + ed3);
		runtime.sendEvent(ed3);
	}
}
