package parts;

/**
 * Object to use for storing the data of an order.
 * @author George
 */
public class Order {
	private int id, quantity, supplierID, partID;
	private String dateOrdered, dateExpected;
	
	/**
	 * Constructor for when getting order from database. Set id to order id
	 * from db
	 * @param id The order id.
	 * @param quantity Quantity ordered.
	 * @param supplierID Id of the supplier ordered from
	 * @param partID if of the part being ordered.
	 * @param dateOrdered Date the order was made.
	 * @param dateExpected Date the order is expected to be delivered.
	 */
	public Order(int id, int quantity, int supplierID, int partID, String dateOrdered, String dateExpected) {
		this.id = id;
		this.quantity = quantity;
		this.supplierID = supplierID;
		this.partID = partID;
		this.dateOrdered = dateOrdered;
		this.dateExpected = dateExpected;

	}
	
	/**
	 * Constructor to be used when creating an order to be inserted into db.
	 * id is set to -1 to avoid insertion. (heh)
	 * @param quantity Quantity ordered.
	 * @param supplierID Id of the supplier ordered from
	 * @param partID if of the part being ordered.
	 * @param dateOrdered Date the order was made.
	 * @param dateExpected Date the order is expected to be delivered.
	 */
	public Order(int quantity, int supplierID, int partID, String dateOrdered, String dateExpected) {
		id = -1;
		this.quantity = quantity;
		this.supplierID = supplierID;
		this.partID = partID;
		this.dateOrdered = dateOrdered;
		this.dateExpected = dateExpected;

	}

	public int getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getSupplierID() {
		return supplierID;
	}

	public int getPartID() {
		return partID;
	}

	public String getDateOrdered() {
		return dateOrdered;
	}

	public String getDateExpected() {
		return dateExpected;
	}
}
