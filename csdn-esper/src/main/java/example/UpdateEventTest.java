package example;

import java.io.Serializable;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 7/31/14.
 */
class UpdateEvent implements Serializable {

	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "UpdateEvent{" + "id=" + id + ", name='" + name + '\'' + '}';
	}
}

class UpdateEventListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println("newEvent: " + newEvents[i].getUnderlying());
			}
		}
		if (oldEvents != null) {
			for (int i = 0; i < oldEvents.length; i++) {
				System.out.println("oldEvent: " + oldEvents[i].getUnderlying());
			}
		}
	}
}

public class UpdateEventTest {

	public static void main(String[] args) {
		/**
		 * config:
		 *
		 * <engine-settings>
		 *		<defaults>
		 *			<execution prioritized="true"/>
		 *		</defaults>
		 *	</engine-settings>
		 */
		Configuration config = new Configuration();
		config.configure("esper.examples.cfg.xml");
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();
		UpdateEventListener listener = new UpdateEventListener();

		UpdateEvent ue = new UpdateEvent();
		ue.setId(1);
		ue.setName("luonanqin");

		UpdateEvent ue2 = new UpdateEvent();
		ue2.setId(2);
		ue2.setName("qinnanluo");

		String updateEvent = UpdateEvent.class.getName();
		String select = "select * from " + updateEvent;
		String update1 = "@Priority(2)update istream " + updateEvent + " set name='qnoul' where id = 1";
		String update2 = "@Priority(1)update istream " + updateEvent + " set name='luonq' where id = 1";

		EPStatement stat1 = admin.createEPL(select);
		stat1.addListener(listener);
		System.out.println("select EPL: " + select);

		runtime.sendEvent(ue);
		runtime.sendEvent(ue2);
		System.out.println();

		EPStatement stat2 = admin.createEPL(update1);
		stat2.addListener(listener);
		System.out.println("update1 EPL: " + update1);

		runtime.sendEvent(ue);
		runtime.sendEvent(ue2);
		System.out.println();

		EPStatement stat3 = admin.createEPL(update2);
		stat3.addListener(listener);
		System.out.println("update2 EPL: " + update2);

		runtime.sendEvent(ue);
		runtime.sendEvent(ue2);
	}
}
