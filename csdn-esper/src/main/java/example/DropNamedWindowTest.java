package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

/**
 * Created by Luonanqin on 4/10/14.
 */
class DropEvent {
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

public class DropNamedWindowTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String dropEvent = DropEvent.class.getName();

		String epl1 = "create window DropWindow.win:keepall() as select * from " + dropEvent;
		String epl2 = "insert into DropWindow select * from " + dropEvent;

		EPStatement state1 = admin.createEPL(epl1);
		admin.createEPL(epl2);

		SelectEvent se1 = new SelectEvent();
		se1.setName("se1");
		se1.setSize(1);
		runtime.sendEvent(se1);
		System.out.println("Send SelectEvent 1: " + se1);

		state1.destroy();

		SelectEvent se2 = new SelectEvent();
		se2.setName("se2");
		se2.setSize(2);
		runtime.sendEvent(se2);
		System.out.println("Send SelectEvent 2: " + se2);

		// 再次建立的named window结构和之前的不同，会抛异常
		// epl1 = "create window DropWindow.win:keepall() as select size from " + dropEvent;
		admin.createEPL(epl1);
	}
}
