package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import example.model.contained.Book;
import example.model.contained.Books;
import example.model.contained.Item;
import example.model.contained.Items;
import example.model.contained.MediaOrder;
import example.model.contained.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luonanqin on 7/30/14.
 */
class JoinContainedListener implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			for (int i = 0; i < newEvents.length; i++) {
				if (newEvents[i] == null) {
					continue;
				}
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}

public class JoinContainedEventTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		Review r1 = new Review();
		r1.setReviewId(1);
		r1.setComment("r1");

		Book b1 = new Book();
		b1.setAuthor("b1");
		b1.setBookId(1);
		b1.setReview(r1);

		Book b2 = new Book();
		b2.setAuthor("b2");
		b2.setBookId(2);
		b2.setReview(new Review());

		Item i1 = new Item();
		i1.setItemId(1);
		i1.setProductId(1);
		i1.setPrice(1.11);
		i1.setAmount(2);

		Item i2 = new Item();
		i2.setItemId(3);
		i2.setProductId(3);
		i2.setPrice(3.11);
		i2.setAmount(5);

		MediaOrder mo1 = new MediaOrder();
		Books bs = new Books();
		Items is = new Items();
		List<Item> items = new ArrayList<Item>();
		List<Book> books = new ArrayList<Book>();
		items.add(i1);
		items.add(i2);
		books.add(b1);
		books.add(b2);
		mo1.setOrderId(1);
		bs.setBook(books);
		is.setItem(items);
		mo1.setItems(is);
		mo1.setBooks(bs);

		String mediaOrder = MediaOrder.class.getName();
		String join1 = "select book.bookId, item.itemId from " + mediaOrder + "[books.book] as book, " + mediaOrder
				+ "[items.item] as item where productId = bookId";

		EPStatement stat1 = admin.createEPL(join1);
		stat1.addListener(new JoinContainedListener());

		System.out.println("EPL1: " + join1);
		runtime.sendEvent(mo1);
		stat1.destroy();
		System.out.println();

		String join2 = "select book.bookId, item.itemId from " + mediaOrder + "[books.book] as book left outer join " + mediaOrder
		  + "[items.item] as item on productId = bookId";

		EPStatement stat2 = admin.createEPL(join2);
		stat2.addListener(new JoinContainedListener());

		System.out.println("EPL2: " + join2);
		runtime.sendEvent(mo1);
		stat2.destroy();
		System.out.println();

		String join3 = "select book.bookId, item.itemId from " + mediaOrder + "[books.book] as book full outer join " + mediaOrder
		  + "[items.item] as item on productId = bookId";

		EPStatement stat3 = admin.createEPL(join3);
		stat3.addListener(new JoinContainedListener());

		System.out.println("EPL3: " + join3);
		runtime.sendEvent(mo1);
		stat3.destroy();
		System.out.println();

		String join4 = "select count(*) from " + mediaOrder + "[books.book] as book, " + mediaOrder
		  + "[items.item] as item where productId = bookId";

		EPStatement stat4 = admin.createEPL(join4);
		stat4.addListener(new JoinContainedListener());

		System.out.println("EPL4: " + join4);
		runtime.sendEvent(mo1);
		runtime.sendEvent(mo1);
		stat4.destroy();
		System.out.println();

		String join5 = "select count(*) from " + mediaOrder + "[books.book] as book unidirectional, " + mediaOrder
		  + "[items.item] as item where productId = bookId";

		EPStatement stat5 = admin.createEPL(join5);
		stat5.addListener(new JoinContainedListener());

		System.out.println("EPL5: " + join5);
		runtime.sendEvent(mo1);
		runtime.sendEvent(mo1);
		stat5.destroy();
		System.out.println();
	}
}
