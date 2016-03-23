import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
/**
* <h1>OrderTableTest</h1>
* This class consists of different JUnit cases to test several methods of OrderTable class
*/
public class TableOrdersTest {

	@Test
	public void testFindByID() throws InvalidPositiveInteger, NoMatchingOrderID {
		TableOrders ot = new TableOrders();
		ListOfOrders o1 = new ListOfOrders(1, "Chicken", 2);
		String id1 = o1.getOrderID();
		ListOfOrders o2 = new ListOfOrders(1, "Lasagna", 1);
		String id2 = o2.getOrderID();
		ListOfOrders o3 = new ListOfOrders(1, "Tuna", 4);
		String id3 = o3.getOrderID();
		ot.addOrder(o1);
		ot.addOrder(o2);
		ot.addOrder(o3);
		assertEquals(o1,ot.findByID(id1));
		assertEquals(o2,ot.findByID(id2));
		assertEquals(o3,ot.findByID(id3));
		assertNull(ot.findByID(""));
	}

	@Test
	public void testFindByMenuItem() throws InvalidPositiveInteger {
		TableOrders ot = new TableOrders();
		ListOfOrders o1 = new ListOfOrders(1, "Chicken", 2);
		ListOfOrders o2 = new ListOfOrders(1, "Lasagna", 1);
		ListOfOrders o3 = new ListOfOrders(1, "Tuna", 4);
		ListOfOrders o4 = new ListOfOrders(2, "Tuna", 2);
		ot.addOrder(o1);
		ot.addOrder(o2);
		ot.addOrder(o3);
		ot.addOrder(o4);
		assertEquals(2, ot.findByMenuItem("Tuna").size());
		ArrayList<ListOfOrders> arr = ot.findByMenuItem("Chicken");
		assertEquals(1,arr.size());
		assertEquals(o1, arr.get(0));
	}

	@Test
	public void testAddOrder() throws InvalidPositiveInteger, NoMatchingOrderID {
		TableOrders ot = new TableOrders();
		ListOfOrders o1 = new ListOfOrders(1, "Chicken", 2);
		ListOfOrders o2 = new ListOfOrders(1, "Lasagna", 1);
		ListOfOrders o3 = new ListOfOrders(1, "Tuna", 4);
		ListOfOrders o4 = new ListOfOrders(2, "Tuna", 2);
		assertEquals(0,ot.getNumberOfOrders());
		ot.addOrder(o1);
		assertEquals(1,ot.getNumberOfOrders());
		ot.addOrder(o2);
		assertTrue(ot.findByMenuItem("Lasagna").contains(o2));
		ot.addOrder(o3);
		ot.addOrder(o4);
		assertEquals(o3,ot.findByID(o3.getOrderID()));
		ListOfOrders o5 = new ListOfOrders(1, "Tuna", 4);
		ot.addOrder(o5);
		assertEquals(4,ot.getNumberOfOrders());
	}

	@Test
	public void testRemoveOrder() throws InvalidPositiveInteger, NoMatchingOrderID {
		TableOrders ot = new TableOrders();
		ListOfOrders o1 = new ListOfOrders(1, "Chicken", 2);
		ListOfOrders o2 = new ListOfOrders(1, "Lasagna", 1);
		ListOfOrders o3 = new ListOfOrders(1, "Tuna", 4);
		ot.addOrder(o1);
		ot.addOrder(o2);
		assertEquals(2,ot.getNumberOfOrders());
		ot.addOrder(o3);
		ot.removeOrder(o1.getOrderID());
		assertEquals(2,ot.getNumberOfOrders());
		ot.removeOrder(o2.getOrderID());
		assertEquals(1,ot.getNumberOfOrders());
	}

	@Test
	public void testGetNumberOfOrders() throws InvalidPositiveInteger, NoMatchingOrderID {
		TableOrders ot = new TableOrders();
		ListOfOrders o1 = new ListOfOrders(1, "Chicken", 2);
		ListOfOrders o2 = new ListOfOrders(1, "Lasagna", 1);
		ListOfOrders o3 = new ListOfOrders(1, "Tuna", 4);
		ListOfOrders o4 = new ListOfOrders(2, "Tuna", 2);
		assertEquals(0,ot.getNumberOfOrders());
		ot.addOrder(o1);
		assertEquals(1,ot.getNumberOfOrders());
		ot.addOrder(o2);
		assertEquals(2,ot.getNumberOfOrders());
		ot.addOrder(o3);
		ot.addOrder(o4);
		assertEquals(4,ot.getNumberOfOrders());
		ot.removeOrder(o4.getOrderID());
		assertEquals(3,ot.getNumberOfOrders());
	}

}
