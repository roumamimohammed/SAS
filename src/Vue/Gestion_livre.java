package Vue;

import Model.Livre;
import Model.Status;
import DAO.EmpruntDAO;
import DAO.LivreDAO;
import Exceptions.DAOException;
import java.util.*;

public class Gestion_livre {
    public static void gestionLivresMenu(LivreDAO livreDAO, Scanner scanner, EmpruntDAO empruntDAO) throws DAOException {
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
                scanner.nextLine();
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
                    try {
                        livreDAO.createLivre(livre);

                    } catch (DAOException.DuplicateException e) {
                        System.out.println("ISBN already exists.");
                    } catch (DAOException e) {
                        System.out.println("Error while creating the book: " + e.getMessage());
                    }
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
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
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
    private static void displayLostLivres(LivreDAO livreDAO) throws DAOException {
        List<Livre> lostLivres = livreDAO.getLostLivres();
        if (lostLivres.isEmpty()) {
            System.out.println("No books are marked as lost.");
        } else {
            System.out.println("Lost Books:");
            lostlivre(lostLivres);
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
}
