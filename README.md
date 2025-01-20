
# Jerry's Quick Mart

## Overview

Jerry's Quick Mart is a simple, menu-driven cash register system designed to manage customer transactions for a retail store. It supports inventory management, receipt generation, and rewards member functionality. The application is written in **Java (OpenJDK 23)** and runs entirely on the command line.

## Features

- Add items to the cart.
- Remove items (by quantity or entirely) from the cart.
- View cart details, including item breakdown, subtotal, tax, and total.
- Support for rewards members with discounted pricing.
- Generate detailed receipts in .txt format.
- Automatically updates inventory after transactions.
- Tracks savings for rewards members and displays potential savings for non-members.
- Ensures inventory consistency by checking stock before adding items to the cart.


## Getting Started
### Prerequisites
- OpenJDK 23
- Any IDE (e.g., IntelliJ IDEA, Eclipse) or terminal for running the program.
### Installation


1. Clone the repository:
```
  git clone https://github.com/Max-AP/Jerry_s_Quick_Mart.git
```
2. Navigate to the project directory:
```
  cd jerrys-quick-mart
```
3. Compile the Java files:
```
  javac -d bin src/*.java
```
4. Run the program:
```
  java -cp bin Main
```
    
## Usage
### Running The Application
When you start the application, you'll be prompted to select whether the customer is a rewards member. This choice impacts the pricing displayed during the transaction.
### Commands
1. **Add Items to Cart:**
Enter the item name and quantity to add items to the cart. The program validates inventory before adding.

2. **Remove Items from Cart:**
Remove items by specifying the name and quantity. You can also remove an item entirely.

3. **View Cart:**
View all items in the cart, including quantities, unit prices, totals, subtotal, tax, and total cost.

4. **Checkout:**
Enter the cash provided to complete the transaction. If the cash is insufficient, the program will notify you.

5. **Receipt Generation:**
A detailed receipt is saved in the format:
`transaction_<transactionNumber>_<MMDDYYYY>.txt`
The receipt includes:

- Date and transaction number.
- Itemized list of purchases.
- Subtotal, tax, total, cash, and change.
- Savings information for rewards members or potential savings for non-members.

### Example Inventory Format
The inventory is loaded from a file named inventory.txt with the following format:
```
Milk: 5, $3.75, $3.50, Tax-Exempt
Bread: 10, $2.50, $2.25, Tax-Exempt
Eggs: 12, $4.00, $3.75, Tax-Exempt
Butter: 4, $5.00, $4.50, Tax-Exempt
Peanut Butter: 6, $3.25, $3.00, Tax-Exempt
```
- Syntax: `<item>: <quantity>, <regular price>, <member price>, <tax status>`

- Prices are in USD.

- Tax-Exempt or Taxable determines whether tax is applied.
## Assumptions

- All transactions are cash-only.
- Florida sales tax rate of 6.5% is applied to taxable items.
- Rewards members receive discounted prices.
- Inventory is updated only after a successful checkout.
- The transaction counter starts at 000001 and increments automatically after each transaction.
- The program has tested fault tolerance for item names and quantities to an extent.
- The inventory files are in the same folder the program is compiled.
- The inventory files have been cured and have perfect syntax.
- The program has read/write permission on the folder it's operating from.
## File Structure

- `src/`: Contains all the Java source files.

    - `Main.java`: Entry point for the program.
    - `Cart.java`: Manages cart operations.
    - `Inventory.java`: Handles inventory management.
    - `Item.java`: Represents individual inventory items.

- `bin/`: Contains compiled .class files.
- `inventory.txt`: Stores inventory details.
- `transaction_counter`: Tracks the current transaction number.
- `transaction_<transactionNumber>_<MMDDYYYY>.txt`: Generated receipts.
## Running Tests

Care for and handle file structure accordingly based on your use case. The program was written and has been tested on IntelliJ IDEA 2024.3.2 which can be finicky about the file structure. 

Because it's worth mentioning again, the program is written in **Java (OpenJDK 23)**, some functionality may not be available in previous versions of Java and may throw some errors if compiled like that.

