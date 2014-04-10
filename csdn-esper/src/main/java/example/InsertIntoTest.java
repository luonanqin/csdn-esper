package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import example.model.Product;

/**
 * insert into子句需要最先定义，AllEvent表示select出来的内容被封装成此名字代表的事件，其中包含的p和t对应Product事件的price和type属性值
 * 监听器监听epl2，用于查看AllEvent的事件输入结果，当然也可以监听epl1的select内容。epl2的细节请参见StandardGroupWinTest例子
 * 
 * @author luonanqin
 *
 */
public class InsertIntoTest {

	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String product = Product.class.getName();
		String epl1 = "insert into AllEvent(p,t) select price, type from " + product;
		String epl2 = "select sum(p), t from AllEvent.std:groupwin(t).win:length_batch(2) group by t";

		admin.createEPL(epl1);
		EPStatement state = admin.createEPL(epl2);
		state.addListener(new InsertIntoListener());

		EPRuntime runtime = epService.getEPRuntime();

		Product esb = new Product();
		esb.setPrice(1);
		esb.setType("esb");
		System.out.println("sendEvent: " + esb);
		runtime.sendEvent(esb);

		Product eos = new Product();
		eos.setPrice(2);
		eos.setType("eos");
		System.out.println("sendEvent: " + eos);
		runtime.sendEvent(eos);

		Product esb1 = new Product();
		esb1.setPrice(2);
		esb1.setType("esb");
		System.out.println("sendEvent: " + esb1);
		runtime.sendEvent(esb1);

		Product eos1 = new Product();
		eos1.setPrice(5);
		eos1.setType("eos");
		System.out.println("sendEvent: " + eos1);
		runtime.sendEvent(eos1);
	}
}
