package Vue;

import DAO.EmpruntDAO;
import DAO.LivreDAO;
import DAO.MembreDAO;
import Exceptions.DAOException;
import java.util.*;
import java.util.Date;
import java.util.Calendar;
public class Gestion_Emprunt {



    public static void empruntMenu(EmpruntDAO empruntDAO, LivreDAO livreDAO, MembreDAO memberDAO, Scanner scanner) throws DAOException {
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
                scanner.nextLine();
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
    private static void empruntLivre(EmpruntDAO empruntDAO, LivreDAO livreDAO, MembreDAO memberDAO, Scanner scanner) throws DAOException {
        scanner.nextLine();
        System.out.print("Enter ISBN of the Livre to emprunt: ");
        String isbnToemprunt = scanner.nextLine();
        System.out.print("Enter member Numero_membre to emprunt to: ");
        int memberNumeroToemprunt = scanner.nextInt();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.HOUR, 176);
        Date returnDate = calendar.getTime();
        empruntDAO.empruntLivre(isbnToemprunt, memberNumeroToemprunt, currentDate, returnDate);
        System.out.println("Livre emprunted successfully.");
    }

    private static void returnLivre(EmpruntDAO empruntDAO, Scanner scanner) throws DAOException {
        scanner.nextLine();
        System.out.print("Enter ISBN of the Livre to return: ");
        String isbnToReturn = scanner.nextLine();
        empruntDAO.returnLivre(isbnToReturn);
        System.out.println("Livre returned successfully.");
    }

}
