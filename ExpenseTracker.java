import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class ExpenseTracker {
    private static class Transaction {
        String type;
        String category;
        double amount;
        LocalDate date;

        Transaction(String type, String category, double amount, LocalDate date) {
            this.type = type;
            this.category = category;
            this.amount = amount;
            this.date = date;
        }

        @Override
        public String toString() {
            return type + "," + category + "," + amount + "," + date;
        }
    }

    private static final List<Transaction> transactions = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Expense Tracker Menu ---");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Load from File");
            System.out.println("5. Save to File");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addTransaction("income");
                case 2 -> addTransaction("expense");
                case 3 -> showMonthlySummary();
                case 4 -> loadFromFile();
                case 5 -> saveToFile();
                case 6 -> running = false;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void addTransaction(String type) {
        System.out.print("Enter category (" + (type.equals("income") ? "salary/business" : "food/rent/travel") + "): ");
        String category = scanner.nextLine().toLowerCase();
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        transactions.add(new Transaction(type, category, amount, date));
        System.out.println("Transaction added.");
    }

    private static void showMonthlySummary() {
        System.out.print("Enter month (1-12): ");
        int monthInput = Integer.parseInt(scanner.nextLine());
        Month month = Month.of(monthInput);
        double totalIncome = 0;
        double totalExpense = 0;

        System.out.println("\n--- Summary for " + month + " ---");
        for (Transaction t : transactions) {
            if (t.date.getMonth() == month) {
                System.out.println(t.type + " | " + t.category + " | $" + t.amount + " | " + t.date);
                if (t.type.equals("income")) totalIncome += t.amount;
                else totalExpense += t.amount;
            }
        }

        System.out.println("Total Income: $" + totalIncome);
        System.out.println("Total Expenses: $" + totalExpense);
        System.out.println("Net Savings: $" + (totalIncome - totalExpense));
    }

    private static void loadFromFile() {
        System.out.print("Enter file name to load: ");
        String fileName = scanner.nextLine();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            transactions.clear(); // Clear current transactions
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("type")) continue;
                String[] parts = line.split(",");
                transactions.add(new Transaction(parts[0], parts[1], Double.parseDouble(parts[2]), LocalDate.parse(parts[3])));
            }
            System.out.println("Transactions loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    private static void saveToFile() {
        System.out.print("Enter file name to save: ");
        String fileName = scanner.nextLine();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("type,category,amount,date\n");
            for (Transaction t : transactions) {
                bw.write(t.toString());
                bw.newLine();
            }
            System.out.println("Transactions saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
