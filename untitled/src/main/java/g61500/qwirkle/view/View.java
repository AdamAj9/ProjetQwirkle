package g61500.qwirkle.view;

import g61500.qwirkle.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe utilitaire pour l'affichage de la grille de jeu.
 */
public class View {

    /**
     * Affiche la grille de jeu.
     */
    public static void display(GridView grid) {
        // Obtient la grille et les indices min/max à afficher
        Grid g=grid.getGrid();
        List<Integer> indexes=grid.minMaxValues();
        int x1=indexes.get(0);
        int x2=indexes.get(1);
        int y1=indexes.get(2);
        int y2=indexes.get(3);

        // Code ANSI pour réinitialiser la couleur du texte
        String reset = "\u001B[0m";
        // Parcourt chaque ligne de la grille à afficher
        for (int i = x1; i <= x2; i++) {
            System.out.print(i);  // Affiche l'indice de la ligne
            for (int j = y1; j <= y2; j++) {
                if ( g.getTile(i, j) != null ) {
                    // Si une tuile existe à la position (i, j)
                    // Vérifie la forme de la tuile et affiche la couleur appropriée
                    if ( g.getTile(i, j).getShape() == Shape.DIAMOND ) {
                        String col=getColorId(g.getTile(i,j).getColor());
                        System.out.print(col+" < > "+reset);
                    } else if ( g.getTile(i, j).getShape() == Shape.CROSS ) {
                        String col=getColorId(g.getTile(i,j).getColor());
                        System.out.print(col+"  X  "+reset);
                    } else if ( g.getTile(i, j).getShape() == Shape.PLUS ) {
                        String col=getColorId(g.getTile(i,j).getColor());
                        System.out.print(col+"  +  "+reset);

                    } else if ( g.getTile(i, j).getShape() == Shape.ROUND ) {
                        String col=getColorId(g.getTile(i,j).getColor());
                        System.out.print(col+"  O  "+reset);
                    }else if ( g.getTile(i, j).getShape() == Shape.STAR ) {
                        String col=getColorId(g.getTile(i,j).getColor());
                        System.out.print(col+"  *  "+reset);

                    }else if ( g.getTile(i, j).getShape() == Shape.SQUARE) {
                        String col=getColorId(g.getTile(i,j).getColor());
                        System.out.print(col+" [ ] "+reset);
                    }
                }else{
                    System.out.print("  .  ");  // Aucune tuile à cette position
                }

            }
            System.out.println();  // Passe à la ligne suivante
        }
        for (int i = y1; i <=y2 ; i++){
            String formatted_i = String.format("%5d", i);
            System.out.print(formatted_i);
        }
        System.out.println();

    }
    // Méthode pour obtenir le code ANSI de couleur en fonction de la couleur donnée
    private static String getColorId(Color col) {
        switch (col) {
            case PURPLE:
                return "\u001B[35m";
            case RED:
                return "\u001B[31m";
            case BLUE:
                return "\u001B[34m";
            case YELLOW:
                return "\u001B[33m";
            case GREEN:
                return "\u001B[32m";
            case ORANGE:
                return "\u001B[38;5;208m";
            default:
                throw new IllegalStateException("Unexpected value: " + col);
        }
    }

    /**
     * Affiche la main du joueur, son score et gère les actions de remélange des tuiles.
     * paramètre player: le joueur dont la main doit être affichée
     * paramètre game: le jeu en cours
     */
    public static void display(Player player, Game game) {
        // Afficher le nom du joueur et sa main actuelle
        System.out.println(player.getName() + " : " + player.getHand());
        // Afficher le nom du joueur, sa main actuelle et son score
        System.out.println(player.getName() + " : " + player.getHand() + " - Score: " + player.getScore());
        // Afficher un message demandant au joueur s'il souhaite remélanger sa main
        System.out.println("Entrez 'r' pour remélanger votre main");
        // Créer un objet Scanner pour lire l'entrée utilisateur depuis la console
        Scanner scanner = new Scanner(System.in);
        // Lire l'entrée utilisateur
        String input = scanner.nextLine();
        // Vérifier si l'entrée utilisateur est "r" (pour remélanger la main)
        if (input.equalsIgnoreCase("r")) {
            // Demander au joueur de spécifier les indices des tuiles qu'il souhaite remettre dans le sac
            System.out.println("entrez l'index de la tuile que vous voulez remettre dans le sac):");
            input = scanner.nextLine();
            // Diviser l'entrée en une liste d'indices sous forme de chaînes de caractères
            String[] indexesStr = input.split(" ");
            List<Integer> indexes = new ArrayList<>();
            // Parcourir chaque indice spécifié par l'utilisateur
            for (String indexStr : indexesStr) {
                try {
                    // Convertir l'indice en entier
                    int index = Integer.parseInt(indexStr);
                    // Vérifier si l'indice est valide (dans les limites de la taille de la main du joueur)
                    if (index >= 0 && index < player.getHand().size()) {
                        indexes.add(index);
                    } else {
                        System.out.println("index invalide: " + indexStr);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("commande invalide: " + indexStr);
                }
            }
            // Si des indices valides ont été spécifiés, remettre les tuiles correspondantes dans le sac et recharger la main du joueur
            if (indexes.size() > 0) {
                player.refill();
            }
        }
    }


    /**
     * Affiche les informations d'aide pour les commandes du jeu Qwirkle.
     */
    public static void displayHelp() {
        System.out.println("Q W I R K L E");
        System.out.println("Commandes Qwirkle :");
        System.out.println("- jouer une tuile : o <ligne> <colonne> <index>");
        System.out.println("- jouer une ligne : l <ligne> <colonne> <index1> [<index2>] <direction>");
        System.out.println("- jouer un plic-ploc : m <ligne1> <colonne1> <index1> [<ligne2> <colonne2> <index2>]");
        System.out.println("- jouer en premier : f <index1> [<index2>] <direction>");
        System.out.println("- passer son tour : p");
        System.out.println("- quitter : q");
        System.out.println("index : indice dans la liste des tuiles");
        System.out.println("direction : l (gauche), r (droite), u (haut), d (bas)");
    }
    public static void displayMessage(String s){
        System.out.println(s);
    }
    public static void displayCurrentPlayerHand(List<Tile> t){
        System.out.println("Voici votre main "+t);
    }

    public static void displayCurrentPlayerName (String currentPlayerName){
        System.out.println("Joueur " + currentPlayerName + ", c'est à votre tour !");
    }

    /**
     * Affiche un message d'erreur.
     * paramètre message: le message d'erreur à afficher
     */
    public static void displayError(String message) {
        System.err.println(message);
    }

}