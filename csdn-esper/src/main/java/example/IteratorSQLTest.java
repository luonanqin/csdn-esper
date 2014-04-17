package example;

import java.util.Iterator;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;

/**
 * Created by Luonanqin on 4/17/14.
 */
public class IteratorSQLTest {

	public static void main(String[] args) throws InterruptedException {
		Configuration config = new Configuration();
		config.configure("esper.examples.cfg.xml");
		config.addVariable("vari", Integer.class, 1);
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);

		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();
		// id=1, name="luonq"
		String epl1 = "select id, name from sql:test['select id, name from test1 where id=${vari}']";

		EPStatement state = admin.createEPL(epl1);

		Iterator<EventBean> iter = state.iterator(); // 也可以调用safeIterator方法，该方法以线程安全方式查询DB
		while (iter.hasNext()) {
			EventBean eventBean = iter.next();
			System.out.println(eventBean.get("id") + " " + eventBean.get("name"));
		}
	}
}
