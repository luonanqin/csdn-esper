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
 * Created by Luonanqin on 5/3/14.
 */
class SelectContainedListener implements UpdateListener {

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

public class SelectContainedEventTest {

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

		MediaOrder mo1 = new MediaOrder();
		Books bs = new Books();
		Items is = new Items();
		List<Item> items = new ArrayList<Item>();
		List<Book> books = new ArrayList<Book>();
		items.add(i1);
		books.add(b1);
		books.add(b2);
		mo1.setOrderId(1);
		bs.setBook(books);
		is.setItem(items);
		mo1.setItems(is);
		mo1.setBooks(bs);

		String mediaOrder = MediaOrder.class.getName();
		String epl1 = "select * from " + mediaOrder + "[books.book]";
		EPStatement stat1 = admin.createEPL(epl1);
		stat1.addListener(new SelectContainedListener());

		System.out.println("EPL1: " + epl1);
		runtime.sendEvent(mo1);
		stat1.destroy();
		System.out.println();

		String epl2 = "select * from " + mediaOrder + "[books.book][review]";
		EPStatement stat2 = admin.createEPL(epl2);
		stat2.addListener(new SelectContainedListener());

		System.out.println("EPL2: " + epl2);
		runtime.sendEvent(mo1);
		stat2.destroy();
		System.out.println();

		// valid: it's return bookId and orderId
		// String epl3 = "select * from " + mediaOrder + "[select bookId, orderId from books.book]";

		String epl3 = "select * from " + mediaOrder + "[select bookId, orderId from books.book][select * from review]";
		EPStatement stat3 = admin.createEPL(epl3);
		stat3.addListener(new SelectContainedListener());

		System.out.println("EPL3: " + epl3);
		runtime.sendEvent(mo1);
		stat3.destroy();
		System.out.println();

		String epl4 = "select * from " + mediaOrder + "[books.book][select bookId, orderId, reviewId from review]";

		// not valid: reviewId is not from book, and it's from review
		// String epl4 = "select * from " + mediaOrder + "[select bookId, orderId, reviewId from books.book][review]";

		// not valid: orderId is not in select clause of Contained-Event Selection
		// String epl4 = "select orderId from " + mediaOrder + "[select bookId from books.book][review]";
		EPStatement stat4 = admin.createEPL(epl4);
		stat4.addListener(new SelectContainedListener());

		System.out.println("EPL4: " + epl4);
		runtime.sendEvent(mo1);
		stat4.destroy();
		System.out.println();
	}
}
