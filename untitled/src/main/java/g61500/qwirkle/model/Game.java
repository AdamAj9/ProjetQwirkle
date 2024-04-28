package g61500.qwirkle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * La classe Game représente le jeu Qwirkle.
 * Elle gère la logique du jeu, la grille, les joueurs et les actions possibles.
 */

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    private Grid grid;
    private Player[] players;
    private int currentPlayerIndex;
    private boolean isOver;

    /**
     * Retourne un tableau contenant les joueurs du jeu.
     */
    public Player[] getPlayers() {
        return players;
    }




    /**
     * Constructeur de la classe Game.
     * Crée une nouvelle instance du jeu avec une grille vide, les joueurs spécifiés et initialise l'index du joueur courant.
     * le paramètre playerNames: une liste contenant les noms des joueurs
     */
    public Game(List<String> playerNames) {
        // Créer une nouvelle grille vide pour le jeu
        this.grid = new Grid();
        // Créer un tableau de joueurs en fonction du nombre de noms de joueurs spécifiés
        this.players = new Player[playerNames.size()];
        // Parcourir les noms de joueurs et créer un objet Player pour chaque nom
        for (int i = 0; i < playerNames.size(); i++) {
            players[i] = new Player(playerNames.get(i), grid.getTileBag());
        }
        // Initialiser l'index du joueur courant à 0 (premier joueur)
        this.currentPlayerIndex = 0;
        // Initialiser l'état du jeu à non terminé
        this.isOver = false;
    }


    /**

     */
    public void first(Direction d, int... is) throws QwirkleException {
        // Récupérer le joueur courant
        Player currentPlayer = players[currentPlayerIndex];
        // Créer un tableau de tuiles à partir des indices spécifiés
        Tile[] tiles = new Tile[is.length];
        for (int i = 0; i < is.length; i++) {
            int index = is[i];
            // Obtenir la tuile correspondant à l'indice depuis la main du joueur
            Tile tile = currentPlayer.getHand().get(index);
            tiles[i] = tile;
        }
        try {
            // Appeler la méthode firstAdd() de la grille pour placer les tuiles dans la direction spécifiée

            int score =grid.firstAdd(d, tiles);
            // Ajouter le score obtenu au joueur courant
            currentPlayer.addScore(score);
            // Retirer les tuiles jouées de la main du joueur
            currentPlayer.remove(tiles);
            // Recharger la main du joueur à partir du sac de tuiles
            currentPlayer.refill();

            // Passer au joueur suivant (en boucle)
            currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        } catch (QwirkleException q) {
            throw new QwirkleException(q.getMessage());
        }
    }


    /**

     */
    public void play(int row, int col, int index) throws QwirkleException {
        // Récupérer le joueur courant
        Player currentPlayer = players[currentPlayerIndex];
        // Obtenir la tuile à partir de l'index spécifié dans la main du joueur
        Tile tile = currentPlayer.getHand().get(index);
        // Appeler la méthode add() de la grille pour placer la tuile à la position spécifiée
        try {

            int score = grid.add(row, col, tile);
            currentPlayer.addScore(score);
            currentPlayer.remove(tile);
            currentPlayer.refill();
            if(currentPlayer.getHand().size()==0){
                currentPlayer.addScore(6);
                isOver=true;
            }
            if(grid.getTileBag().size()==0){
                isOver=true;
            }
            currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        }catch(QwirkleException q){
            throw new QwirkleException(q.getMessage());
        }
    }


    /**

     */
    public void play(int row, int col, Direction d, int... indexes) throws QwirkleException {
        Player currentPlayer = players[currentPlayerIndex];
        // Créer un tableau de tuiles à partir des indices spécifiés dans la main du joueur
        Tile[] tiles = new Tile[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            int index = indexes[i];
            Tile tile = currentPlayer.getHand().get(index);
            tiles[i] = tile;
        }
        try {

            int score =  grid.add(row, col, d, tiles);
            currentPlayer.addScore(score);
            currentPlayer.remove(tiles);
            currentPlayer.refill();
            if(currentPlayer.getHand().size()==0){
                currentPlayer.addScore(6);
                isOver=true;
            }
            if(grid.getTileBag().size()==0){
                isOver=true;
            }
            currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        }catch(QwirkleException q){
            throw new QwirkleException(q.getMessage());
        }
    }

    /**

     */
    public void play(int... is) throws QwirkleException {
        // Récupérer les coordonnées de départ (ligne et colonne)
       List<TileAtPosition> tilesAtPosition=new ArrayList<>();
        Player currentPlayer = players[currentPlayerIndex];
        for (int i = 0; i <is.length-2 ; i=i+3) {
            Tile tile = currentPlayer.getHand().get(is[i+2]);
            TileAtPosition t=new TileAtPosition(tile,is[i],is[i+1]);
            tilesAtPosition.add(t);
        }
        TileAtPosition[] tiles = new TileAtPosition[tilesAtPosition.size()];
        for (int i = 0; i <tiles.length ; i++) {
            tiles[i]=tilesAtPosition.get(i);
        }
        // Récupérer la direction à partir de l'index spécifié
        try {

            int score = grid.add(tiles);;
            currentPlayer.addScore(score);
            for (int i = 0; i <tiles.length ; i++) {
                currentPlayer.remove(tiles[i].getTile());
            }

            currentPlayer.refill();
            if(currentPlayer.getHand().size()==0){
                currentPlayer.addScore(6);
                isOver=true;
            }
            if(grid.getTileBag().size()==0){
                isOver=true;
            }
            currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        }catch(QwirkleException q){
            throw new QwirkleException(q.getMessage());
        }
    }
    public static Direction getDirFromString(String s){
        s=s.toLowerCase();
        switch(s){
            case "l":
                return Direction.LEFT;
            case "d":
                return Direction.DOWN;
            case "u":
                return Direction.UP;
            case "r":
                return Direction.RIGHT;


        }
        return null;
    }
    public boolean isGameOver(){
        return isOver;
    }
    /**
     * Retourne le nom du joueur courant.
     */
    public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    /**
     * Retourne la liste des tuiles dans la main du joueur courant.
     */
    public List<Tile> getCurrentPlayerHand() {
        return players[currentPlayerIndex].getHand();
    }

    /**
     * Retourne la grille du jeu.
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Passe le tour du joueur courant au joueur suivant.
     */
    public void pass() throws QwirkleException {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        /*if (currentPlayerIndex == 0) {
            throw new QwirkleException("Tous les joueurs ont passé leur tour, fin de la partie");
        }*/
    }

    /**
     * Retourne le score du joueur courant.
     */
    public int getCurrentPlayerScore() {
        return players[currentPlayerIndex].getScore();
    }

    /**
     * Écrit la partie dans un fichier.
     * Le nom du fichier est passé en paramètre.
     * Lève une exception QwirkleException en cas de problème.
     */
    public void write(String fileName) throws QwirkleException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
        } catch (IOException e) {
            throw new QwirkleException("Erreur lors de l'écriture de la partie dans le fichier");
        }
    }

    /**
     * Récupère une instance de Game à partir d'un fichier.
     * Le nom du fichier est passé en paramètre.
     * Lève une exception QwirkleException en cas de problème.
     */
    public static Game getFromFile(String fileName) throws QwirkleException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Game) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new QwirkleException("Erreur lors de la lecture de la partie à partir du fichier");
        }
    }


}

