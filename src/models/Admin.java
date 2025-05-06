package models;

public class Admin extends User {
    public Admin(int id, String firstName, String lastName, String email, String phone,
                 String username, String password) {
        super(id, firstName, lastName, email, phone, username, password, "admin");
    }

    public void login() {
        System.out.println("Admin " + getUsername() + " logged in.");
    }
}
