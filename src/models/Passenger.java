package models;

public class Passenger extends User {

    public Passenger(int id, String firstName, String lastName, String email, String phone,
                     String username, String password) {
        super(id, firstName, lastName, email, phone, username, password, "passenger");
    }

    // Removed @Override since User doesn't define a login() method
    public void login() {
        System.out.println("Passenger " + getUsername() + " logged in.");
    }
}
