import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

public class Cart {
    private HashMap<String, Integer> cartMap;
    private boolean isRewardsMember;
    private double subTotal = 0.0;
    private final double TAX_RATE = 0.065;
    private double total = 0.0;
    private double cash = 0.0;
    private String transactionNumber;

    public Cart() {}

    public Cart(boolean isRewardsMember) {
        cartMap = new HashMap<>();
        File transactionFile = new File("transaction_counter");
        try {
            if (!transactionFile.exists()) {
                transactionFile.createNewFile();
                FileWriter fileWriter = new FileWriter(transactionFile);
                fileWriter.write("000001");
                fileWriter.close();
            }
            Scanner scanner = new Scanner(transactionFile);
            transactionNumber = scanner.nextLine();
            scanner.close();

        } catch (IOException e) {
            System.out.println("Error transaction number.");
        }

        this.isRewardsMember = isRewardsMember;
    }

    public void addItem(String itemName, int quantity) {
        if (quantity >= 0){
            String itemNameLowerCase = itemName.toLowerCase();
            int initialQuantity = quantity;
            if (cartMap.containsKey(itemNameLowerCase)) {
                initialQuantity += cartMap.get(itemNameLowerCase);
            }
            assert Inventory.getInstance() != null;
            if (Inventory.getInstance().inventoryCheck(itemNameLowerCase, initialQuantity)){
                cartMap.put(itemNameLowerCase, initialQuantity);
                double itemPrice = Inventory.getInstance().getPrice(itemNameLowerCase, isRewardsMember);
                subTotal += itemPrice * quantity;
            }
        } else {
            System.out.println("Can't add negative quantities.");
        }
    }

    public void removeItemByQuantity(String itemName, int quantity) {
        if (quantity >= 0) {
            String itemNameLowerCase = itemName.toLowerCase();
            if (cartMap.containsKey(itemNameLowerCase)) {
                cartMap.put(itemNameLowerCase, cartMap.get(itemNameLowerCase) - quantity);
                assert Inventory.getInstance() != null;
                double itemPrice = Inventory.getInstance().getPrice(itemNameLowerCase, isRewardsMember);
                subTotal -= itemPrice * quantity;
                if (cartMap.get(itemNameLowerCase) <= 0) {
                    removeItemCompletely(itemName);
                } else {
                    System.out.println("Removed " + quantity + " " + itemName + " from cart.");
                }
            } else {
                System.out.println("Item not found, nothing was removed.");
            }
        } else {
            System.out.println("Can't remove negative quantities.");
        }
    }

    public void removeItemCompletely(String itemName){
        Integer quantity = cartMap.remove(itemName.toLowerCase());
        if (quantity != null) {
            assert Inventory.getInstance() != null;
            double itemPrice = Inventory.getInstance().getPrice(itemName.toLowerCase(), isRewardsMember);
            subTotal -= itemPrice * quantity;
            System.out.println("Removed all " + itemName + " from cart.");
        } else {
            System.out.println("Item not found, nothing was removed.");
        }
    }

    public void emptyCart(){
        cartMap.clear();
        subTotal = 0.0;
        total = 0.0;
        System.out.println("Cart has been emptied.");
    }

    public void printCart(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        System.out.println(today.format(formatter));
        System.out.println("TRANSACTION: " + transactionNumber);

        printCartDetails();
        printCartFooter();
    }

    private void printCartDetails() {
        System.out.printf("%-20s %-10s %-15s %-15s%n", "ITEM", "QUANTITY", "UNIT PRICE", "TOTAL");

        Inventory inventory = Inventory.getInstance();
        for (HashMap.Entry<String, Integer> entry : cartMap.entrySet()) {
            String itemName = capitalizeWords(entry.getKey());
            int quantity = entry.getValue();
            assert inventory != null;
            double unitPrice = inventory.getPrice(itemName, isRewardsMember);
            double totalPrice = unitPrice * quantity;

            System.out.printf("%-20s %-10d $%-15.2f $%-15.2f%n", itemName, quantity, unitPrice, totalPrice);
        }
        System.out.println("************************************");
    }

    private void printCartFooter() {
        Inventory inventory = Inventory.getInstance();
        if (inventory == null) {
            System.out.println("Inventory data is unavailable.");
            return;
        }

        double tax = cartMap.entrySet().stream()
                .filter(entry -> inventory.isTaxable(entry.getKey()))
                .mapToDouble(entry ->
                        inventory.getPrice(entry.getKey(), isRewardsMember) * entry.getValue() * TAX_RATE)
                .sum();

        total = subTotal + tax;

        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("TOTAL NUMBER OF ITEMS SOLD: " + cartMap.values().stream().mapToInt(Integer::intValue).sum());
        System.out.println("SUB-TOTAL: $" + df.format(subTotal));
        System.out.println("TAX (6.5%): $" + df.format(tax));
        System.out.println("TOTAL: $" + df.format(total));
    }

    public void checkout(double cash, String inventoryFileName) {
        this.cash = cash;
        Inventory inventory = Inventory.getInstance();
        if (inventory == null) {
            System.out.println("Inventory data is unavailable.");
            return;
        }
        double tax = cartMap.entrySet().stream()
                .filter(entry -> inventory.isTaxable(entry.getKey()))
                .mapToDouble(entry ->
                        inventory.getPrice(entry.getKey(), isRewardsMember) * entry.getValue() * TAX_RATE)
                .sum();

        total = subTotal + tax;

        DecimalFormat df = new DecimalFormat("0.00");

        if (cash < total) {
            System.out.println("Insufficient cash. Transaction cannot be completed.");
            System.out.println("TOTAL: $" + df.format(total));
            System.out.println("CASH: $" + df.format(cash));
            System.out.println("SHORTAGE: $" + df.format(total - cash));
        } else {
            System.out.println("TOTAL: $" + df.format(total));
            System.out.println("CASH: $" + df.format(cash));
            System.out.println("CHANGE: $" + df.format(cash - total));
            System.out.println("Thank you for your purchase!");
            completeTransaction(inventoryFileName);
            saveReceiptToFile(cash - total);
        }
    }

    private void completeTransaction(String inventoryFileName) {
        assert Inventory.getInstance() != null;
        Inventory.getInstance().updateInventory(cartMap);
        saveInventoryToFile(inventoryFileName);
    }

    private void saveInventoryToFile(String inventoryFileName) {
        Inventory inventory = Inventory.getInstance();
        if (inventory == null) {
            System.out.println("No inventory data available to save.");
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(inventoryFileName))) {
            for (Item item : inventory.getInventoryItems()) {
                String line = String.format("%s: %d, $%.2f, $%.2f, %s",
                        item.getItemName(),
                        item.getQuantity(),
                        item.getRegularPrice(),
                        item.getMemberPrice(),
                        item.isTaxable() ? "Taxable" : "Tax-Exempt");
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error writing inventory to file: " + e.getMessage());
        }
    }

    private void saveReceiptToFile(double change) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String dateFormatted = today.format(dateFormatter);

        String fileName = String.format("transaction_%s_%s.txt", transactionNumber, dateFormatted);

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(today.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
            writer.println("TRANSACTION: " + transactionNumber);
            writer.printf("%-20s %-10s %-15s %-15s%n", "ITEM", "QUANTITY", "UNIT PRICE", "TOTAL");

            Inventory inventory = Inventory.getInstance();
            double regularTotal = 0.0;
            for (HashMap.Entry<String, Integer> entry : cartMap.entrySet()) {
                String itemName = capitalizeWords(entry.getKey());
                int quantity = entry.getValue();
                assert inventory != null;
                double unitPrice = inventory.getPrice(itemName.toLowerCase(), isRewardsMember);
                double totalPrice = unitPrice * quantity;
                double regularPrice = inventory.getPrice(itemName.toLowerCase(), !isRewardsMember);
                regularTotal += regularPrice * quantity;
                writer.printf("%-20s %-10d $%-15.2f $%-15.2f%n", itemName, quantity, unitPrice, totalPrice);
            }

            writer.println("************************************");
            int totalItems = cartMap.values().stream().mapToInt(Integer::intValue).sum();
            double tax = cartMap.entrySet().stream()
                    .filter(entry -> inventory.isTaxable(entry.getKey()))
                    .mapToDouble(entry ->
                            inventory.getPrice(entry.getKey(), isRewardsMember) * entry.getValue() * TAX_RATE)
                    .sum();
            double otherTax = cartMap.entrySet().stream()
                    .filter(entry -> inventory.isTaxable(entry.getKey()))
                    .mapToDouble(entry ->
                            inventory.getPrice(entry.getKey(), !isRewardsMember) * entry.getValue() * TAX_RATE)
                    .sum();
            regularTotal = regularTotal + otherTax;
            writer.println("TOTAL NUMBER OF ITEMS SOLD: " + totalItems);
            writer.printf("SUB-TOTAL: $%.2f%n", subTotal);
            writer.printf("TAX (6.5%%): $%.2f%n", tax);
            writer.printf("TOTAL: $%.2f%n", total);
            writer.printf("CASH: $%.2f%n", cash);
            writer.printf("CHANGE: $%.2f%n", change);
            writer.println("************************************");
            double savings = Math.abs(regularTotal - total);
            if (isRewardsMember) {
                writer.printf("YOU SAVED: $%.2f!%n", savings);
            } else {
                writer.printf("YOU WOULD HAVE SAVED: $%.2f BY BECOMING A MEMBER!%n", savings);
            }

            System.out.println("Receipt saved as: " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing receipt to file: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("transaction_counter"))) {
            int currentTransaction = Integer.parseInt(transactionNumber);
            writer.printf("%06d", currentTransaction + 1);

        } catch (IOException e) {
            System.out.println("Error writing transaction number to file: " + e.getMessage());
        }
    }

    private static String capitalizeWords(String input) {
        String[] words = input.split("\\s");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(Character.toTitleCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        return result.toString().trim();
    }
}
