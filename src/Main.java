import java.util.Scanner;
import java.util.List;
import DAO.LivreDAO;
import Model.Livre;
import Model.Status;
import Connection.ConnectionManager;

public class Main {
    public static void main(String[] args) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        LivreDAO livreDAO = new LivreDAO(connectionManager.getConnection());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Options:");
            System.out.println("1. Create Livre");
            System.out.println("2. Read All Livres");
            System.out.println("3. Update Livre");
            System.out.println("4. Delete Livre");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    scanner.nextLine(); // Consume newline character
                    System.out.print("Enter titre: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter isbn: ");
                    String isbn = scanner.nextLine();
                    Status newStatu = null;
                    Livre livre = new Livre(title, author, isbn, newStatu);
                    livreDAO.createLivre(livre);
                    break;

                case 2:
                    List<Livre> allLivres = livreDAO.getAllLivres();
                    for (Livre l : allLivres) {
                        System.out.println("Titre: " + l.getTitle());
                        System.out.println("Author: " + l.getAuthor());
                        System.out.println("ISBN: " + l.getIsbn());
                        System.out.println("Status: " + l.getStatus());
                        System.out.println();
                    }
                    break;
                case 3:
                    scanner.nextLine(); // Consume newline character
                    System.out.print("Enter ISBN of the Livre to update: ");
                    String isbnToUpdate = scanner.nextLine();

                    // Retrieve the existing Livre to display current information
                    Livre existingLivre = null;
                    List<Livre> existingLivres = livreDAO.getAllLivres();
                    for (Livre l : existingLivres) {
                        if (l.getIsbn().equals(isbnToUpdate)) {
                            existingLivre = l;
                            break;
                        }
                    }

                    if (existingLivre == null) {
                        System.out.println("Livre with ISBN " + isbnToUpdate + " not found.");
                        break;
                    }

                    System.out.println("Current Titre: " + existingLivre.getTitle());
                    System.out.print("Enter new titre (or press Enter to keep current): ");
                    String newTitle = scanner.nextLine();
                    if (newTitle.isEmpty()) {
                        newTitle = existingLivre.getTitle();
                    }

                    System.out.println("Current Author: " + existingLivre.getAuthor());
                    System.out.print("Enter new author (or press Enter to keep current): ");
                    String newAuthor = scanner.nextLine();
                    if (newAuthor.isEmpty()) {
                        newAuthor = existingLivre.getAuthor();
                    }
                    System.out.println("Current Status: " + existingLivre.getStatus());
                    System.out.println("Status Options: disponible, eumprinter, perdu");
                    System.out.print("Enter new status (or press Enter to keep current): ");
                    String newStatusInput = scanner.nextLine();
                    Status newStatus = existingLivre.getStatus(); // Default to current status
                    if (!newStatusInput.isEmpty()) {
                        try {
                            newStatus = Status.valueOf(newStatusInput);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid status input. Keeping current status.");
                        }
                    }
                    System.out.println("Updated Status: " + newStatus);

                    Livre updatedLivre = new Livre(newTitle, newAuthor, isbnToUpdate, newStatus);
                    livreDAO.updateLivre(updatedLivre);
                    break;


                case 4:
                    scanner.nextLine(); // Consume newline character
                    System.out.print("Enter ISBN of the Livre to delete: ");
                    String isbnToDelete = scanner.nextLine();
                    livreDAO.deleteLivre(isbnToDelete);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
