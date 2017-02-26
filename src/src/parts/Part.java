package parts;

/**
 * Object to use for storing data of a part from part stock table.
 * @author George
 */
public class Part {
	private int id, stock;
	private String name, desc;
	private float price;
	
	/**
	 * Constructor to be used when getting an existing part from
	 * the database. Set id to id from database.
	 * @param id part id
	 * @param name name of the part.
	 * @param desc a description of the part.
	 * @param stock how much stock of the part is in the garage.
	 * @param price the monetary price of the part.
	 */
	public Part(int id, String name, String desc, int stock, float price) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.stock = stock;
		this.price = price;
	}
	
	/**
	 * Constructor to be used when creating a new part to be inserted into the
	 * database. Id will be set to -1 to avoid insertion.
	 * @param name name of the part.
	 * @param desc a description of the part.
	 * @param stock how much stock of the part is in the garage.
	 * @param price the monetary price of the part.
	 */
	public Part(String name, String desc, int stock, float price){
		id = -1;
		this.name = name;
		this.desc = desc;
		this.stock = stock;
		this.price = price;
	}
	
	// GETTERS AND SETTERS
	// NOTHING INTERESTING TO BE SEEN AT ALL

	public int getId() {
		return id;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
}
