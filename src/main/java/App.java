import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

public class App {
    public static EntityManagerFactory emf;
    public static EntityManager em;

    public static void main (String[] args) {
        boolean exit = false;
        emf = Persistence.createEntityManagerFactory("Bank");
        em = emf.createEntityManager();
        if (!checkCurrencies()) {
            Currencies currency = new Currencies("UAH", 27.50);
            Currencies currency1 = new Currencies("USD", 28.80);
            Currencies currency2 = new Currencies("EUR", 32.80);
            transactionCommit(currency);
            transactionCommit(currency1);
            transactionCommit(currency2);
        }
        Scanner sc = new Scanner(System.in);

        try {
                while (true) {
                    System.out.println("1) Register new user");
                    System.out.println("2) Login user");
                    System.out.println("q) Quit app");
                    String s = sc.nextLine();
                    switch (s) {
                        case "1" :
                            registerUser();
                            break;
                        case "2" :
                            login();
                            break;
                        case "q" :
                            exit = true;
                            break;
                        default:
                            break;
                    }
                if (exit) break;
                }
        } finally {
            em.close();
            emf.close();
        }

    }
    private static boolean checkCurrencies () {
        boolean result = false;
        List<Currencies> currencies;
        currencies = em.createQuery("SELECT c FROM Currencies c").getResultList();
        if (currencies.size() != 0) result = true;
        return result;
    }
    private static void getAccount(String name){
        List<Users> list = findAllUsers();
        Users user = new Users();
        for (Users users:list) {
            System.out.println(users.getName());
            if (users.getName().equalsIgnoreCase(name)) {
                    user = users;
            }
        }
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("1. Create account in UAH");
            System.out.println("2. Create account in USD");
            System.out.println("3. Create account in EUR");
            String s = sc.nextLine();
            switch (s) {
                case "1" :
                    replenishAccount(user,"UAH");
                    break;
                case "2" :
                    replenishAccount(user,"USD");
                    break;
                case "3" :
                    replenishAccount(user,"EUR");
                    break;
                default:
                    break;
            }
            break;
        }
    }
    private static void replenishAccount(Users user,String currency) {
        Scanner sc = new Scanner(System.in);
        double amount;
        while (true) {
            System.out.println("What amount do you want to replenish?:");
            amount = sc.nextDouble();
            break;
        }
        Accounts account = new Accounts(currency,amount,user);
        transactionCommit(account);
    }
    private static void transactionCommit(Object c) {
        em.getTransaction().begin();
        try {
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }
    private static void menu (String name) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        while (true) {
            System.out.println("1) Get new account");
            System.out.println("2) Replenish accounts");
            System.out.println("3) Verify the balance of all accounts in UAH");
            System.out.println("4) Make transaction");
            System.out.println("q) Back to main menu");
            String s = sc.nextLine();
            switch (s) {
                case "1" :
                    getAccount(name);
                    break;
                case "2" :
                    checkAccounts(name);
                    break;
                case "3" :
                    System.out.println(userTotalAmount(name) + " " + "UAH");
                    break;
                case "4" :
                    makeTransaction(name);
                    break;
                case "q" :
                    exit = true;
                    break;
            }
            if (exit) break;
        }
    }
    private static void makeTransaction (String name) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Input account you want to make transaction to:");
            String number = sc.nextLine();
            if (checkAccount(number)){
                Accounts recipient = findAccount(number);
                chooseAccount(recipient,name);
                break;
            } else {
                System.out.println("Incorrect account number!");
                break;
            }
        }
    }
    private static void chooseAccount (Accounts recipient,String sender) {
        List<Accounts> list = findAccountByName(sender);
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Choose account you want to make transaction from:");
            for (int i = 0; i < list.size(); i++) {
                System.out.println(i+1 + ")" + " " + list.get(i).getId() + " " + list.get(i).getCurrency() + " " + list.get(i).getAmount());
            }
            System.out.println("q) back to previous menu");
            String s = sc.nextLine();
            if (!s.equals("") && !s.equalsIgnoreCase("q")) {
                for (int i = 0; i < list.size(); i++) {
                    if (Integer.parseInt(s) == i+1) {
                        transactSum(recipient,list.get(i));
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }
    private static void transactSum (Accounts recipient, Accounts sender) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("What amount you want to transact?");
            String amount = sc.nextLine();
            if (checkAmount(sender,Double.parseDouble(amount))) {
                createTransaction(sender,recipient,Double.parseDouble(amount));
                break;
            } else {
                System.out.println("You do not have enough cash on selected account");
            }
        }
    }
    private static boolean checkAccount (String number) {
        boolean result = false;
        if (!number.equals("")) {
            if (Long.parseLong(number) == findAccount(number).getId()) {
                result = true;
            }
        }
        return result;
    }
    private static Accounts findAccount (String number) {
        return em.createQuery("SELECT a FROM Accounts a WHERE a.id=?1",Accounts.class).setParameter(1,Long.parseLong(number)).getSingleResult();
    }
    private static void checkAccounts (String name) {
        Scanner sc = new Scanner(System.in);
        List<Accounts> accounts = findAccountByName(name);
        while (true) {
            System.out.println("Select account you want to replenish");
            for (int i = 0; i < accounts.size();i++) {
                System.out.println(i+1  + ")" + " " + accounts.get(i).getId() + " " + accounts.get(i).getCurrency() + " " + accounts.get(i).getAmount());
            }
            System.out.println("q) back to previous menu");
            String s = sc.nextLine();
            if (!s.equals("") && !s.equalsIgnoreCase("q")) {
                for (int i = 0; i < accounts.size(); i++) {
                    if (Integer.parseInt(s) == i + 1) {
                        replenishUserAccount(accounts.get(i));
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }
    private static void replenishUserAccount(Accounts account) {
        Scanner sc = new Scanner(System.in);
        double amount;
        while (true) {
            System.out.println("What amount do you want to replenish?:");
            amount = sc.nextDouble();
            break;
        }
        account.setAmount(account.getAmount() + amount);
        transactionCommit(account);

    }
    private static void login() {
        Scanner sc = new Scanner(System.in);
        List<Users> list = findAllUsers();
        while (true) {
            System.out.println("Enter your login");
           String name = sc.nextLine();
            if (list.size() == 0) {
                System.out.println("There are no users registered");
                System.out.println("Please register");
            } else {
                menu(name);
           }
            break;
        }
    }
    private static void registerUser() {
        Scanner sc = new Scanner(System.in);
        String email = "";
        String phone = "";
        boolean check = false;
        while (true) {
                System.out.println("Enter your name:");
                String name = sc.nextLine();
            for (Users user: findAllUsers()) {
                if (user.getName().equalsIgnoreCase(name)){
                    check = true;
                }
            }
            if (check) {
                System.out.println("There is already registered user with same name");
            } else {
                System.out.println("Enter your e-mail:");
                email = sc.nextLine();
                System.out.println("Enter your phone:");
                phone = sc.nextLine();
            }
            if (checkCredentials(name,email,phone)) {
                registerCommit(name, Long.parseLong(phone), email);
                menu(name);
            }
            break;
        }
    }
    private static boolean checkCredentials (String name, String email, String phone) {
        boolean result = false;
        if (name.equals("")) System.out.println("Name cannot be blank");
        if (checkName(name) && checkEmail(email) && checkPhone(phone)) {
            result = true;
        }
        return result;
    }
    private static boolean checkName(String name) {
        boolean result = false;
        boolean error = false;
        for (Users user: findAllUsers()) {
            if (user.getName().equalsIgnoreCase(name)) {
                error = true;
            }
        }
        if (!error) {
            result = true;
        } else {
            System.out.println("Name already registered");
        }
        return result;
    }
    private static boolean checkPhone(String phone) {
        boolean result = false;
        boolean error = false;
        if (phone.length() == 10) {
            for (int i = 0; i < phone.length() - 1; i++) {
                if (!Character.isDigit(phone.charAt(i))) {
                    error = true;
                }
            }
        } else {
            error = true;
        }
        if (!error) {
            result = true;
        } else {
            System.out.println("Phone number is incorrect");
        }
        return result;
    }
    private static boolean checkEmail (String email) {
        boolean result = false;
        boolean check = false;
        for (int i = 0; i<email.length() -1;i++) {
            if (email.charAt(i) == '@') check = true;
        }
        if (check) {
            result = true;
        } else {
            System.out.println("Your e-mail address is incorrect");
        }
        return result;
    }
    private static void registerCommit(String name,long phone,String email) {
        if (name.length() != 0) {
                Users user = new Users(name, phone, email);
                transactionCommit(user);
        }
    }
    private static List<Users> findAllUsers () {
                return em.createQuery("SELECT a FROM Users a", Users.class).getResultList();
    }

    private static List<Accounts> findAccountByName (String name) {
                Users user = (Users) em.createQuery("SELECT u FROM Users u WHERE u.name=:name").setParameter("name",name).getSingleResult();
                long userId = user.getId();
                return em.createQuery("SELECT a FROM Accounts a WHERE a.user.id=?1").setParameter(1,userId).getResultList();
    }

    private static void createTransaction (Accounts sender,Accounts recipient, double amount) {
                Transactions transaction = new Transactions(sender,recipient,amount);
                double current;
                if (checkAmount(transaction.getSender(),amount)) {
                    if (transaction.getRecipient().getCurrency().equalsIgnoreCase(transaction.getSender().getCurrency())) {
                        sender.setAmount(sender.getAmount() - amount);
                        recipient.setAmount(recipient.getAmount() + amount);
                        transactionCommit(transaction);
                        //em.persist(transaction);
                        //em.getTransaction().commit();
                    } else {
                        current = convertToCurrency(sender, recipient, amount);
                        transaction.setAmount(current);
                        sender.setAmount(sender.getAmount() - amount);
                        recipient.setAmount(recipient.getAmount() + current);
                        transactionCommit(transaction);
                        //em.persist(transaction);
                       // em.getTransaction().commit();
                    }
                }
       }
       private static Boolean checkAmount (Accounts sender, double amount) {
        boolean result = false;
        if (sender.getAmount() > amount) {
            result = true;
        }
        return result;
       }
       private static double getCurrentCurrency (String name) {
        Currencies currency = (Currencies) em.createQuery("SELECT c FROM Currencies c WHERE c.name=:name").setParameter("name",name).getSingleResult();
            return currency.getCurrent();
       }
       private static double convertToCurrency (Accounts sender,Accounts recipient,double amount) {
        double result;
        double recipientCurrency = getCurrentCurrency(recipient.getCurrency());
        if (!sender.getCurrency().equalsIgnoreCase("UAH")) {
            result = amount * recipientCurrency;
        } else {
            result = amount / recipientCurrency;
        }

        return result;
       }
       private static double userTotalAmount (String name) {
        double result = 0;
        List<Accounts> list = findAccountByName(name);
        for (Accounts account:list) {
            String currency = account.getCurrency();
            double amount = account.getAmount();
            if (!currency.equalsIgnoreCase("UAH")) {
                result = result + amount * getCurrentCurrency(currency);
            } else {
                result = amount;
            }
        }
        return result;
       }
}
