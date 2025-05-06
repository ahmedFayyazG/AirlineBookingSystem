import db.DatabaseManager;
import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        try (Connection conn = DatabaseManager.getConnection()) {
            System.out.println("✅ Connected to PostgreSQL!");
        } catch (Exception e) {
            System.out.println("❌ Failed to connect.");
            e.printStackTrace();
        }
    }
}
