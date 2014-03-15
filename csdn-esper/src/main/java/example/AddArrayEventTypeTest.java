package example;

import java.util.Arrays;

import com.espertech.esper.client.ConfigurationOperations;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EventType;

/**
 * 1.新增数组类EventBean需要输入事件名，包含的属性列表，及列表类型。列表和列表类型一一对应
 * 2.更新数组类EventBean为增量更新，只能增加属性，不能删除属性
 * 
 * @author luonanqin
 *
 */
public class AddArrayEventTypeTest {

	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();
		ConfigurationOperations config = admin.getConfiguration();

		config.addEventType("arrayTest", new String[] { "a", "b" }, new Object[] { String.class, int.class });
		EventType event = config.getEventType("arrayTest");
		System.out.println("Event Names: " + Arrays.asList(event.getPropertyNames()));

		config.updateObjectArrayEventType("arrayTest", new String[] { "c" }, new Object[] { long.class });
		event = config.getEventType("arrayTest");
		System.out.println("Event Names: " + Arrays.asList(event.getPropertyNames()));
	}
}
