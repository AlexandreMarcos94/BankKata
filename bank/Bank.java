package bank;


import java.sql.*;
import java.util.Arrays;

public class Bank {

    /*
        Strings de connection à la base postgres
     */
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5439/postgres";
    private static final String DB_USER = "postgres";

    /*
        Strings de connection à la base mysql, à décommenter et compléter avec votre nom de bdd et de user
     */
    // private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    // private static final String DB_URL = "jdbc:mysql://localhost:3306/bank_db";
    // private static final String DB_USER = "bank_user";

    private static final String DB_PASS = "1234";

    private static final String TABLE_NAME = "accounts";

    private Connection c;
    private String error_creation = "ERROR: relation \"accounts\" already exists";
    private String error_nothing_return = "Aucun résultat retourné par la requête.";

    public Bank() {
        initDb();
        // TODO
    }

    private void initDb() {
        try {
            Class.forName(JDBC_DRIVER);
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Opened database successfully");
            try (Statement s = c.createStatement()) {
                s.executeQuery("CREATE TABLE accounts(" +       // Create table accounts
                        "name VARCHAR(30), " +
                        "balance INTEGER, " +
                        "threshold INTEGER, " +
                        "isblock BOOLEAN DEFAULT FALSE);"
                );
            } catch (Exception f){
                if(!f.getMessage().equals(error_creation) && !f.getMessage().equals(error_nothing_return)){ // don't show following error: error_creation et error_creation2
                    System.out.println(f.getMessage());

                }
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void closeDb() {
        try {
            c.close();
        } catch (SQLException e) {
            System.out.println("Could not close the database : " + e);
        }
    }

    void dropAllTables() {
        try (Statement s = c.createStatement()) {
            s.executeUpdate(
                    "DROP SCHEMA public CASCADE;" +
                            "CREATE SCHEMA public;" +
                            "GRANT ALL ON SCHEMA public TO postgres;" +
                            "GRANT ALL ON SCHEMA public TO public;");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    public void createNewAccount(String name, Integer balance, Integer threshold) {
       if(threshold <= 0 && balance >= 0 && !name.equals("")){
           try (Statement s = c.createStatement()) {
               s.executeUpdate("INSERT INTO accounts (name, balance, threshold) VALUES ('" + name + "', " + balance + ", " + threshold + ");");     //  Put the new account in the db
           } catch (Exception e) {
               System.out.println(e.toString());
           }
       } else {
           System.out.println("Invalid Inputs");
       }
    }


//                  DEMANDER POUR LE ACCOUNT = NEW ACCOUNT();

    public String printAllAccounts() {
        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery("SELECT * FROM accounts");    // Select all acounts in database
            String printAccount = "";       // Set the default string if nothing is return by the query

            while (rs.next()){
                String accountName = rs.getString("NAME");
                int accountBalance = rs.getInt("BALANCE");
                int accountThreshold = rs.getInt("THRESHOLD");
                boolean isblock = rs.getBoolean("isblock");
                printAccount = printAccount + accountName + " | " + accountBalance + " | " + accountThreshold + " | " + isblock + "\n";     //      Concatenation of all returned values;
            }
            return printAccount;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "";
    }

    public void changeBalanceByName(String name, int balanceModifier) {
        try (Statement s = c.createStatement()){
            ResultSet res = s.executeQuery("SELECT balance, isblock, threshold FROM accounts WHERE name = '" + name + "'"); // Get the accounts by the name, error return if doesn't exist
            res.next();         // res.next() Put the cursor on the first row
            boolean isblock = res.getBoolean("isblock");
            Integer balance = res.getInt("balance");
            Integer threshold = res.getInt("threshold");
            balance += balanceModifier;
            boolean isThresholdOk = threshold <= balance;       //      Check if the value of the balance is not below than threshold
            if(!isblock && isThresholdOk){
                try (Statement f = c.createStatement()){
                    f.executeQuery("UPDATE accounts SET balance = " + balance + "WHERE name = '" + name + "'");         // If its good Update the acounts with the new balance
                    System.out.println("Process finish with success");
                } catch (Exception d) {
                    if(!d.getMessage().equals(error_nothing_return)){
                        System.out.println("Error during the process" + d);
                    }
                }
            } else {
                System.out.println("The account is blocked you can't change the balance");
            }
        } catch (Exception e) {
            System.out.println("The account doesn't exist.");
        }
    }

    public void blockAccount(String name) {
        try (Statement s = c.createStatement()){
            ResultSet res = s.executeQuery("SELECT * FROM accounts WHERE name = '" + name + "'");
                res.next();
                boolean isblock = res.getBoolean("isblock");    // Check if the account is already blocked
                if(!isblock){
                    try (Statement f = c.createStatement()){
                        f.executeQuery("UPDATE accounts SET isblock = true WHERE name = '" + name + "'");       //      Change the statement of the accounts on blocked
                        System.out.println("Process finish with success");
                    } catch (Exception d) {
                        if(!d.getMessage().equals(error_nothing_return)){
                            System.out.println("Error during the process" + d);
                        }
                    }
                } else {
                    System.out.println("The account is already blocked");
                }
        } catch (Exception e) {
            System.out.println("The account doesn't exist :");
        }
    }

    // For testing purpose
    String getTableDump() {
        String query = "select * from " + TABLE_NAME;
        String res = "";

        try (PreparedStatement s = c.prepareStatement(query)) {
            ResultSet r = s.executeQuery();

            // Getting nb colmun from meta data
            int nbColumns = r.getMetaData().getColumnCount();

            // while there is a next row
            while (r.next()) {
                String[] currentRow = new String[nbColumns];

                // For each column in the row
                for (int i = 1; i <= nbColumns; i++) {
                    currentRow[i - 1] = r.getString(i);
                }
                res += Arrays.toString(currentRow);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return res;
    }
}
