package example;

import java.io.Serializable;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPOnDemandQueryResult;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EventBean;

/**
 * Created by Luonanqin on 4/8/14.
 */
class UpdateWindow implements Serializable{
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

public class UpdateWindowTest {
	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String updateEvent = UpdateWindow.class.getName();

		String epl1 = "create window UpdateWindow.win:keepall() as select * from " + updateEvent;
		String epl2 = "insert into UpdateWindow select * from " + updateEvent;

		admin.createEPL(epl1);
		admin.createEPL(epl2);

		UpdateWindow ue1 = new UpdateWindow();
		ue1.setName("ue1");
		ue1.setSize(1);
		runtime.sendEvent(ue1);
		System.out.println("Send UpdateEvent 1: " + ue1);

		UpdateWindow ue2 = new UpdateWindow();
		ue2.setName("ue2");
		ue2.setSize(2);
		runtime.sendEvent(ue2);
		System.out.println("Send UpdateEvent 2: " + ue2);

		String select = "select * from UpdateWindow";
		String delete = "update UpdateWindow set name='update1' where size = 2";

		System.out.println("\nSelect UpdateWindow!");
		EPOnDemandQueryResult result1 = epService.getEPRuntime().executeQuery(select);
		EventBean[] events = result1.getArray();
		for (int i = 0; i < events.length; i++) {
			System.out.println(events[i].getUnderlying());
		}

		System.out.println("\nUpdate UpdateEvent(size = 2) in UpdateWindow!");
		EPOnDemandQueryResult result2 = epService.getEPRuntime().executeQuery(delete);
		events = result2.getArray();
		for (int i = 0; i < events.length; i++) {
			System.out.println(events[i].getUnderlying());
		}

		System.out.println("\nSelect UpdateWindow!");
		result1 = epService.getEPRuntime().executeQuery(select);
		events = result1.getArray();
		for (int i = 0; i < events.length; i++) {
			System.out.println(events[i].getUnderlying());
		}
	}
}
