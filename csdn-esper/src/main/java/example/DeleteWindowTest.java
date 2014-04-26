package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPOnDemandQueryResult;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EventBean;

/**
 * Created by Luonanqin on 4/8/14.
 */
class DeleteEvent {
	private String name;
	private int size;

	public String getName() {
		return name;
	}

	public void detName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void detSize(int size) {
		this.size = size;
	}

	public String toString() {
		return "name=" + name + ", size=" + size;
	}
}

public class DeleteWindowTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String deleteEvent = DeleteEvent.class.getName();

		String epl1 = "create window DeleteWindow.win:keepall() as select * from " + deleteEvent;
		String epl2 = "insert into DeleteWindow select * from " + deleteEvent;

		admin.createEPL(epl1);
		admin.createEPL(epl2);

		DeleteEvent de1 = new DeleteEvent();
		de1.detName("de1");
		de1.detSize(1);
		runtime.sendEvent(de1);
		System.out.println("Send DeleteEvent 1: " + de1);

		DeleteEvent de2 = new DeleteEvent();
		de2.detName("de2");
		de2.detSize(2);
		runtime.sendEvent(de2);
		System.out.println("Send DeleteEvent 2: " + de2);

		String delect = "select * from DeleteWindow";
		String delete = "Delete from DeleteWindow where size < 2";

		System.out.println("\nSelect DeleteWindow!");
		EPOnDemandQueryResult result1 = epService.getEPRuntime().executeQuery(delect);
		EventBean[] events = result1.getArray();
		for (int i = 0; i < events.length; i++) {
			System.out.println(events[i].getUnderlying());
		}

		System.out.println("\nDelete DeleteEvent(size < 2) from DeleteWindow!");
		EPOnDemandQueryResult result2 = epService.getEPRuntime().executeQuery(delete);
		events = result2.getArray();
		for (int i = 0; i < events.length; i++) {
			System.out.println(events[i].getUnderlying());
		}

		System.out.println("\nSelect DeleteWindow!");
		result1 = epService.getEPRuntime().executeQuery(delect);
		events = result1.getArray();
		for (int i = 0; i < events.length; i++) {
			System.out.println(events[i].getUnderlying());
		}
	}
}
