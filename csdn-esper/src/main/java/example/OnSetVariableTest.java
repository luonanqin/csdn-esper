package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 4/26/14.
 */
class SetVariableEvent {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

class SetVariableListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			for (int i = 0; i < newEvents.length; i++) {
				EventBean newEvent = newEvents[i];
				System.out.println(newEvent.get("v"));
			}
		}
	}
}

public class OnSetVariableTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String variEvent = SetVariableEvent.class.getName();

		String epl1 = "create variable String Variable1 = 'Variable1'";
		String epl2 = "create variable int Variable2 = 2";
		String epl3 = "on " + variEvent + " as ve set Variable1 = ve.name, Variable2 = 3";

		admin.createEPL(epl1);
		admin.createEPL(epl2);
		admin.createEPL(epl3);
		EPStatement statement1 = admin.createEPL("select Variable1 as v from " + variEvent);
		statement1.addListener(new SetVariableListener());
		EPStatement statement2 = admin.createEPL("select Variable2 as v from " + variEvent);
		statement2.addListener(new SetVariableListener());

		SetVariableEvent ve = new SetVariableEvent();
		ve.setName("VariableEvent");
		runtime.sendEvent(ve);
	}
}
