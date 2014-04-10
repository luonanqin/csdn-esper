package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 3/29/14.
 */
class OnSelectTrigger {
	private int trigger;

	public int getTrigger() {
		return trigger;
	}

	public void setTrigger(int trigger) {
		this.trigger = trigger;
	}

	public String toString() {
		return "trigger=" + trigger;
	}
}

class OnSelectEvent {
	private String name;
	private int size;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String toString() {
		return "name=" + name + ", size=" + size;
	}
}

class OnSelectWindowListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println("Trigger On Select:");
			System.out.println("There is " + newEvents.length + " OnSelectEvent in OnSelectWindow!");
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}

public class OnSelectWindowTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String triggerEvent = OnSelectTrigger.class.getName();
		String selectEvent = OnSelectEvent.class.getName();

		// win:keepall()使named window中的事件一直保留
		// String epl1 = "create window OnSelectWindow.win:keepall() as select * from " + selectEvent;
		String epl1 = "create window OnSelectWindow.win:length(2) as select * from " + selectEvent;
		String epl2 = "insert into OnSelectWindow select * from " + selectEvent;
		String epl3 = "on " + triggerEvent + "(trigger>=2) select osw.* from OnSelectWindow as osw";

		System.out.println("Create Window:" + epl1);
		System.out.println("Trigger sentence: " + epl3);
		System.out.println();

		admin.createEPL(epl1);
		admin.createEPL(epl2);
		EPStatement state3 = admin.createEPL(epl3);
		state3.addListener(new OnSelectWindowListener());

		OnSelectEvent ose1 = new OnSelectEvent();
		ose1.setName("ose1");
		ose1.setSize(1);
		runtime.sendEvent(ose1);
		System.out.println("Send OnSelectEvent 1: " + ose1);

		OnSelectEvent ose2 = new OnSelectEvent();
		ose2.setName("ose2");
		ose2.setSize(2);
		runtime.sendEvent(ose2);
		System.out.println("Send OnSelectEvent 2: " + ose2);

		OnSelectEvent ose3 = new OnSelectEvent();
		ose3.setName("ose3");
		ose3.setSize(3);
		runtime.sendEvent(ose3);
		System.out.println("Send OnSelectEvent 3: " + ose3);

		OnSelectTrigger ost1 = new OnSelectTrigger();
		ost1.setTrigger(1);
		System.out.println("Send OnSelectTrigger " + ost1);
		runtime.sendEvent(ost1);

		OnSelectTrigger ost2 = new OnSelectTrigger();
		ost2.setTrigger(2);
		System.out.println("Send OnSelectTrigger " + ost2 + "\n");
		runtime.sendEvent(ost2);

		System.out.println();

		String epl4 = "on OnSelectWindow select osw.* from OnSelectWindow as osw";
		EPStatement state4 = admin.createEPL(epl4);
		state4.addListener(new OnSelectWindowListener());

		System.out.println("Trigger sentence: " + epl4 + "\n");

		OnSelectEvent ose4 = new OnSelectEvent();
		ose4.setName("ose4");
		ose4.setSize(4);
		System.out.println("Send OnSelectEvent 4(also a Trigger): " + ose4 + "\n");
		runtime.sendEvent(ose4);

		System.out.println();
		OnSelectEvent ose5 = new OnSelectEvent();
		ose5.setName("ose5");
		ose5.setSize(5);
		System.out.println("Send OnSelectEvent 5(also a Trigger): " + ose5 + "\n");
		runtime.sendEvent(ose5);
	}
}
