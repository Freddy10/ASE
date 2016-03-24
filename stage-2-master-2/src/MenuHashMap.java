import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class MenuHashMap {

	private TreeMap<String, ListOfMenu> menuHashMap;

	public MenuHashMap() {
		menuHashMap = new TreeMap<String, ListOfMenu>();
	}

	public ListOfMenu findByName(String item_name) {
		ListOfMenu item = menuHashMap.get(item_name);
		return item;
	}

	public TreeMap<String, ListOfMenu> getMenuItemMap() {
		return menuHashMap;
	}

	public void addItem(ListOfMenu new_item) throws CheckMenuList {
		String name = new_item.getName();

		if (findByName(name) != null) {

			throw new CheckMenuList(name);
			// String error = "Could not add '" + name + "'. Duplicate value.";
			// System.out.println(error);
		} else {
			menuHashMap.put(name, new_item);
		}
	}

	public void removeItem(String item_name) {
		menuHashMap.remove(item_name);
	}

	public int getNumberOfDishes() {
		int num_of_dishes = menuHashMap.size();
		return num_of_dishes;
	}

	public String listByName() {
		String menu = "";

		Set set = menuHashMap.entrySet();

		Iterator i = set.iterator();

		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			ListOfMenu m = (ListOfMenu) me.getValue();
			menu += "\r\n" + m.printItemSummary();
		}
		return menu;
	}

	public String listByCategory() {
		String menu = "\r\nMENU \r\n==== \r\n";
		Set set = menuHashMap.entrySet();
		// Get an iterator
		Iterator iterator = set.iterator();
		// Create a string for each menu item category
		String starters = "STARERS\r\n";
		String mains = "MAINS\r\n";
		String sides = "SIDES\r\n";
		String desserts = "DESSERTS\r\n";
		String drinks = "DRINKS\r\n";

		while (iterator.hasNext()) {
			Map.Entry me = (Map.Entry) iterator.next();
			ListOfMenu m = (ListOfMenu) me.getValue();
			String categ = m.getCategory().toLowerCase();
			if (categ.equals("starter")) {
				starters += "    " + m.printItemSummary() + "\r\n";
			} else if (categ.equals("main")) {
				mains += "    " + m.printItemSummary() + "\r\n";
			} else if (categ.equals("side")) {
				sides += "    " + m.printItemSummary() + "\r\n";
			} else if (categ.equals("dessert")) {
				desserts += "    " + m.printItemSummary() + "\r\n";
			} else if (categ.equals("drink")) {
				drinks += "    " + m.printItemSummary() + "\r\n";
			} else {
				System.out.println("Ooops, somethign went wrong.");
			}
		}
		// the strings of all menu item categories being joined together
		menu += starters + mains + sides + desserts + drinks;
		return menu;
	}

	public boolean containsItem(String item_name) {
		return menuHashMap.containsKey(item_name);
	}

	public List<String> keySet() {
		List<String> key_set = (List<String>) menuHashMap.keySet();
		return key_set;
	}

	public String getRandomItemName() {
		Random r = new Random();
		List<String> keys = new ArrayList<String>(menuHashMap.keySet());
		String randomKey = keys.get(r.nextInt(keys.size()));
		return randomKey;
	}
}
