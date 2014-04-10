package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPOnDemandQueryResult;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EventBean;

import java.io.Serializable;

/**
 * Created by Luonanqin on 4/9/14.
 */
class SelectEvent implements Serializable {
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

public class SelectWindowTest {
	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String selectEvent = SelectEvent.class.getName();

		String epl1 = "create window SelectWindow.win:keepall() as select * from " + selectEvent;
		String epl2 = "insert into SelectWindow select * from " + selectEvent;

		admin.createEPL(epl1);
		admin.createEPL(epl2);

		SelectEvent se1 = new SelectEvent();
		se1.setName("se1");
		se1.setSize(1);
		runtime.sendEvent(se1);
		System.out.println("Send SelectEvent 1: " + se1);

		SelectEvent se2 = new SelectEvent();
		se2.setName("se2");
		se2.setSize(2);
		runtime.sendEvent(se2);
		System.out.println("Send SelectEvent 2: " + se2);

		String select = "select * from SelectWindow";
		String update = "update SelectWindow set name='update1' where size = 2";
		String delete = "delete from SelectWindow where size < 2";

		System.out.println("\nSelect SelectWindow!");
		EPOnDemandQueryResult selectResult = epService.getEPRuntime().executeQuery(select);
		EventBean[] events = selectResult.getArray();
		for (int i = 0; i < events.length; i++) {
			System.out.println(events[i].getUnderlying());
		}

		// 更新size=2的事件，将name改为'update1'
		System.out.println("\nUpdate SelectEvent(size = 2) in SelectWindow!");
		EPOnDemandQueryResult updateResult = epService.getEPRuntime().executeQuery(update);
		events = updateResult.getArray();
		for (int i = 0; i < events.length; i++) {
			System.out.println(events[i].getUnderlying());
		}

		System.out.println("\nSelect SelectWindow!");
		selectResult = epService.getEPRuntime().executeQuery(select);
		events = selectResult.getArray();
		for (int i = 0; i < events.length; i++) {
			System.out.println(events[i].getUnderlying());
		}

		// 删除size<2的事件
		System.out.println("\nDelete SelectEvent(size < 2) in SelectWindow!");
		EPOnDemandQueryResult deleteResult = epService.getEPRuntime().executeQuery(delete);
		events = deleteResult.getArray();
		for (int i = 0; i < events.length; i++) {
			System.out.println(events[i].getUnderlying());
		}

		System.out.println("\nSelect SelectWindow!");
		selectResult = epService.getEPRuntime().executeQuery(select);
		events = selectResult.getArray();
		for (int i = 0; i < events.length; i++) {
			System.out.println(events[i].getUnderlying());
		}
	}
}
