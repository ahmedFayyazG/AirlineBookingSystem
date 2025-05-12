package models;

/**
 * ✅ Design Pattern: Inheritance (Object-Oriented Design Principle)
 * ---------------------------------------------------------------
 * This class demonstrates a **specialization** of the `User` class.
 * 
 * 🔍 Why Inheritance?
 * - It allows Admin to reuse common user attributes (id, name, credentials, etc.).
 * - Enables **polymorphic behavior** where Admin can override or extend functionality like `login()`.
 * - Makes it easy to identify Admin-specific responsibilities while keeping base logic in `User`.
 */
public class Admin extends User {
    
    public Admin(int id, String firstName, String lastName, String email, String phone,
                 String username, String password) {
        // Call to parent constructor with role "admin"
        super(id, firstName, lastName, email, phone, username, password, "admin");
    }

    // Overridable method for specific behavior
    public void login() {
        System.out.println("Admin " + getUsername() + " logged in.");
    }
}
