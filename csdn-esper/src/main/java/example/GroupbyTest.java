package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import example.model.Product;

/**
 * 分组统计每个产品的价格列表，并返回每个产品的最新价格
 * 
 * @author luonq(luonq@primeton.com)
 *
 */
public class GroupbyTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		// window只能用于stream的属性，不能用于stream，只有prevwindow可用于stream，详细可见SelectWindowEventTest例子
		String epl1 = "select type, window(price) as priceList, last(price) as lastPrice from " + Product.class.getName()
				+ ".win:length_batch(5) group by type";

		EPStatement state = admin.createEPL(epl1);
		state.addListener(new GroupbyListener());

		EPRuntime runtime = epService.getEPRuntime();

		Product p1 = new Product();
		p1.setType("esb");
		p1.setPrice(20);
		System.out.println("sendEvent: " + p1);
		runtime.sendEvent(p1);

		Product p2 = new Product();
		p2.setType("eos");
		p2.setPrice(30);
		System.out.println("sendEvent: " + p2);
		runtime.sendEvent(p2);

		Product p3 = new Product();
		p3.setType("esb");
		p3.setPrice(35);
		System.out.println("sendEvent: " + p3);
		runtime.sendEvent(p3);

		Product p4 = new Product();
		p4.setType("bps");
		p4.setPrice(40);
		System.out.println("sendEvent: " + p4);
		runtime.sendEvent(p4);

		Product p5 = new Product();
		p5.setType("esb");
		p5.setPrice(25);
		System.out.println("sendEvent: " + p5);
		runtime.sendEvent(p5);

	}
}
