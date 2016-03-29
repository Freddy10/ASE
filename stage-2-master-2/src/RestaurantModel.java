import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Map.Entry;
import java.util.ArrayList;

public class RestaurantModel extends Observable {

	private TableOrders tableOrders;

	private MenuHashMap menuHashMap;

	private LinkedList<ListOfOrders> kitchenOrders;

	private LinkedList<ListOfOrders> hatch;

	private ArrayList<LinkedList<ListOfOrders>> tables;

	LogFile log;

	private String populateMethod;

	private boolean finishedRun;

	private boolean hatchFinished;

	private int numberOfTables;

	private HashMap<Integer, Integer> discounts;

	private boolean startThread;

	private int kitchOpenTime;

	private boolean threadFinished;

	private static final String orderTitles = String.format("%-9s", "ID") + String.format("%-7s", "TABLE")
			+ String.format("%-22s", "ITEM NAME") + String.format("%-5s", "QUANT") + "\r\n";

	public RestaurantModel() {
		tableOrders = new TableOrders();
		menuHashMap = new MenuHashMap();
		CheckMenu s = new CheckMenu();
		menuHashMap = s.getMenuEntries();
		discounts = new HashMap<Integer, Integer>();
		kitchenOrders = new LinkedList<ListOfOrders>();
		populateMethod = "";
		log = LogFile.getInstance();
		numberOfTables = 6;
		finishedRun = false;
		hatchFinished = false;
		startThread = false;
		hatch = new LinkedList<ListOfOrders>();
		threadFinished = false;
		tables = new ArrayList<LinkedList<ListOfOrders>>();
		for (int i = 1; i <= 6; i++) {
			tables.add(new LinkedList<ListOfOrders>());
		}
	}

	public boolean isFinishedRun() {
		return finishedRun;
	}

	public boolean hatchIsFinished() {
		return hatchFinished;
	}

	public boolean isThreadActive() {
		return startThread;
	}

	public void setThreadFinished() {
		threadFinished = true;

		// Notifies observers.
		setChanged();
		notifyObservers();
		clearChanged();
	}

	public boolean getThreadFinished() {
		return threadFinished;
	}

	public void setStartThread() {
		this.startThread = true;
	}

	public void setFinishedRun() {
		finishedRun = true;
	}

	public LinkedList<ListOfOrders> getOrdersInKitchen() {
		return kitchenOrders;
	}

	public void setPopulateMethod(String value) {
		populateMethod = value;
	}

	public void setKitchOpenTime(String value) {
		int time = Integer.parseInt(value.trim());
		time = 15;
		kitchOpenTime = time;
	}

	public int getKitchOpenTime() {
		return kitchOpenTime;
	}

	public void populateWithFile(int line) throws IOException {

		if (!this.isFinishedRun()) {
			try {
				InputStream is = getClass().getResourceAsStream("resources/OrderList.txt");

				String lineValue = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()
						.collect(Collectors.toList()).get(line).trim();

				if (lineValue != null && !lineValue.isEmpty()) {
					String parts[] = lineValue.split(";");

					int table = Integer.parseInt(parts[0].trim());
					int quantity = Integer.parseInt(parts[2].trim());
					String item = parts[1].trim();

					if (menuHashMap.containsItem(item) && ((numberOfTables + 1) > table && table > 0)
							&& (quantity > 0)) {
						ListOfOrders o;
						try {
							o = new ListOfOrders(table, item, quantity);

							if (tableOrders.addOrder(o)) {
								this.receiveOrder(o);
							} else {
								String error = "Error  " + line + " -  no item " + item + " in  menu";
								System.out.println(error);
							}
						} catch (InvalidPositiveInteger e) {
							e.printStackTrace();
						}
					}
				}
			} catch (NullPointerException npe) {
				System.out.println("OrderList.txt not  found\n");
				System.exit(0);
			}
		}
	}

	public synchronized void populateWithGenerator() throws InvalidPositiveInteger {
		ListOfOrders o = this.generateRandomOrder();
		if (tableOrders.addOrder(o)) {
			this.receiveOrder(o);
		}
		setChanged();
		notifyObservers();
		clearChanged();
	}

	public void setHatchFinished() {
		hatchFinished = true;
	}

	public ArrayList<LinkedList<ListOfOrders>> getListOfTables() {
		return tables;
	}

	public LinkedList<ListOfOrders> getHatch() {
		return hatch;
	}

	public String getOrderList(int i) {
		String report = "TABLE " + (i + 1) + "\n";
		if (tables.get(i).size() == 0) {
			report += "There are no orders to show";
		} else {
			int num = 1;
			for (ListOfOrders o : tables.get(i)) {
				report += num++ + " " + o.getItemName() + " * " + o.getQuantity() + "\n";
			}
		}
		return report;
	}

	public MenuHashMap getMenuItemMap() {
		return menuHashMap;
	}

	public void start() {
		Thread kitchOrderThread = new Thread();
		kitchOrderThread.start();

		Kitchen firstStep = new Kitchen(this);
		Thread sendToKitchen = new Thread(firstStep);
		sendToKitchen.start();

		Hatch secondStep = new Hatch(this);
		Thread sendToHatch = new Thread(secondStep);
		sendToHatch.start();

		Tables thirdStep = new Tables(this);
		Thread sendToTables = new Thread(thirdStep);
		sendToTables.start();
	}

	public String getKitchenReport() {
		String report = "LIST OF ORDERS IN THE KITCHEN \r\n" + orderTitles;
		for (ListOfOrders ord : kitchenOrders) {
			report += ord.printShortInfo() + "\r\n";
		}
		return report;
	}

	public String getHatchReport() {
		String report = "LIST OF ORDERS IN THE HATCH \r\n" + orderTitles;
		for (ListOfOrders ord : hatch) {
			report += ord.printShortInfo() + "\r\n";
		}
		return report;
	}

	public synchronized void receiveOrder(ListOfOrders o) {
		kitchenOrders.add(o);
		log.addEntry("ListOfOrders " + o.getOrderID() + " ('" + o.getItemName() + "', x" + o.getQuantity() + ", table "
				+ o.getTableID() + ") has been sent to the kitchen.\r\n");

		setChanged();
		notifyObservers();
		clearChanged();

		try {

			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ListOfOrders generateRandomOrder() throws InvalidPositiveInteger {

		Random r1 = new Random();
		int t = r1.nextInt(6) + 1;

		Random r2 = new Random();
		int q = r2.nextInt(10) + 1;
		ListOfOrders o = new ListOfOrders(t, menuHashMap.getRandomItemName(), q);
		return o;
	}

	public String getPopulateMethod() {
		return populateMethod;
	}

	public ListOfOrders getFirstOrder() {
		return this.kitchenOrders.getFirst();
	}

	public synchronized void orderToHatch() {
		ListOfOrders firstOrder = this.getFirstOrder();
		this.hatch.add(firstOrder);
		log.addEntry("ListOfOrders " + firstOrder.getOrderID() + " ('" + firstOrder.getItemName() + "', x"
				+ firstOrder.getQuantity() + ", table " + firstOrder.getTableID()
				+ ") has been sent to the hatch.\r\n");
		kitchenOrders.removeFirst();
		setChanged();
		notifyObservers();
		clearChanged();
	}

	public boolean noOrdersInKitchen() {
		if (kitchenOrders.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public synchronized void orderToTable() {
		if (!this.hatch.isEmpty()) {
			ListOfOrders firstOrder = this.hatch.getFirst();
			tables.get(firstOrder.getTableID() - 1).add(firstOrder);
			log.addEntry("ListOfOrders " + firstOrder.getOrderID() + " ('" + firstOrder.getItemName() + "', x"
					+ firstOrder.getQuantity() + ", table " + firstOrder.getTableID()
					+ ") has been sent to the table.\r\n");
			this.hatch.removeFirst();
			if (kitchenOrders.isEmpty() && hatch.isEmpty())
				this.setHatchFinished();
			setChanged();
			notifyObservers();
			clearChanged();
		}
	}

	public String reporter() {
		String report = "";
		report += menuHashMap.listByCategory();
		report += "\nTABLE SUMMARY\n==============";
		report += printBills();
		report += this.frequencyReport();
		report += this.dishesNotOrdered();
		int highestBill = this.mostExpensiveTableBill();
		int lowestBill = this.cheapestTableBill();
		report += "\nBILLS PRICE STATISTICS (without discounts)\n==========================================\n";
		report += "Most expensive table: Table " + highestBill + ", whose bill is £"
				+ String.format("%.2f", getTableTotal(highestBill)) + "\n";
		report += "Cheapest table: Table " + lowestBill + ", whose bill is £"
				+ String.format("%.2f", getTableTotal(lowestBill)) + "\n";
		report += "The average bill paid is: £" + String.format("%.2f", this.averageBill()) + "\n";
		report += "\nVEGETARIAN DISHES STATISTICS\n============================\n";
		report += "Menu contains " + vegetarianDishes() + " veggie dishes out of " + menuHashMap.getNumberOfDishes()
				+ "\n";
		report += String.format("%.1f", vegetarianOrdersPercentage()) + "% of orders are vegetarian dishes";
		return report;
	}

	public void writer(String filename, String report) {
		FileWriter fW;
		try {
			fW = new FileWriter(filename);
			fW.write("----  REPORT OF Today ----\n");
			fW.write(report);
			fW.close();
		} catch (FileNotFoundException fnf) {
			System.out.println(filename + "  not  found\n");

		} catch (IOException ioe) {
			ioe.printStackTrace();
			// If this error occurs, the application would end
			System.exit(1);
		}
	}

	protected void updateDiscounts(int table, int discount) {
		discounts.put(table, discount);
	}

	protected void deleteDiscount(int table) {
		discounts.remove(table);
	}

	public double getTableTotal(int table) {
		double total = 0;
		HashSet<ListOfOrders> set = tableOrders.findByTable(table);
		if (set != null) {
			Iterator<ListOfOrders> i = set.iterator();
			ListOfOrders temp = null;
			while (i.hasNext()) {
				temp = i.next();
				total += menuHashMap.findByName(temp.getItemName()).getPrice() * temp.getQuantity();
			}
		}
		return total;
	}

	public double getTableDiscountedTotal(int table) {
		double total = getTableTotal(table);

		if (discounts.containsKey(table)) {
			double discount = total * (discounts.get(table) / 100.0);
			total -= discount;

		} else {
			int autoDiscount = (int) (total / 10);
			autoDiscount *= 2;
			total -= autoDiscount;
		}
		return total;
	}

	public int mostExpensiveTableBill() {
		int table = 0;
		if (!(tableOrders.getOrderTable().isEmpty())) {
			double max = 0;
			for (Integer t : tableOrders.getOrderTable().keySet()) {
				double price = getTableTotal(t);
				if (price > max) {
					max = price;
					table = t;
				}
			}
		} else
			System.out.println("no orders");
		return table;
	}

	public int cheapestTableBill() {
		int table = 0;
		if (!(tableOrders.getOrderTable().isEmpty())) {
			double min = getTableTotal(tableOrders.getOrderTable().firstKey());
			for (Integer t : tableOrders.getOrderTable().keySet()) {
				double price = getTableTotal(t);
				if (price <= min) {
					min = price;
					table = t;
				}
			}
		} else
			System.out.println("no orders");
		return table;
	}

	public double averageBill() {
		double average = 0;
		int numberOfTables = tableOrders.getOrderTable().size();
		if (!(tableOrders.getOrderTable().isEmpty())) {
			for (Integer t : tableOrders.getOrderTable().keySet()) {
				average += getTableTotal(t);
			}
		} else
			System.out.println("no orders");
		return Math.round((average / numberOfTables) * 100.0) / 100.0;
	}

	public String frequencyReport() {
		String report = "\nFREQUENCY REPORT\n================\n";
		report += tableOrders.orderedItems();
		return report;
	}

	public String dishesNotOrdered() {
		String report = "\nDISHES NOT ORDERED\n==================\n";
		if (menuHashMap.getNumberOfDishes() > 0) {
			Iterator<String> it = menuHashMap.getMenuItemMap().keySet().iterator();
			while (it.hasNext()) {
				String temp = it.next();
				if (!(tableOrders.getFrequency().containsKey(temp))) {
					report += temp + "\n";
				}
			}
			if (report.equals("DISHES NOT ORDERED\n==================\n"))
				report += "All of dishes have been already ordered";
		} else
			report += "There is no dishes in the menu yet\n";
		return report;
	}

	public String getTableBill(int table) {
		String bill = "no orders for table " + table + "\n";
		if (tableOrders.getOrderTable().containsKey(table)) {
			bill = "\nTABLE " + table + "\n";
			HashSet<ListOfOrders> set = tableOrders.getOrderTable().get(table);
			Iterator<ListOfOrders> i = set.iterator();
			while (i.hasNext()) {
				ListOfOrders o = i.next();
				bill += String.format("%-20s", o.getItemName().toUpperCase()) + String.format("%-2s", o.getQuantity())
						+ " , ";
				double price = menuHashMap.findByName(o.getItemName()).getPrice();
				bill += String.format("%5.2f", price) + " = " + String.format("%6.2f", price * o.getQuantity()) + "\n";
			}
			bill += String.format("%-33s", "") + "\n";
			double total = getTableTotal(table);
			bill += String.format("%-33s", "Total bill  :") + String.format("%6.2f", total) + "\n";
			double discount = total - getTableDiscountedTotal(table);
			bill += String.format("%-33s", "Reduction :") + String.format("%6.2f", discount) + "\n";
			bill += String.format("%-33s", "Total reduction :") + String.format("%6.2f", getTableDiscountedTotal(table))
					+ "\n";
		}
		return bill;
	}

	public String printBills() {
		String report = "";
		for (Integer i : tableOrders.getOrderTable().keySet()) {
			report += getTableBill(i);
		}
		return report;
	}

	public int vegetarianDishes() {
		int amount = 0;
		Iterator<ListOfMenu> it = menuHashMap.getMenuItemMap().values().iterator();
		while (it.hasNext()) {
			if (it.next().isVegetarianPrint().equals("(V)"))
				amount++;
		}
		return amount;
	}

	public double vegetarianOrdersPercentage() {
		int total = 0;
		int veggies = 0;
		Iterator<Entry<String, Integer>> it = tableOrders.getFrequency().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> pair = (Entry<String, Integer>) it.next();
			total += pair.getValue();
			if (menuHashMap.findByName(pair.getKey()).isVegetarianPrint().equals("(V)"))
				veggies += pair.getValue();
		}
		if (total > 0) {
			double percentage = veggies * 100 / total;
			return percentage;
		} else
			return 0;
	}

	public String generateBill(String numberText, String discountText) {
		int number = Integer.parseInt(numberText);
		if (!discountText.equals("")) {
			int discount = Integer.parseInt(discountText);
			this.updateDiscounts(number, discount);
		} else {
			this.deleteDiscount(number);
		}
		return this.getTableBill(number);
	}
}
