import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TableOrders {

	private TreeMap<Integer, HashSet<ListOfOrders>> orderTable;

	private HashMap<String, Integer> frequency;

	public TableOrders() {
		orderTable = new TreeMap<Integer, HashSet<ListOfOrders>>();
		frequency = new HashMap<String, Integer>();
	}

	public TreeMap<Integer, HashSet<ListOfOrders>> getOrderTable() {
		return orderTable;
	}

	public HashMap<String, Integer> getFrequency() {
		return frequency;
	}

	public ListOfOrders findByID(String id) throws NoMatchingOrderID {
		if (validateOrderID(id)) {
			Collection<HashSet<ListOfOrders>> c = orderTable.values();
			Iterator<HashSet<ListOfOrders>> i = c.iterator();
			boolean success = false;
			while (i.hasNext() && !success) {
				Iterator<ListOfOrders> j = i.next().iterator();
				while (j.hasNext() && !success) {
					ListOfOrders o = j.next();
					if (o.getOrderID().equals(id))
						return o;
				}
			}
			if (!success)
				throw new NoMatchingOrderID(id);
		}
		return null;
	}

	public HashSet<ListOfOrders> findByTable(int table) {
		if (orderTable.containsKey(table))
			return orderTable.get(table);
		else {
			System.out.println("Table number " + table + " cannot be found");
			return null;
		}
	}

	public ArrayList<ListOfOrders> findByMenuItem(String item) {
		ArrayList<ListOfOrders> orders = new ArrayList<ListOfOrders>();
		Collection<HashSet<ListOfOrders>> c = orderTable.values();
		Iterator<HashSet<ListOfOrders>> i = c.iterator();
		boolean success = false;
		while (i.hasNext()) {
			Iterator<ListOfOrders> j = i.next().iterator();
			while (j.hasNext()) {
				ListOfOrders o = j.next();
				if (o.getItemName().equals(item)) {
					success = true;
					orders.add(o);
				}
			}
		}
		if (success)
			return orders;
		else {
			System.out.println("There is no orders containing '" + item + "'");
			return null;
		}
	}

	public boolean addOrder(ListOfOrders o) {
		Integer i = new Integer(o.getTableID());
		String itemName = o.getItemName();
		HashSet<ListOfOrders> set = orderTable.get(i);
		if (set == null)
			set = new HashSet<ListOfOrders>();
		if (set.add(o)) {
			orderTable.put(i, set);
			Integer value = frequency.putIfAbsent(itemName, o.getQuantity());
			if (value != null)
				frequency.replace(itemName, value + o.getQuantity());
			return true;
		} else {
			// It would not process that line, but print the error message
			String error = "same value: '" + itemName + "' in table " + i;
			System.out.println(error);
			return false;
		}
	}

	public void removeOrder(String id) throws NoMatchingOrderID {
		if (validateOrderID(id)) {
			Collection<HashSet<ListOfOrders>> c = orderTable.values();
			Iterator<HashSet<ListOfOrders>> i = c.iterator();
			boolean success = false;
			while (i.hasNext() && !success) {
				HashSet<ListOfOrders> set = i.next();
				Iterator<ListOfOrders> j = set.iterator();
				while (j.hasNext() && !success) {
					ListOfOrders o = j.next();
					if (o.getOrderID().equals(id)) {
						success = set.remove(o);
						Integer value = frequency.get(o.getItemName());
						value -= o.getQuantity();
						if (value != 0)
							frequency.replace(o.getItemName(), value);
						else
							frequency.remove(o.getItemName());
					}
				}
			}
			if (!success)
				throw new NoMatchingOrderID(id);
		} else {
			// It would not delete that Order, but print the error message
			String error = "Could not delete the order. '" + id + "' is not a valid order ID";
			System.out.println(error);
		}
	}

	public int getNumberOfOrders() {
		int total = 0;
		Collection<HashSet<ListOfOrders>> c = orderTable.values();
		Iterator<HashSet<ListOfOrders>> i = c.iterator();
		while (i.hasNext()) {
			total += i.next().size();
		}
		return total;
	}

	public String listAllOrders() {
		String summary = "";
		if (!orderTable.isEmpty()) {
			Collection<HashSet<ListOfOrders>> set = orderTable.values();
			Iterator<HashSet<ListOfOrders>> i = set.iterator();
			while (i.hasNext()) {
				Iterator<ListOfOrders> io = i.next().iterator();
				while (io.hasNext())
					summary += io.next().printInfo() + "\n";
			}
		} else
			summary += "Empty";
		return summary;
	}

	public String orderedItems() {
		String list = "";
		Iterator<Entry<String, Integer>> it = frequency.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> pair = (Entry<String, Integer>) it.next();
			list += String.format("%-22s", pair.getKey()) + pair.getValue() + "\n";
		}
		if (list.equals(""))
			list += "Empty";
		return list;
	}

	private boolean validateOrderID(String id) {
		if (id.length() != 9)
			return false;
		if (id.charAt(0) != 'O')
			return false;
		if (!id.substring(1).matches("\\d+"))
			return false;
		return true;
	}
}
