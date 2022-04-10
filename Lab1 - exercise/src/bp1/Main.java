package bp1;

public class Main {

    public static void main(String[] args) {
	SQlite_java database = new SQlite_java();
    database.connect();

    database.printAllUsers();

    database.printAllUsers();

    database.disconnect();
    }
}
