package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import example.model.Product;

/**
 * avg函数用于计算view里包含的事件的平均值，具体计算的平均值是由avg里的内容决定的
 * 以如下view所示，如果为length_batch，则当batch满足数量并触发监听器时，只计算view里的平均price，不会将oldEvent计算在内
 * 如果为length，则请参看AverageTest例子
 * 
 * @author luonanqin
 *
 */
public class AverageBatchTest {

	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String product = Product.class.getName();
		String epl1 = "select avg(price) from " + product + ".win:length_batch(2)";

		EPStatement state = admin.createEPL(epl1);
		state.addListener(new AverageBatchListener());

		EPRuntime runtime = epService.getEPRuntime();

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
	}
}
