import java.util.Scanner;
import java.util.List;
import DAO.LivreDAO;
import DAO.MembreDAO;
import DAO.EumprinterDAO;
import Model.Livre;
import Model.Membre;
import java.util.Date;
import java.util.Calendar;
import Model.Status;
import Connection.ConnectionManager;

public class Main {
    public static void main(String[] args) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        LivreDAO livreDAO = new LivreDAO(connectionManager.getConnection());
        MembreDAO memberDAO = new MembreDAO(connectionManager.getConnection());
        EumprinterDAO eumprinterDAO = new EumprinterDAO(connectionManager.getConnection());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Gestion des Livres");
            System.out.println("2. Gestion des Membres");
            System.out.println("3. Eumprinter Livre");
            System.out.println("4. Check for Perdu Livres");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    gestionLivresMenu(livreDAO, scanner);
                    break;
                case 2:
                    gestionMembresMenu(memberDAO, scanner);
                    break;
                case 3:
                    eumprinterMenu(eumprinterDAO, livreDAO, memberDAO, scanner);
                    break;
                case 4:
                    eumprinterDAO.checkForPerduLivres();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void gestionLivresMenu(LivreDAO livreDAO, Scanner scanner) {
        while (true) {
            System.out.println("Gestion des Livres:");
            System.out.println("1. Add Livre");
            System.out.println("2. Read All Livres");
            System.out.println("3. Update Livre");
            System.out.println("4. Delete Livre");
            System.out.println("5. Search Livre");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    scanner.nextLine();
                    System.out.print("Enter titre: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter isbn: ");
                    String isbn = scanner.nextLine();
                    Livre livre = new Livre(title, author, isbn, null); // No need to declare newStatus here
                    livreDAO.createLivre(livre);
                    break;
                case 2:
                    List<Livre> allLivres = livreDAO.getAllAvailableLivres();
                    for (Livre l : allLivres) {
                        System.out.println("Titre: " + l.getTitle());
                        System.out.println("Author: " + l.getAuthor());
                        System.out.println("ISBN: " + l.getIsbn());
                        System.out.println("Status: " + l.getStatus());
                        System.out.println();
                    }
                    break;
                case 3:
                    scanner.nextLine();
                    System.out.print("Enter ISBN of the Livre to update: ");
                    String isbnToUpdate = scanner.nextLine();

                    // Retrieve the existing Livre to display current information
                    Livre existingLivre = null;
                    List<Livre> existingLivres = livreDAO.getAllAvailableLivres();
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
                    System.out.println("Current Title: " + existingLivre.getTitle());
                    System.out.print("Enter new title (or press Enter to keep current): ");
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
                    scanner.nextLine();
                    System.out.print("Enter ISBN of the Livre to delete: ");
                    String isbnToDelete = scanner.nextLine();
                    livreDAO.deleteLivre(isbnToDelete);
                    break;
                case 5:
                    scanner.nextLine();
                    System.out.print("Enter a keyword to search by title or author: ");
                    String keyword = scanner.nextLine();
                    List<Livre> matchingLivres = livreDAO.searchLivresByTitleOrAuthor(keyword);
                    if (matchingLivres.isEmpty()) {
                        System.out.println("No matching Livres found.");
                    } else {
                        System.out.println("Matching Livres:");
                        for (Livre l : matchingLivres) {
                            System.out.println("Title: " + l.getTitle());
                            System.out.println("Author: " + l.getAuthor());
                            System.out.println("ISBN: " + l.getIsbn());
                            System.out.println("Status: " + l.getStatus());
                            System.out.println();
                        }
                    }
                    break;
                case 6:
                    return; // Back to Main Menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void gestionMembresMenu(MembreDAO memberDAO, Scanner scanner) {
        while (true) {
            System.out.println("Gestion des Membres:");
            System.out.println("1. Add Membre");
            System.out.println("2. Update Membre");
            System.out.println("3. Delete Membre");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    scanner.nextLine();
                    System.out.print("Enter numéro de membre: ");
                    int numMembre = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter nom du membre: ");
                    String nomMembre = scanner.nextLine();
                    Membre membre = new Membre(numMembre, nomMembre);
                    memberDAO.createMember(membre);
                    break;
                case 2:
                    scanner.nextLine();
                    System.out.print("Enter numéro de membre à mettre à jour: ");
                    int numeroMembreToUpdate = scanner.nextInt();
                    scanner.nextLine();
                    Membre existingMembre = memberDAO.getMemberByNumero(numeroMembreToUpdate);
                    if (existingMembre == null) {
                        System.out.println("Membre avec le numéro de membre " + numeroMembreToUpdate + " introuvable.");
                        break;
                    }
                    System.out.println("Nom actuel du membre: " + existingMembre.getNom());
                    System.out.print("Entrez le nouveau nom du membre (ou appuyez sur Entrée pour conserver l'actuel): ");
                    String newNomMembre = scanner.nextLine();
                    if (newNomMembre.isEmpty()) {
                        newNomMembre = existingMembre.getNom();
                    }
                    Membre updatedMembre = new Membre(numeroMembreToUpdate, newNomMembre);
                    memberDAO.updateMember(updatedMembre);
                    break;
                case 3:
                    scanner.nextLine();
                    System.out.print("Entrez le numéro de membre à supprimer: ");
                    int numeroMembreToDelete = scanner.nextInt();
                    memberDAO.deleteMember(numeroMembreToDelete);
                    break;
                case 4:
                    return; // Back to Main Menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void eumprinterMenu(EumprinterDAO eumprinterDAO, LivreDAO livreDAO, MembreDAO memberDAO, Scanner scanner) {
        while (true) {
            System.out.println("Eumprinter Livre:");
            System.out.println("1. Eumprint Livre");
            System.out.println("2. Return Livre");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    scanner.nextLine();
                    System.out.print("Enter ISBN of the Livre to eumprint: ");
                    String isbnToEumprint = scanner.nextLine();
                    System.out.print("Enter member Numero_membre to eumprint to: ");
                    int memberNumeroToEumprint = scanner.nextInt();
                    Date currentDate = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    calendar.add(Calendar.MINUTE, 1);
                    Date returnDate = calendar.getTime();
                    eumprinterDAO.eumprintLivre(isbnToEumprint, memberNumeroToEumprint, currentDate, returnDate);
                    break;
                case 2:
                    scanner.nextLine();
                    System.out.print("Enter ISBN of the Livre to return: ");
                    String isbnToReturn = scanner.nextLine();
                    eumprinterDAO.returnLivre(isbnToReturn);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}
