package Vue;

import DAO.MembreDAO;
import Exceptions.DAOException;
import Model.Membre;

import java.util.Scanner;

public class Gestion_membre {
    public static void gestionMembresMenu(MembreDAO memberDAO, Scanner scanner) throws DAOException {
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
                    try {
                        memberDAO.createMember(membre);
                    } catch (DAOException.DuplicateException e) {
                        System.out.println("Member already exists.");
                    } catch (DAOException e) {
                        System.out.println("Error while creating the book: " + e.getMessage());
                    }
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
}
