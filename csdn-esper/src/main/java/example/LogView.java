package example;

import java.util.Iterator;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.view.ViewSupport;

public class LogView extends ViewSupport {

	public void update(EventBean[] newData, EventBean[] oldData) {

	}

	public EventType getEventType() {
		return null;
	}

	public Iterator<EventBean> iterator() {
		return null;
	}

}
