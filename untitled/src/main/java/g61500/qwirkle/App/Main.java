package g61500.qwirkle.App;

import g61500.qwirkle.model.*;
import g61500.qwirkle.view.View;

import java.util.*;



/**
 * Classe principale du jeu Qwirkle.
 */
public class Main {

    private static Player player;

    /**
     * Méthode principale du programme.
     * paramètre args: les arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        // Création d'un objet Scanner pour lire l'entrée utilisateur depuis la console
        Scanner scanner = new Scanner(System.in);

        // Création du jeu avec les noms des joueurs saisis par l'utilisateur
        List<String> playerNames = new ArrayList<>();
        View.displayMessage("Entrez les noms des joueurs (séparés par une virgule) :");
        String[] inputNames = scanner.nextLine().split(",");
        for (String name : inputNames) {
            playerNames.add(name.trim());
        }
        Game game = new Game(playerNames);
        View.displayHelp();
        while (!game.isGameOver()) {
            GridView gridView = new GridView(game.getGrid());
            View.display(gridView);

            // Affichage du joueur courant et de sa main
            String currentPlayerName = game.getCurrentPlayerName();
            View.displayCurrentPlayerName(currentPlayerName);
            List<Tile> currentPlayerHand = game.getCurrentPlayerHand();
            View.displayCurrentPlayerHand(currentPlayerHand);

            List<Player> players = Arrays.asList(game.getPlayers());
            players.sort(Comparator.comparing(Player::getScore).reversed());
            for (Player player : players) {
                View.displayMessage(player.getName() + " : " + player.getScore());
            }

            // Attente de l'entrée de l'utilisateur


            View.displayMessage("Entrez votre coup :");
            String input = scanner.nextLine();
            String[] inputArgs = input.split(" ");

            try {
                if (inputArgs[0].equals("p")) {
                    game.pass();
                } else if (inputArgs[0].equals("q")) {
                    System.exit(0);
                } else if (inputArgs[0].equals("f")) {
                    List<Integer> t = new ArrayList<>();
                    for (int i = 1; i < inputArgs.length - 1; i++) {
                        t.add(Integer.parseInt(inputArgs[i]));
                    }
                    int[] tab = new int[t.size()];
                    for (int i = 0; i < t.size(); i++) {
                        tab[i] = t.get(i);
                    }
                    try {
                        game.first(game.getDirFromString(inputArgs[inputArgs.length - 1]), tab);
                    } catch (QwirkleException q) {
                        View.displayError(q.getMessage());
                    }
                } else if (inputArgs[0].equals("l")) {
                    List<Integer> t = new ArrayList<>();
                    for (int i = 1; i < inputArgs.length - 1; i++) {
                        t.add(Integer.parseInt(inputArgs[i]));
                    }
                    int[] tab = new int[t.size() - 2];
                    for (int i = 0; i < t.size() - 2; i++) {
                        tab[i] = t.get(i + 2);
                    }
                    try {

                        game.play(t.get(0), t.get(1), game.getDirFromString(inputArgs[inputArgs.length - 1]), tab);
                    } catch (QwirkleException q) {
                        View.displayError(q.getMessage());
                    }
                } else if (inputArgs[0].equals("o")) {
                    List<Integer> t = new ArrayList<>();
                    for (int i = 1; i < inputArgs.length; i++) {
                        t.add(Integer.parseInt(inputArgs[i]));
                    }

                    try {

                        game.play(t.get(0), t.get(1), Integer.parseInt(inputArgs[3]));
                    } catch (QwirkleException q) {
                        View.displayError(q.getMessage());
                    }
                } else if (inputArgs[0].equals("h")) {
                    View.displayHelp();

                } else if (inputArgs[0].equals("m")) {
                    List<Integer> t = new ArrayList<>();
                    for (int i = 1; i < inputArgs.length; i++) {
                        t.add(Integer.parseInt(inputArgs[i]));
                    }
                    int[] tab = new int[t.size()];
                    for (int i = 0; i < t.size(); i++) {
                        tab[i] = t.get(i);
                    }

                    try {
                        game.play(tab);
                    } catch (QwirkleException q) {
                        View.displayError(q.getMessage());
                    }
                }else{
                    throw new IllegalArgumentException("Commande inconnue");
                }
            } catch (Exception e) {
                View.displayError(e.getMessage());
                View.displayError("Votre commande ou index pour tuile est incorrect ");
                View.displayError("Voici un leger rappel des commandes");
                View.displayHelp();
            }
        }
    }
}