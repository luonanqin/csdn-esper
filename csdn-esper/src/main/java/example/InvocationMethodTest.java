package example;

import java.util.Iterator;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 2/16/14.
 */

class Times {
	private int times;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
}

class InvocationMethodListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println(newEvents[0].getUnderlying());
			System.out.println(newEvents[1].getUnderlying());
		}
	}
}

public class InvocationMethodTest {
	public static void main(String arg[]) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPRuntime runtime = epService.getEPRuntime();
		EPAdministrator admin = epService.getEPAdministrator();

		/**
		 * 调用外部方法返回Java类数据
		 */
		String timesName = Times.class.getName();
		String ijName = InvocationMethodJava.class.getName();
		String epl1 = "select ij.* from " + timesName + " as t, method:" + ijName + ".getJavaObject(times) as ij";
		System.out.println(epl1+"\n");

		EPStatement state1 = admin.createEPL(epl1);
		state1.addListener(new InvocationMethodListener());

		Times times = new Times();
		times.setTimes(2);

		runtime.sendEvent(times);

		System.out.println("");

		/**
		 * 调用外部方法返回Map类型数据
		 */
		String imName = InvocationMethodMap.class.getName();
		String epl2 = "select * from method:" + imName + ".getMapObject()";
		System.out.println(epl2+"\n");

		EPStatement state2 = admin.createEPL(epl2);
		Iterator<EventBean> iter = state2.iterator();
		while (iter.hasNext()) {
			EventBean event = iter.next();
			System.out.println(event.getUnderlying());
		}
	}
}
