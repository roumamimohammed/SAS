import Model.Membre;
import Model.Livre;
import Model.Status;
import Connection.ConnectionManager;
import DAO.EmpruntDAO;
import DAO.LivreDAO;
import DAO.MembreDAO;
import Exceptions.DAOException;
import java.util.*;
import java.util.Date;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) throws DAOException {

        ConnectionManager connectionManager = ConnectionManager.getInstance();
        LivreDAO livreDAO = new LivreDAO(connectionManager.getConnection());
        MembreDAO memberDAO = new MembreDAO(connectionManager.getConnection());
        EmpruntDAO empruntDAO = new EmpruntDAO(connectionManager.getConnection());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Gestion des Livres");
            System.out.println("2. Gestion des Membres");
            System.out.println("3. Emprunt Livre");
            System.out.println("4. Afficher Statistiques");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                scanner.nextLine(); // Consume invalid input
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            switch (choice) {
                case 1 -> gestionLivresMenu(livreDAO, scanner, empruntDAO);
                case 2 -> gestionMembresMenu(memberDAO, scanner);
                case 3 -> empruntMenu(empruntDAO, livreDAO, memberDAO, scanner);
                case 4 -> displayStatistics(livreDAO);
                case 5 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void gestionLivresMenu(LivreDAO livreDAO, Scanner scanner, EmpruntDAO empruntDAO) throws DAOException {
        while (true) {
            System.out.println("Gestion des Livres:");
            System.out.println("1. Add Livre");
            System.out.println("2. Afficher les livres disponibles");
            System.out.println("3. Update Livre");
            System.out.println("4. Delete Livre");
            System.out.println("5. Search Livre");
            System.out.println("6. Afficher Livres Empruntés");
            System.out.println("7. Mark Livre as Lost");
            System.out.println("8. Afficher Livres Perdus");
            System.out.println("9. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                scanner.nextLine(); // Consume invalid input
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    scanner.nextLine();
                    System.out.print("Enter titre: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter isbn: ");
                    String isbn = scanner.nextLine();
                    Livre livre = new Livre(title, author, isbn, Status.disponible);
                    livreDAO.createLivre(livre);
                }
                case 2 -> {
                    List<Livre> allLivres = livreDAO.getAllAvailableLivres();
                    for (Livre l : allLivres) {
                        System.out.println("Titre: " + l.getTitle());
                        System.out.println("Author: " + l.getAuthor());
                        System.out.println("ISBN: " + l.getIsbn());
                        System.out.println("Status: " + l.getStatus());
                        System.out.println();
                    }
                }
                case 3 -> {
                    scanner.nextLine();
                    System.out.print("Enter ISBN of the Livre to update: ");
                    String isbnToUpdate = scanner.nextLine();
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
                    System.out.println("Status Options: disponible, emprunt, perdu");
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
                }
                case 4 -> {
                    scanner.nextLine();
                    System.out.print("Enter ISBN of the Livre to delete: ");
                    String isbnToDelete = scanner.nextLine();
                    livreDAO.deleteLivre(isbnToDelete);
                }
                case 5 -> {
                    scanner.nextLine();
                    System.out.print("Enter a keyword to search by title or author: ");
                    String keyword = scanner.nextLine();
                    List<Livre> matchingLivres = livreDAO.searchLivresByTitleOrAuthor(keyword);
                    if (matchingLivres.isEmpty()) {
                        System.out.println("No matching Livres found.");
                    } else {
                        System.out.println("Matching Livres:");
                        lostlivre(matchingLivres);
                    }
                }
                case 6 -> displayBorrowedLivres(empruntDAO, livreDAO);
                case 7 -> {
                    scanner.nextLine();
                    System.out.print("Enter ISBN of the Livre to mark as lost: ");
                    String isbnToMarkAsLost = scanner.nextLine();
                    livreDAO.markLivreAsLost(isbnToMarkAsLost);
                }
                case 8 -> displayLostLivres(livreDAO);
                case 9 -> {
                    return; // Back to Main Menu
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void gestionMembresMenu(MembreDAO memberDAO, Scanner scanner) throws DAOException {
        while (true) {
            System.out.println("Gestion des Membres:");
            System.out.println("1. Add Membre");
            System.out.println("2. Update Membre");
            System.out.println("3. Delete Membre");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                scanner.nextLine(); // Consume invalid input
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    scanner.nextLine();
                    System.out.print("Enter numéro de membre: ");
                    int numMembre = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter nom du membre: ");
                    String nomMembre = scanner.nextLine();
                    Membre membre = new Membre(numMembre, nomMembre);
                    memberDAO.createMember(membre);
                }
                case 2 -> {
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
                }
                case 3 -> {
                    scanner.nextLine();
                    System.out.print("Entrez le numéro de membre à supprimer: ");
                    int numeroMembreToDelete = scanner.nextInt();
                    memberDAO.deleteMember(numeroMembreToDelete);
                }
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void empruntMenu(EmpruntDAO empruntDAO, LivreDAO livreDAO, MembreDAO memberDAO, Scanner scanner) throws DAOException {
        while (true) {
            System.out.println("emprunt Livre:");
            System.out.println("1. emprunt Livre");
            System.out.println("2. Return Livre");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                scanner.nextLine(); // Consume invalid input
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            switch (choice) {
                case 1 -> empruntLivre(empruntDAO, livreDAO, memberDAO, scanner);
                case 2 -> returnLivre(empruntDAO, scanner);
                case 3 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayBorrowedLivres(EmpruntDAO empruntDAO, LivreDAO livreDAO) throws DAOException {
        List<Livre> borrowedLivres = livreDAO.getBorrowedLivres();
        if (borrowedLivres.isEmpty()) {
            System.out.println("No books are currently borrowed.");
        } else {
            System.out.println("Borrowed Books:");
            lostlivre(borrowedLivres);
        }
    }

    private static void displayLostLivres(LivreDAO livreDAO) throws DAOException {
        List<Livre> lostLivres = livreDAO.getLostLivres();
        if (lostLivres.isEmpty()) {
            System.out.println("No books are marked as lost.");
        } else {
            System.out.println("Lost Books:");
            lostlivre(lostLivres);
        }
    }

    private static void empruntLivre(EmpruntDAO empruntDAO, LivreDAO livreDAO, MembreDAO memberDAO, Scanner scanner) throws DAOException {
        scanner.nextLine();
        System.out.print("Enter ISBN of the Livre to emprunt: ");
        String isbnToemprunt = scanner.nextLine();
        System.out.print("Enter member Numero_membre to emprunt to: ");
        int memberNumeroToemprunt = scanner.nextInt();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MINUTE, 1);
        Date returnDate = calendar.getTime();
        empruntDAO.empruntLivre(isbnToemprunt, memberNumeroToemprunt, currentDate, returnDate);
        System.out.println("Livre emprunted successfully.");
    }

    private static void returnLivre(EmpruntDAO empruntDAO, Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter ISBN of the Livre to return: ");
        String isbnToReturn = scanner.nextLine();
        empruntDAO.returnLivre(isbnToReturn);
        System.out.println("Livre returned successfully.");
    }

    private static void lostlivre(List<Livre> lostLivres) {
        for (Livre livre : lostLivres) {
            System.out.println("Title: " + livre.getTitle());
            System.out.println("Author: " + livre.getAuthor());
            System.out.println("ISBN: " + livre.getIsbn());
            System.out.println("Status: " + livre.getStatus());
            System.out.println();
        }
    }

    private static void displayStatistics(LivreDAO livreDAO) throws DAOException {
        livreDAO.displayStatistics();
    }
}
