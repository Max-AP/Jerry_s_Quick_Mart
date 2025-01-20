import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Inventory {
    private static Inventory inventory = null;
    private ArrayList<Item> items;

    private Inventory() {

    }

    private Inventory(ArrayList<Item> items) {
        this.items = items;
    }

    public static synchronized Inventory getInstance(ArrayList<Item> items) {
        if (inventory == null) {
            inventory = new Inventory(items);
        }
        return inventory;
    }
    public static synchronized Inventory getInstance() {
        if (inventory != null) {
            return inventory;
        }
        return null;
    }

    public ArrayList<Item> getInventoryItems() {
        return items;
    }

    public boolean inventoryCheck(String itemName, int quantity) {
        if (isInInventory(itemName)) {
            int remainingInventory = remainingNumberOfItem(itemName);
            if (quantity <= remainingInventory) {
                return true;
            } else {
                System.out.println("There is only " + remainingInventory + " " + itemName + " items to a total of " +
                        quantity + " that you are trying to add.");
                return false;
            }
        } else {
            System.out.println("Item " + itemName + " is not in inventory");
            return false;
        }
    }

    public double getPrice(String itemName, boolean isMember) {
        for (Item item : items) {
            if (item.getItemName().equalsIgnoreCase(itemName)){
                if(isMember){
                    return item.getMemberPrice();
                } else {
                    return item.getRegularPrice();
                }
            }
        }
        return -1;
    }

    public boolean isTaxable(String itemName) {
        for (Item item : items) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                return item.isTaxable();
            }
        }
        return false;
    }

    private boolean isInInventory(String itemName) {
        for (Item item : items) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    private int remainingNumberOfItem(String itemName){
        for (Item item : items) {
            if (item.getItemName().equalsIgnoreCase(itemName)){
                return item.getQuantity();
            }
        }
        return 0;
    }

    public synchronized void updateInventory(Map<String, Integer> cartMap) {
        for (Map.Entry<String, Integer> cartEntry : cartMap.entrySet()) {
            String cartItemName = cartEntry.getKey();
            int cartItemQuantity = cartEntry.getValue();

            for (Item item : items) {
                if (item.getItemName().equalsIgnoreCase(cartItemName)) {
                    int newQuantity = item.getQuantity() - cartItemQuantity;
                    item.setQuantity(newQuantity);

                    if (newQuantity < 0) {
                        System.out.println("Warning: Inventory quantity for " + cartItemName + " is negative. Check for errors.");
                    }
                }
            }
        }

        System.out.println("Inventory updated after transaction.");
    }


}
