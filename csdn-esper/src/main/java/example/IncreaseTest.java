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
import example.model.User;

/**
 * 某个应用的性能数据（例如TPS、平均响应时间、mq队列深度、cpu、内存等）的值，在某个固定时间间隔内其值以某个百分数递增，或百分数递减，则报警。
 * 
 * @author luonq(luonq@primeton.com)
 *
 */
class InputListener implements UpdateListener {

	private EPRuntime runtime;
	private Product[] ps = new Product[0];
	private int count = 0;

	public InputListener(EPRuntime runtime) {
		this.runtime = runtime;
	}

	public synchronized void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			ps = (Product[]) newEvents[0].get("prevwindow(i)");
			count = ps.length;
		}
		if (oldEvents != null) {
			for (int i = count - 1; i >= 0; i--) {
				Map<String, Object> event = new HashMap<String, Object>();
				event.put("p", ps[i]);
				runtime.sendEvent(event, "S");
			}

			int last = ps.length - oldEvents.length;
			Product[] temp = new Product[last];
			for (int i = 0; i < last; i++) {
				temp[i] = ps[i];
			}
			ps = temp;

			User u = new User();
			runtime.sendEvent(u);

			Map<String, Object> scount = new HashMap<String, Object>();
			scount.put("count", count);
			runtime.sendEvent(scount, "Scount");

			count -= oldEvents.length;
		}
	}
}

class AcountListener implements UpdateListener {

	private EPRuntime runtime;

	public AcountListener(EPRuntime runtime) {
		this.runtime = runtime;
	}

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			long count = (Long) newEvents[0].get("count(*)");
			Map<String, Object> acount = new HashMap<String, Object>();
			acount.put("count", (int) count);
			runtime.sendEvent(acount, "Acount");
		}
	}
}

class Result3Listener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println("Clarm!!!!");
		}
	}
}

public class IncreaseTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		Map<String, Object> mapDef = new HashMap<String, Object>();
		mapDef.put("p", Product.class);

		Map<String, Object> mapDef1 = new HashMap<String, Object>();
		mapDef1.put("count", int.class);

		admin.getConfiguration().addEventType("S", mapDef);
		admin.getConfiguration().addEventType("Scount", mapDef1);
		admin.getConfiguration().addEventType("Acount", mapDef1);

		EPRuntime runtime = epService.getEPRuntime();

		String product = Product.class.getName();
		String close = User.class.getName();

		String input = "select irstream *, prevwindow(i) from " + product + ".win:time(5 sec) as i";
		String window = "create window ABC.win:keepall() as select * from S";
		String output = "insert into ABC select * from pattern[every a=S -[1]> b=S(b.p.price>=(a.p.price*1.05))]";
		String acount = "on " + close + " select and delete count(*) from ABC";
		String result = "every b=Acount -> every a=Scount(b.count+1=a.count)";

		EPStatement state1 = admin.createEPL(input);
		state1.addListener(new InputListener(runtime));
		admin.createEPL(window);
		admin.createEPL(output);
		EPStatement state2 = admin.createEPL(acount);
		state2.addListener(new AcountListener(runtime));
		EPStatement state3 = admin.createPattern(result);
		state3.addListener(new Result3Listener());

		Product esb = new Product();
		esb.setPrice(1);
		esb.setType("esb");
		runtime.sendEvent(esb);

		Product eos = new Product();
		eos.setPrice(2);
		eos.setType("eos");
		runtime.sendEvent(eos);

		Product esb1 = new Product();
		esb1.setPrice(3);
		esb1.setType("esb");
		runtime.sendEvent(esb1);

		Product eos1 = new Product();
		eos1.setPrice(5);
		eos1.setType("eos");
		runtime.sendEvent(eos1);

		Product esb2 = new Product();
		esb2.setPrice(6);
		esb2.setType("esb");
		runtime.sendEvent(esb2);

		Product eos3 = new Product();
		eos3.setPrice(7);
		eos3.setType("eos");
		runtime.sendEvent(eos3);

		System.out.println();
	}

}
