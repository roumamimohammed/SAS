import Connection.ConnectionManager;
import DAO.EmpruntDAO;
import DAO.LivreDAO;
import DAO.MembreDAO;
import Exceptions.DAOException;
import java.util.*;
import static Vue.Gestion_Emprunt.empruntMenu;
import static Vue.Gestion_livre.gestionLivresMenu;
import static Vue.Gestion_membre.gestionMembresMenu;

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
                scanner.nextLine();
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            switch (choice) {
                case 1 -> gestionLivresMenu(livreDAO, scanner, empruntDAO);
                case 2 -> gestionMembresMenu(memberDAO, scanner);
                case 3 -> empruntMenu(empruntDAO, livreDAO, memberDAO, scanner);
                case 4 -> livreDAO.displayStatistics();
                case 5 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}