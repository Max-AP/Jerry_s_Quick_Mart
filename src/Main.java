import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.print("""
                    Welcome to Jerry's Quick Mart!
                    Please enter the name of the inventory file (e.g., inventory.txt): \s""");
        Inventory inventoryInstance = null;
        String inventoryFileName;
        boolean isAlive = true;
        Scanner scanner = new Scanner(System.in);
        do {
            inventoryFileName = scanner.nextLine();
            CustomFileReader customFileReader = new CustomFileReader(inventoryFileName);
            ArrayList<Item> inventoryCheck = customFileReader.readItems();
            if (inventoryCheck == null) {
                continue;
            }
            inventoryInstance = Inventory.getInstance(inventoryCheck);
        } while (inventoryInstance == null);
        System.out.println("Inventory successfully uploaded!");

        while (isAlive) {
            System.out.print("Are you a Rewards Member? (yes/no): ");
            boolean isMember = scanner.nextLine().equalsIgnoreCase("yes");

            Cart cart = new Cart(isMember);
            boolean isTransaction = true;

            while (isTransaction) {
                Scanner scanner3 = new Scanner(System.in);
                System.out.println("\nOptions:");
                System.out.println("1. Add Item to Cart");
                System.out.println("2. Remove Item from Cart");
                System.out.println("3. View Cart");
                System.out.println("4. Empty Cart");
                System.out.println("5. Checkout");
                System.out.println("6. Cancel Transaction");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");
                int i;
                try {
                    i = scanner3.nextInt();
                } catch (Exception e) {
                    i = -1;
                }

                Scanner scanner2 = new Scanner(System.in);
                switch (i) {
                    case 1 -> {
                        System.out.println("Enter item name:");
                        String name = scanner2.nextLine();
                        System.out.println("Enter quantity:");
                        int quantity = scanner2.nextInt();
                        cart.addItem(name, quantity);
                    }
                    case 2 -> {
                        System.out.println("Enter item name:" );
                        String name = scanner2.nextLine();
                        System.out.println("Enter quantity or type all to remove all:");
                        String option = scanner2.nextLine();
                        if (option.equalsIgnoreCase("all")) {
                            cart.removeItemCompletely(name);
                        } else {
                            try{
                                cart.removeItemByQuantity(name, Integer.parseInt(option));
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid quantity");
                            }
                        }
                    }
                    case 3 -> cart.printCart();
                    case 4 -> cart.emptyCart();
                    case 5 -> {
                        System.out.println("Enter cash paid:");
                        double cash = scanner2.nextDouble();
                        cart.checkout(cash, inventoryFileName);
                        isTransaction = false;
                    }
                    case 6 -> {
                        System.out.println("Transaction canceled.");
                        isTransaction = false;
                    }
                    case 7 -> {
                        System.out.println("Thank you for visiting!");
                        isTransaction = false;
                        isAlive = false;
                    }
                    default -> System.out.println("Invalid option.");
                }
            }
        }

    }
}