package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 4/16/14.
 */
class Parent {

	private int id;
	public Child child;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public String toString() {
		return "Parent: " + id + ", Child: " + child;
	}
}

interface Child {

	public int getAge();
}

class XiaoMing implements Child {

	private int age;
	private String name;

	public void setAge(int age) {
		this.age = age;
	}

	public int getAge() {
		return age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "name=" + name + ", age=" + age;
	}
}

class PojoInterfaceEventTypeListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println("\nTrigger Listener!");
			for (int i = 0; i < newEvents.length; i++) {
				EventBean newEvent = newEvents[i];
				System.out.println(newEvent.getUnderlying());
			}
		}
	}
}

public class PojoInterfaceEventTypeTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String parent = Parent.class.getName();
		String epl1 = "select * from " + parent + "(child.age > 3)";
		EPStatement state1 = admin.createEPL(epl1);
		state1.addListener(new PojoInterfaceEventTypeListener());

		System.out.println("EPL: " + epl1 + "\n");

		XiaoMing xm1 = new XiaoMing();
		xm1.setName("xm1");
		xm1.setAge(2);

		XiaoMing xm2 = new XiaoMing();
		xm2.setName("xm2");
		xm2.setAge(5);

		Parent p1 = new Parent();
		p1.setChild(xm1);
		p1.setId(1);
		System.out.println("Send Parent " + p1.getId() + ": child is " + xm1);
		runtime.sendEvent(p1);

		Parent p2 = new Parent();
		p2.setChild(xm2);
		p2.setId(2);
		System.out.println("Send Parent " + p2.getId() + ": child is " + xm2);
		runtime.sendEvent(p2);
	}
}
/* Result：
	EPL: select * from example.Parent(child.age > 3)

	Send Parent 1: child is name=xm1, age=2
	Send Parent 2: child is name=xm2, age=5

	Trigger Listener!
	Parent: 2, Child: name=xm2, age=5
 */
