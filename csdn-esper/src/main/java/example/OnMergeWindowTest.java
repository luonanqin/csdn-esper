package example;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 4/8/14.
 */
class MergeEvent implements Serializable {

	private int mergeId;
	private String mergeStr;
	private int mergeSize;
	private boolean deleteFlag;

	public int getMergeId() {
		return mergeId;
	}

	public void setMergeId(int mergeId) {
		this.mergeId = mergeId;
	}

	public String getMergeStr() {
		return mergeStr;
	}

	public void setMergeStr(String mergeStr) {
		this.mergeStr = mergeStr;
	}

	public int getMergeSize() {
		return mergeSize;
	}

	public void setMergeSize(int mergeSize) {
		this.mergeSize = mergeSize;
	}

	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String toString() {
		return "mergeId=" + mergeId + ", mergeStr=" + mergeStr + ", mergeSize=" + mergeSize + ", deleteFlag=" + deleteFlag;
	}
}

class OnMergeWindowlistener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println("Trigger MergeWindow:");
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}

class SelectLogWindowlistener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}

class SelectMergeWindowlistener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}

public class OnMergeWindowTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String mergeEvent = MergeEvent.class.getName();

		String epl1 = "create window MergeWindow.win:keepall() select * from " + mergeEvent;
		String epl2 = "create schema LogEvent as (id int, name string)";
		String epl3 = "create window LogWindow.win:keepall() as LogEvent";
		String epl4 = "on " + mergeEvent + "(mergeSize > 0) me merge MergeWindow mw where me.mergeId = mw.mergeId "
				+ "when matched and me.deleteFlag = true then delete "
				+ "when matched then update set mergeSize = mergeSize + me.mergeSize where mw.mergeSize > 3 "
				// MergeWindow中不存在的事件都会在触发merge时插入到window中，同时将部分属性插入到LogWindow中用于记录
				+ "when not matched then insert select * then insert into LogWindow(id, name) select me.mergeId, me.mergeStr";
		String epl5 = "on LogEvent(id=0) select lw.* from LogWindow as lw";
		String epl6 = "on " + mergeEvent + "(mergeSize = 0) select mw.* from MergeWindow as mw";

		System.out.println("Create Window: " + epl1);
		System.out.println("Merge Trigger: " + epl4);
		System.out.println();

		admin.createEPL(epl1);
		admin.createEPL(epl2);
		admin.createEPL(epl3);
		EPStatement state1 = admin.createEPL(epl4);
		state1.addListener(new OnMergeWindowlistener());
		EPStatement state2 = admin.createEPL(epl5);
		state2.addListener(new SelectLogWindowlistener());
		EPStatement state3 = admin.createEPL(epl6);
		state3.addListener(new SelectMergeWindowlistener());

		Map<String, Object> selectLog = new HashMap<String, Object>();
		selectLog.put("id", 0);

		MergeEvent selectMerge = new MergeEvent();
		selectMerge.setMergeSize(0);

		MergeEvent me1 = new MergeEvent();
		me1.setDeleteFlag(false);
		me1.setMergeId(1);
		me1.setMergeSize(2);
		me1.setMergeStr("me1");
		System.out.println("Send MergeEvent 1: " + me1);
		runtime.sendEvent(me1);

		MergeEvent me2 = new MergeEvent();
		me2.setDeleteFlag(false);
		me2.setMergeId(2);
		me2.setMergeSize(3);
		me2.setMergeStr("me2");
		System.out.println("Send MergeEvent 2: " + me2);
		runtime.sendEvent(me2);

		MergeEvent me3 = new MergeEvent();
		me3.setDeleteFlag(false);
		me3.setMergeId(3);
		me3.setMergeSize(4);
		me3.setMergeStr("me3");
		System.out.println("Send MergeEvent 3: " + me3);
		runtime.sendEvent(me3);

		/**
		 * 查询之前插入的三个事件
		 */
		System.out.println("\nSend MergeEvent to Select MergeWindow!");
		runtime.sendEvent(selectMerge);

		/**
		 * 查询LogWindow中记录的MergeEvent部分属性
		 */
		System.out.println("\nSend LogEvent to Select LogWindow!");
		runtime.sendEvent(selectLog, "LogEvent");

		/**
		 * 因为mergeId是3，所以MergeWindow中只有mergeId=3的事件有资格被更新。 并且mergeSize>3，所以可以执行更新操作。
		 */
		MergeEvent me4 = new MergeEvent();
		me4.setDeleteFlag(false);
		me4.setMergeId(3);
		me4.setMergeSize(5);
		me4.setMergeStr("me4");
		System.out.println("\nSend MergeEvent 4: " + me4);
		runtime.sendEvent(me4);

		System.out.println("\nSend MergeEvent to Select MergeWindow!");
		runtime.sendEvent(selectMerge);

		/**
		 * 因为mergeId是1，所以MergeWindow中只有mergeId=1的事件有资格被删除。 并且deleteFlag=true，所以mergeId=1的事件将从MergeWindow中移除
		 */
		MergeEvent me5 = new MergeEvent();
		me5.setDeleteFlag(true);
		me5.setMergeId(1);
		me5.setMergeSize(6);
		me5.setMergeStr("me5");
		System.out.println("\nSend MergeEvent 5: " + me5);
		runtime.sendEvent(me5);

		System.out.println("\nSend MergeEvent to Select MergeWindow!");
		runtime.sendEvent(selectMerge);
	}
}
