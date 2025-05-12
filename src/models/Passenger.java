package models;

/**
 * ✅ Design Pattern: Inheritance / Factory Method (via polymorphism)
 * ------------------------------------------------------------------
 * `Passenger` is a **subclass** of `User`, utilizing **inheritance** to reuse core user properties
 * and optionally override behavior (like login logic).
 *
 * 🔍 Why this is useful:
 * - Supports polymorphism: you can treat all user types (Admin, Passenger) as `User`.
 * - Encourages separation of responsibilities between user roles.
 * - Can be extended via a **Factory Pattern** to instantiate specific user types.
 */
public class Passenger extends User {

    public Passenger(int id, String firstName, String lastName, String email, String phone,
                     String username, String password) {
        super(id, firstName, lastName, email, phone, username, password, "passenger");
    }

    // Custom login behavior specific to passengers
    public void login() {
        System.out.println("Passenger " + getUsername() + " logged in.");
    }
}
