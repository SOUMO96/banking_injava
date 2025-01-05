import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class banking {
    static final int MAX_NAME = 100;

    static class BankingUpdate {
        String name;
        int age, balance;
        long accountNumber, phoneNumber;
        char gender;
        String category, loginName, hashedPassword;
    }

    static BankingUpdate[] bankingUpdates = new BankingUpdate[MAX_NAME];
    static int count = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nWELCOME");
            System.out.println("1) ADD USER");
            System.out.println("2) CHECK BALANCE");
            System.out.println("3) DEPOSIT");
            System.out.println("4) WITHDRAWAL");
            System.out.println("5) EXIT!");
            System.out.print("ENTER YOUR CHOICE: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addAccount();
                    break;
                case 2:
                    fetchData();
                    break;
                case 3:
                    updateBalance();
                    break;
                case 4:
                    withdrawal();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("INVALID INPUT!");
            }
        }
    }

    static long generateRandom() {
        long num1 = (long) (Math.random() * 900000) + 1000000;
        long num2 = (long) (Math.random() * 9000000) + 10000000;
        return num1 * 10000000 + num2;
    }

    static void addAccount() {
        if (count >= MAX_NAME) {
            System.out.println("SIZE NOT SUPPORTED!");
            return;
        }
        Scanner sc = new Scanner(System.in);

        BankingUpdate newAccount = new BankingUpdate();

        System.out.print("ENTER YOUR NAME: ");
        newAccount.name = sc.nextLine();

        System.out.print("ENTER YOUR GENDER (M/F): ");
        newAccount.gender = sc.next().charAt(0);

        System.out.print("ENTER YOUR CATEGORY: ");
        sc.nextLine();
        newAccount.category = sc.nextLine();

        System.out.print("ENTER YOUR AGE: ");
        newAccount.age = sc.nextInt();

        if (newAccount.age < 18) {
            System.out.println("AGE MUST BE AT LEAST 18 YEARS!");
            System.out.println("CANNOT ADD USER");
            return;
        }

        System.out.print("ENTER YOUR PHONE NUMBER: ");
        newAccount.phoneNumber = sc.nextLong();

        if (String.valueOf(newAccount.phoneNumber).length() != 10) {
            System.out.println("Invalid phone number. Please enter a 10-digit number.");
            return;
        }

        System.out.print("ENTER USERNAME: ");
        sc.nextLine();
        newAccount.loginName = sc.nextLine();

        System.out.print("ENTER A PASSWORD: ");
        newAccount.hashedPassword = getPassword();

        System.out.print("ENTER THE AMOUNT YOU WANT TO DEPOSIT: ");
        newAccount.balance = sc.nextInt();

        newAccount.accountNumber = generateRandom();

        System.out.println("ACCOUNT NUMBER: " + newAccount.accountNumber);
        System.out.println("ACCOUNT CREATED SUCCESSFULLY!");

        bankingUpdates[count] = newAccount;
        count++;
    }

    public static String getPassword() {
        Scanner scanner = new Scanner(System.in);
        StringBuilder password = new StringBuilder();
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error initializing encryption algorithm.");
            return null;
        }

        while (true) {
            try {
                char ch = (char) System.in.read();

                if (ch == '\n' || ch == '\r') {
                    break;
                } else if (ch == '\b' || ch == 127) {
                    if (password.length() > 0) {
                        password.deleteCharAt(password.length() - 1);
                        System.out.print("\b \b");
                    }
                } else {
                    password.append(ch);
                    System.out.print("*");
                    md.update((byte) ch);
                }
            } catch (Exception e) {
                System.out.println("Error reading password input.");
                break;
            }
        }
        System.out.println();

        byte[] hashedPassword = md.digest();

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedPassword) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    static void fetchData() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ENTER USERNAME: ");
        String userName = sc.nextLine();

        System.out.print("ENTER YOUR PASSWORD: ");
        String hashedPasswordInput = getPassword();

        boolean found = false;

        for (int i = 0; i < count; i++) {
            if (bankingUpdates[i].loginName.equals(userName) &&
                    bankingUpdates[i].hashedPassword.equals(hashedPasswordInput)) {
                System.out.println("ACCOUNT FOUND!");
                System.out.println("NAME: " + bankingUpdates[i].name);
                System.out.println("GENDER: " + bankingUpdates[i].gender);
                System.out.println("CATEGORY: " + bankingUpdates[i].category);
                System.out.println("AGE: " + bankingUpdates[i].age);
                System.out.println("PHONE NUMBER: " + bankingUpdates[i].phoneNumber);
                System.out.println("BALANCE: " + bankingUpdates[i].balance);
                if (bankingUpdates[i].balance < 1500) {
                    System.out.println("Sufficient balance is not maintained!");
                }
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("WRONG USERNAME OR PASSWORD. TRY AGAIN!");
        }
    }

    static void updateBalance() {

        Scanner sc = new Scanner(System.in);
        System.out.print("ENTER USERNAME: ");
        String userName = sc.nextLine();

        System.out.print("ENTER YOUR PASSWORD: ");
        String hashedPasswordInput = getPassword();


        boolean found = false;

        for (int i = 0; i < count; i++) {
            if (bankingUpdates[i].loginName.equals(userName) &&
                    bankingUpdates[i].hashedPassword.equals(hashedPasswordInput)) {
                System.out.print("ENTER THE DEPOSIT AMOUNT: ");
                int depositAmount = sc.nextInt();

                bankingUpdates[i].balance += depositAmount;

                System.out.println("AMOUNT CREDITED SUCCESSFULLY!");
                System.out.println("NEW BALANCE: " + bankingUpdates[i].balance);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("ACCOUNT NUMBER NOT FOUND. PLEASE TRY AGAIN.");
        }
    }

    static void withdrawal() {
        Scanner sc = new Scanner(System.in);


        System.out.print("ENTER USERNAME: ");
        String userName = sc.nextLine();

        System.out.print("ENTER YOUR PASSWORD: ");
        String hashedPasswordInput = getPassword();

        boolean found = false;

        for (int i = 0; i < count; i++) {
            if (bankingUpdates[i].loginName.equals(userName) &&
                    bankingUpdates[i].hashedPassword.equals(hashedPasswordInput)) {
                System.out.print("ENTER THE WITHDRAWAL AMOUNT: ");
                int withdrawalAmount = sc.nextInt();

                if (bankingUpdates[i].balance >= withdrawalAmount) {
                    bankingUpdates[i].balance -= withdrawalAmount;

                    System.out.println("AMOUNT DEBITED SUCCESSFULLY!");
                    System.out.println("NEW BALANCE: " + bankingUpdates[i].balance);
                } else {
                    System.out.println("INSUFFICIENT BALANCE!");
                }
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("ACCOUNT NUMBER NOT FOUND. PLEASE TRY AGAIN.");
        }
    }
}
