package example;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import example.model.User;


public class ViewListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			Object users = newEvents[0].get("prevwindow(user)");
			for (User u : (User[]) users) {
				System.out.print(u + " ");
			}
			System.out.println();
		}
	}

}
