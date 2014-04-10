package example;

import java.io.Serializable;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 4/5/14.
 */
class OnUpdateTrigger {
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

class OnUpdateEvent implements Serializable{
	private String name;
	private int size;
	private int price;

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

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
		return "name=" + name + ", size=" + size + ", price=" + price;
	}
}

class OnUpdateWindowListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println();
			System.out.println("Trigger On Update:");
			System.out.println("There is " + newEvents.length + " to be updated in OnUpdateWindow!");
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}

class OnUpdateSelectWindowListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println();
			System.out.println("Trigger On Select:");
			System.out.println("There is " + newEvents.length + " OnUpdateEvent in OnUpdateWindow!");
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}

public class OnUpdateWindowTest {
	public static void main(String[] args) {

		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String triggerEvent = OnUpdateTrigger.class.getName();
		String updateEvent = OnUpdateEvent.class.getName();

		String epl1 = "create window OnUpdateWindow.win:keepall() as select * from " + updateEvent;
		String epl2 = "insert into OnUpdateWindow select * from " + updateEvent;
		String epl3 = "on " + triggerEvent + "(trigger>0) as out update OnUpdateWindow as ouw set size=out.trigger, price=initial.size where out.trigger<ouw.price";
		String epl4 = "on " + triggerEvent + "(trigger=0) select ouw.* from OnUpdateWindow as ouw";

		System.out.println("Create Window: " + epl1);
		System.out.println("Update Trigger: " + epl3);
		System.out.println();

		admin.createEPL(epl1);
		admin.createEPL(epl2);
		EPStatement state3 = admin.createEPL(epl3);
		state3.addListener(new OnUpdateWindowListener());
		EPStatement state4 = admin.createEPL(epl4);
		state4.addListener(new OnUpdateSelectWindowListener());

		OnUpdateEvent oue1 = new OnUpdateEvent();
		oue1.setName("oue1");
		oue1.setSize(1);
		oue1.setPrice(2);
		runtime.sendEvent(oue1);
		System.out.println("Send OnUpdateEvent 1: " + oue1);

		OnUpdateEvent oue2 = new OnUpdateEvent();
		oue2.setName("oue2");
		oue2.setSize(2);
		oue2.setPrice(3);
		runtime.sendEvent(oue2);
		System.out.println("Send OnUpdateEvent 2: " + oue2);

		OnUpdateEvent oue3 = new OnUpdateEvent();
		oue3.setName("oue3");
		oue3.setSize(3);
		oue3.setPrice(4);
		runtime.sendEvent(oue3);
		System.out.println("Send OnUpdateEvent 3: " + oue3);

		OnUpdateTrigger ost1 = new OnUpdateTrigger();
		ost1.setTrigger(0);
		System.out.println("\nSend OnUpdateTrigger " + ost1);
		runtime.sendEvent(ost1);

		OnUpdateTrigger ost2 = new OnUpdateTrigger();
		ost2.setTrigger(2);
		System.out.println("\nSend OnUpdateTrigger " + ost2);
		runtime.sendEvent(ost2);

		OnUpdateTrigger ost3 = new OnUpdateTrigger();
		ost3.setTrigger(0);
		System.out.println("\nSend OnUpdateTrigger " + ost3);
		runtime.sendEvent(ost3);
	}
}
