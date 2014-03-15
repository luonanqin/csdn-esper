package example;

import com.espertech.esper.client.ConfigurationOperations;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

import example.model.ESB;
import example.model.Product;

/**
 * ConfigurationOperations可设置变量值或者常量值，然后通过EPRuntime提供的接口查询当前值
 * 
 * @author luonanqin
 *
 */
public class VariableTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String esb = ESB.class.getName();
		String context1 = "create context esbtest partition by id from " + esb;
		String epl1 = "context esbtest select avg(price) as aPrice from " + esb;

		admin.createEPL(context1, "context1");
		admin.createEPL(epl1, "epl1");

		ConfigurationOperations conf = admin.getConfiguration();
		conf.addEventType("product", Product.class);
		conf.addVariable("abc", String.class, "initVariable");
		conf.addVariable("constabc", int.class.getName(), 123, true);

		Object variableValue = epService.getEPRuntime().getVariableValue("abc");
		Object constValue = epService.getEPRuntime().getVariableValue("constabc");
		System.out.println("variable value: " + variableValue);
		System.out.println("const value: " + constValue);
	}

}
