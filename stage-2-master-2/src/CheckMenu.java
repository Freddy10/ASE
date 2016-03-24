import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CheckMenu {
	private MenuHashMap menuEntries;

	private static final String[] categories = new String[] { "Starter", "Main", "Side", "Dessert", "Drink" };

	public CheckMenu() {

		menuEntries = new MenuHashMap();

		BufferedReader buff = null;
		InputStream is = null;
		InputStreamReader isr = null;
		String data[] = new String[4];
		try {
			is = getClass().getResourceAsStream("resources/Menu.txt");
			isr = new InputStreamReader(is);
			buff = new BufferedReader(isr);
			String inputLine = buff.readLine();
			int line_count = 0;
			while (inputLine != null) {
				line_count++;
				try {

					data = inputLine.split(";");
					for (int i = 0; i < 5; i++) {
						data[i] = data[i].trim();
					}

					double price = Double.parseDouble(data[1]);
					int time = Integer.parseInt(data[4]);

					boolean valid_category = Arrays.asList(categories).contains(data[2]);

					if (!valid_category) {
						try {
							throw new CheckCategory(data[2]);
						} catch (CheckCategory ic) {
							System.out.println(ic.getMessage());
						}

					} else if (data[3].toLowerCase().equals("true") || data[3].toLowerCase().equals("false")) {
						boolean is_veg = Boolean.parseBoolean(data[3]);

						ListOfMenu m = new ListOfMenu(data[0], price, data[2], is_veg, time);

						menuEntries.addItem(m);

					} else {
						System.out.println("Invalid boolean data format in line " + line_count + ".");
					}

					inputLine = buff.readLine();

				} catch (NumberFormatException nfe) {
					System.out.println("Error adding '" + data[0] + "'. Price '" + data[1] + "' or Time '" + data[3]
							+ "' is not a number.");
					inputLine = buff.readLine();
				} catch (ArrayIndexOutOfBoundsException aoe) {

					inputLine = buff.readLine();
				} catch (CheckMenuList e) {
					System.out.println(e.getMessage());
					inputLine = buff.readLine();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				buff.close();
				isr.close();
				is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	public MenuHashMap getMenuEntries() {
		return menuEntries;
	}

}
