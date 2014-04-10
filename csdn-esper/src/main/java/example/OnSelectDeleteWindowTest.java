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
class OnSelectDeleteTrigger {
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

class OnSelectDeleteEvent {
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

class OnSelectDeleteWindowListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println();
			System.out.println("Trigger On Select and Delete:");
			System.out.println("There is " + newEvents.length + " OnSelectDeleteEvent in OnSelectDeleteWindow!");
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}

public class OnSelectDeleteWindowTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();


		String triggerEvent = OnSelectDeleteTrigger.class.getName();
		String selectDeleteEvent = OnSelectDeleteEvent.class.getName();

		String epl1 = "create window OnSelectDeleteWindow.win:keepall() as select * from " + selectDeleteEvent;
		String epl2 = "insert into OnSelectDeleteWindow select * from " + selectDeleteEvent;
		String epl3 = "on " + triggerEvent + " select and delete osw.* from OnSelectDeleteWindow as osw";

		System.out.println("Create Window:" + epl1);
		System.out.println("Trigger sentence: " + epl3);
		System.out.println();

		admin.createEPL(epl1);
		admin.createEPL(epl2);
		EPStatement state3 = admin.createEPL(epl3);
		state3.addListener(new OnSelectDeleteWindowListener());

		OnSelectDeleteEvent osde1 = new OnSelectDeleteEvent();
		osde1.setName("osde1");
		osde1.setSize(1);
		runtime.sendEvent(osde1);
		System.out.println("Send OnSelectDeleteEvent 1: " + osde1);

		OnSelectDeleteEvent osde2 = new OnSelectDeleteEvent();
		osde2.setName("osde2");
		osde2.setSize(2);
		runtime.sendEvent(osde2);
		System.out.println("Send OnSelectDeleteEvent 2: " + osde2);

		OnSelectDeleteEvent osde3 = new OnSelectDeleteEvent();
		osde3.setName("osde3");
		osde3.setSize(3);
		runtime.sendEvent(osde3);
		System.out.println("Send OnSelectDeleteEvent 3: " + osde3);

		OnSelectDeleteTrigger osdt1 = new OnSelectDeleteTrigger();
		osdt1.setTrigger(1);
		System.out.println("Send OnSelectDeleteTrigger " + osdt1);
		runtime.sendEvent(osdt1);

		System.out.println();

		OnSelectDeleteEvent osde4 = new OnSelectDeleteEvent();
		osde4.setName("osde4");
		osde4.setSize(4);
		System.out.println("Send OnSelectDeleteEvent 4: " + osde4);
		runtime.sendEvent(osde4);

		OnSelectDeleteTrigger osdt2 = new OnSelectDeleteTrigger();
		osdt2.setTrigger(1);
		System.out.println("Send OnSelectDeleteTrigger " + osdt2);
		runtime.sendEvent(osdt2);
	}
}
