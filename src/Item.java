import java.util.Arrays;

public class Item {
    private String itemName;
    private int quantity;
    private double regularPrice;
    private double memberPrice;
    private boolean isTaxable;

    public Item(String itemName, int quantity, double regularPrice, double memberPrice, boolean isTaxable) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.regularPrice = regularPrice;
        this.memberPrice = memberPrice;
        this.isTaxable = isTaxable;
    }

    public Item (String itemLine){
        String[] itemLineParts =  Arrays.stream(itemLine.split("[,:]"))
                .map(s -> s.trim().replace("$",""))
                .toArray(String[]::new);
        setItemName(itemLineParts[0]);
        setQuantity(Integer.parseInt(itemLineParts[1]));
        setRegularPrice(Double.parseDouble(itemLineParts[2]));
        setMemberPrice(Double.parseDouble(itemLineParts[3]));
        setTaxable(itemLineParts[4].equalsIgnoreCase("taxable"));
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public double getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(double memberPrice) {
        this.memberPrice = memberPrice;
    }

    public boolean isTaxable() {
        return isTaxable;
    }

    public void setTaxable(boolean taxable) {
        isTaxable = taxable;
    }

    @Override
    public String toString() {
        return "[" + itemName + ", " + quantity + ", $" + regularPrice + ", $" + memberPrice + ", " + isTaxable + ']';
    }
}
