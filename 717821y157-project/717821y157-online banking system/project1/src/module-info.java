package project1;
import java.sql.*;
import java.util.Scanner;

public class OnlineBanking {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/bank";
    static final String USER = "username";
    static final String PASS = "password";

    static Connection conn;
    static Statement stmt;
    static PreparedStatement pstmt;
    static ResultSet rs;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            int choice;
            do {
                System.out.println("1. Create Account");
                System.out.println("2. Deposit Amount");
                System.out.println("3. Withdraw Amount");
                System.out.println("4. Check Balance");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        depositAmount();
                        break;
                    case 3:
                        withdrawAmount();
                        break;
                    case 4:
                        checkBalance();
                        break;
                    case 5:
                        System.out.println("Thank you for using our banking system!");
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                        break;
                }
                System.out.println();
            } while (choice != 5);

            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void createAccount() throws SQLException {
        System.out.println("==== Create Account ====");
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        System.out.print("Enter account holder name: ");
        String accountHolderName = scanner.next();

        String sql = "INSERT INTO accounts (account_number, account_holder_name, balance) VALUES (?, ?, ?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, accountNumber);
        pstmt.setString(2, accountHolderName);
        pstmt.setDouble(3, 0.0);
        pstmt.executeUpdate();

        System.out.println("Account created successfully!");
    }

    static void depositAmount() throws SQLException {
        System.out.println("==== Deposit Amount ====");
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();

        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setDouble(1, amount);
        pstmt.setInt(2, accountNumber);
        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected == 0) {
            System.out.println("Account not found!");
        } else {
            System.out.println("Amount deposited successfully!");
        }
    }

    static void withdrawAmount() throws SQLException {
        System.out.println("==== Withdraw Amount ====");
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();

        String sql = "SELECT balance FROM accounts WHERE account_number = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, accountNumber);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            double balance = rs.getDouble("balance");
            if (balance >= amount) {
                sql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setDouble(1, amount);
                pstmt.setInt(2, accountNumber);
                pstmt.executeUpdate();
                System.out.println("Amount withdrawn successfully!");
            } else {
                System.out.println("Insufficient balance!");
            }
        } else {
            System.out.println("Account not found!");
        }
    }

    static void checkBalance() throws SQLException {
        System.out.println("==== Check Balance ====");
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();

        String sql = "SELECT balance FROM accounts WHERE account_number = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, accountNumber);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            double balance = rs.getDouble("balance");
            System.out.println("Current balance: " + balance);
        } else {
            System.out.println("Account not found!");
        }
    }
}

