package example;

import java.util.HashMap;
import java.util.Map;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import example.model.Product;

/**
 * 某个应用的性能数据（例如TPS、平均响应时间、mq队列深度、cpu、内存等）的值，在某个固定时间间隔内其值持续 大于最大门限值或小于最小门限值，则报警。
 * 
 * @author luonq(luonq@primeton.com)
 *
 */
class StdCountListener implements UpdateListener {

	private int num = 0;
	private EPRuntime runtime;
	private int n = 0;

	public StdCountListener(EPRuntime runtime) {
		this.runtime = runtime;
	}

	public synchronized void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			long temp = (Long) newEvents[0].get("prevcount(s)");
			if (temp > n) {
				n = (int) temp;
			}
		}
		if (oldEvents != null) {
			Map<String, Object> event = new HashMap<String, Object>();
			event.put("id", num);
			event.put("count", n);
			runtime.sendEvent(event, "S");

			num++;
			int count = oldEvents.length;
			n -= count;
		}
	}
}

class ActCountListener implements UpdateListener {

	private int num = 0;
	private EPRuntime runtime;
	private Integer n = 0;

	public ActCountListener(EPRuntime runtime) {
		this.runtime = runtime;
	}

	public synchronized void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			long temp = (Long) newEvents[0].get("prevcount(a)");
			if (temp > n) {
				n = (int) temp;
			}
		}
		if (oldEvents != null) {
			Map<String, Object> event = new HashMap<String, Object>();
			event.put("id", num);
			event.put("count", n);
			runtime.sendEvent(event, "A");

			num++;
			int oldCount = oldEvents.length;
			n -= oldCount;
		}
	}
}

class Result2Listener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println("Clarm!!!!");
		}
	}
}

public class MaxValueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		Map<String, Object> mapDef = new HashMap<String, Object>();
		mapDef.put("id", int.class);
		mapDef.put("count", int.class);

		admin.getConfiguration().addEventType("S", mapDef);
		admin.getConfiguration().addEventType("A", mapDef);

		EPRuntime runtime = epService.getEPRuntime();

		String product = Product.class.getName();

		String stdCount = "select irstream *, prevcount(s) from " + product + ".win:time(10 sec) as s";
		String actCount = "select irstream *, prevcount(a) from " + product + "(price > 0).win:time(10 sec) as a";
		String result = "every (a=S -> b=A(a.id=b.id and a.count=b.count)) or every (b=A -> a=S(a.id=b.id and a.count=b.count))";

		EPStatement state1 = admin.createEPL(stdCount);
		state1.addListener(new StdCountListener(runtime));
		EPStatement state2 = admin.createEPL(actCount);
		state2.addListener(new ActCountListener(runtime));
		EPStatement state3 = admin.createPattern(result);
		state3.addListener(new Result2Listener());

		Product esb = new Product();
		esb.setPrice(1);
		esb.setType("esb");
		runtime.sendEvent(esb);

		Product eos = new Product();
		eos.setPrice(2);
		eos.setType("eos");
		runtime.sendEvent(eos);

		Product esb1 = new Product();
		esb1.setPrice(2);
		esb1.setType("esb");
		runtime.sendEvent(esb1);

		Product eos1 = new Product();
		eos1.setPrice(5);
		eos1.setType("eos");
		runtime.sendEvent(eos1);

		Product esb2 = new Product();
		esb2.setPrice(3);
		esb2.setType("esb");
		runtime.sendEvent(esb2);

		Product eos3 = new Product();
		eos3.setPrice(6);
		eos3.setType("eos");
		runtime.sendEvent(eos3);

		System.out.println();
	}

}
