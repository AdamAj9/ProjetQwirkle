package g61500.qwirkle.model;

import java.util.Collections;
import java.util.List;
import java.io.Serializable;

/**
 * La classe Player représente un joueur dans le jeu.
 * Chaque joueur a un nom, une main de tuiles, et un score.
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private List<Tile> tiles;
    private int score;

    private Bag bag;

    /**
     * Constructeur de la classe Player.
     * paramètre name: le nom du joueur
     */
    public Player(String name, Bag b) {
        this.name = name;
        this.bag=b;
        this.tiles = bag.getRandomTiles(6);
        this.score = 0;
    }

    /**
     * Retourne le nom du joueur.
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne la main du joueur (liste de tuiles).
     * La liste retournée est immuable (non modifiable).
     */
    public List<Tile> getHand() {
        return Collections.unmodifiableList(tiles);
    }

    /**
     * Recharge la main du joueur avec de nouvelles tuiles aléatoires.
     */
    public void refill() {
        int tilesToAdd = 6 - tiles.size();
        List<Tile> newTiles = bag.getRandomTiles(tilesToAdd);
        if(newTiles!=null) {
            tiles.addAll(newTiles);
        }
    }

    /**
     * Retire les tuiles spécifiées de la main du joueur.
     * paramètre ts: les tuiles à retirer de la main du joueur
     */
    public void remove(Tile... ts) {
        for (Tile t : ts) {
            tiles.remove(t);
        }
    }

    /**
     * Retourne le score du joueur.
     */
    public int getScore() {
        return score;
    }

    /**
     * Ajoute des points au score du joueur.
     * paramètre points: le nombre de points à ajouter au score
     */
    public void addScore(int points) {
        score += points;
    }
}
