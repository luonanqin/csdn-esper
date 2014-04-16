package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 3/19/14.
 */
class SelectWindowEvent {

	private int price;
	private String name;

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

	@Override
	public String toString() {
		return "name=" + name + ", price=" + price;
	}
}

class SelectNamedWindowListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println("There is " + newEvents.length + " events to be return!");
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}

public class SelectNamedWindowTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String selectEvent = SelectWindowEvent.class.getName();

		String epl1 = "create window SelectNamedWindow.win:length_batch(3) as " + selectEvent;
		admin.createEPL(epl1);
		System.out.println("Create named window: create window SelectNamedWindow.win:length_batch(3) as " + selectEvent);
		String epl2 = "insert into SelectNamedWindow select * from " + selectEvent;
		admin.createEPL(epl2);

		SelectWindowEvent se1 = new SelectWindowEvent();
		se1.setName("se1");
		se1.setPrice(1);
		System.out.println("Send SelecEvent1 " + se1);
		runtime.sendEvent(se1);

		SelectWindowEvent se2 = new SelectWindowEvent();
		se2.setName("se2");
		se2.setPrice(2);
		System.out.println("Send SelecEvent2 " + se2);
		runtime.sendEvent(se2);

		// Can't create "select * from SelectamedWindow.win:time(3 sec)"
		String epl3 = "select * from SelectNamedWindow(price>=2)";
		EPStatement state3 = admin.createEPL(epl3);
		state3.addListener(new SelectNamedWindowListener());
		System.out.println("Register select sentence: select * from SelectNamedWindow(price>=2)");

		SelectWindowEvent se3 = new SelectWindowEvent();
		se3.setName("se3");
		se3.setPrice(3);
		System.out.println("Send SelecEvent3 " + se3 + "\n");
		runtime.sendEvent(se3);
	}
}
