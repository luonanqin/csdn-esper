package example;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * 
 * 
 * @author luonanqin
 */

class Student {
	private int sid;
	private String name;

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

class StudentListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println(newEvents.length);
		}
	}
}

public class AccessRDBMSTest {

	public static void main(String[] args) throws InterruptedException {
		Configuration config = new Configuration();
		config.configure("esper.examples.cfg.xml");
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);

		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();
		String epl1 = "select id, name from " + Student.class.getName() + ",sql:test['select id from test1 where id=${sid}']";
		System.out.println(epl1);
		EPStatement state1 = admin.createEPL(epl1);
		state1.addListener(new StudentListener());

		Student s1 = new Student();
		s1.setSid(1);
		s1.setName("name");
		runtime.sendEvent(s1);
	}
}
