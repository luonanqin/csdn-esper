package example;

import java.util.HashMap;
import java.util.Map;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

/**
 * 展示如何定义map事件和处理方式
 * 
 * @author luonq(luonq@primeton.com)
 *
 */
public class MapProcessTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();
		Map<String, Object> mapDef = new HashMap<String, Object>();
		mapDef.put("id", int.class);
		mapDef.put("count", int.class);
		admin.getConfiguration().addEventType("preSumIdEvent", mapDef);

		String epl1 = "select sum(count) as sumCount, id from preSumIdEvent.std:groupwin(id).win:length(2)";

		EPStatement state = admin.createEPL(epl1);
		state.addListener(new MapProcessListener());

		EPRuntime runtime = epService.getEPRuntime();

		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("id", 1);
		map1.put("count", 1);
		runtime.sendEvent(map1, "preSumIdEvent");

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", 2);
		map2.put("count", 4);
		runtime.sendEvent(map2, "preSumIdEvent");

		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("id", 2);
		map3.put("count", 3);
		runtime.sendEvent(map3, "preSumIdEvent");

		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("id", 1);
		map4.put("count", 3);
		runtime.sendEvent(map4, "preSumIdEvent");

	}
}
