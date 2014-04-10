package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 3/30/14.
 */

class OnDeleteTrigger {
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

class OnDeleteEvent {
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

class OnDeleteWindowListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println();
			System.out.println("Trigger On Delete:");
			System.out.println("There is " + newEvents.length + " OnDeleteEvent to be deleted in OnDeleteWindow!");
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}

class OnSelectListener implements UpdateListener{

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println();
			System.out.println("Trigger On Select:");
			System.out.println("There is " + newEvents.length + " OnDeleteEvent in OnDeleteWindow!");
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}

public class OnDeleteWindowTest {
	public static void main(String[] args) {

		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String triggerEvent = OnDeleteTrigger.class.getName();
		String deleteEvent = OnDeleteEvent.class.getName();

		String epl1 = "create window OnDeleteWindow.win:keepall() as select * from " + deleteEvent;
		String epl2 = "insert into OnDeleteWindow select * from " + deleteEvent;
		String epl3 = "on " + triggerEvent + "(trigger>0) as odt delete from OnDeleteWindow as odw where odt.trigger=odw.size";
		String epl4 = "on " + triggerEvent + "(trigger=0) select odw.* from OnDeleteWindow as odw";

		System.out.println("Create Window:" + epl1);
		System.out.println("Delete Trigger sentence: " + epl3);
		System.out.println("Select Trigger sentence: " + epl4);
		System.out.println();

		admin.createEPL(epl1);
		admin.createEPL(epl2);
		EPStatement state3 = admin.createEPL(epl3);
		state3.addListener(new OnDeleteWindowListener());
		EPStatement state4 = admin.createEPL(epl4);
		state4.addListener(new OnSelectListener());

		OnDeleteEvent ose1 = new OnDeleteEvent();
		ose1.setName("ose1");
		ose1.setSize(1);
		runtime.sendEvent(ose1);
		System.out.println("Send OnDeleteEvent 1: " + ose1);

		OnDeleteEvent ose2 = new OnDeleteEvent();
		ose2.setName("ose2");
		ose2.setSize(2);
		runtime.sendEvent(ose2);
		System.out.println("Send OnDeleteEvent 2: " + ose2);

		OnDeleteEvent ose3 = new OnDeleteEvent();
		ose3.setName("ose3");
		ose3.setSize(3);
		runtime.sendEvent(ose3);
		System.out.println("Send OnDeleteEvent 3: " + ose3);

		OnDeleteTrigger ost1 = new OnDeleteTrigger();
		ost1.setTrigger(0);
		System.out.println("\nSend OnSelectTrigger " + ost1);
		runtime.sendEvent(ost1);

		OnDeleteTrigger ost2 = new OnDeleteTrigger();
		ost2.setTrigger(2);
		System.out.println("\nSend OnDeleteTrigger " + ost2);
		runtime.sendEvent(ost2);

		OnDeleteTrigger ost3 = new OnDeleteTrigger();
		ost3.setTrigger(0);
		System.out.println("\nSend OnSelectTrigger " + ost3);
		runtime.sendEvent(ost3);
	}
}
