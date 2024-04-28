package g61500.qwirkle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * La classe Bag représente un sac de tuiles.
 */
public class Bag {

    private static Bag instance;
    private List<Tile> tiles;

    /**
     * Constructeur privé de la classe Bag.
     * Crée un sac de tuiles contenant 108 tuiles de différentes couleurs et formes.
     * Les tuiles sont mélangées aléatoirement.
     */
    private Bag() {
        // Crée une nouvelle instance de ArrayList pour stocker les tuiles
        tiles = new ArrayList<>();
        // Effectue trois itérations pour ajouter les tuiles au sac
        for (int i = 0; i < 3; i++) {
            // Parcourt toutes les couleurs possibles
            for (Color color : Color.values()) {
                // Parcourt toutes les formes possibles
                for (Shape shape : Shape.values()) {
                    // Crée une nouvelle tuile avec la couleur et la forme actuelles et l'ajoute au sac
                    tiles.add(new Tile(color, shape));
                }
            }
        }

        // Mélange aléatoirement les tuiles dans le sac
        Collections.shuffle(tiles);
    }

    /**
     * Retourne une liste de tuiles aléatoires à partir du sac.
     * le paramètre count le nombre de tuiles à extraire du sac
     * le return une liste de tuiles aléatoires de taille count, ou null si le sac ne contient pas suffisamment de tuiles
     */
    public List<Tile> getRandomTiles(int count) {
        // Cette condition vérifie si le sac contient suffisamment de tuiles pour extraire 'count' tuiles.
        // Si le nombre de tuiles présentes dans le sac est inférieur à 'count', la méthode retourne null, indiquant qu'il n'y a pas suffisamment de tuiles disponibles.
        if (tiles.size() < count) {
            return null;
        }
        // crée une nouvelle liste selectedTiles de tuiles sélectionnées à partir du sac. Cette liste sera utilisée pour stocker les tuiles extraites du sac.
        List<Tile> selectedTiles = new ArrayList<>();
        // Cette boucle itère count fois et extrait la première tuile du sac en utilisant tiles.remove(0),
        // puis l'ajoute à la liste selectedTiles en utilisant selectedTiles.add().
        // Ainsi, count tuiles sont extraites du sac et ajoutées à la liste selectedTiles.
        for (int i = 0; i < count; i++) {
            selectedTiles.add(tiles.remove(0));
        }
        return selectedTiles;
    }

    /**
     * Retourne l'instance unique du sac.
     * Si aucune instance n'existe, en crée une et la retourne.
     */
    public static Bag getInstance() {
        if (instance == null) {
            instance = new Bag();
        }
        return instance;
    }

    /**
     * Retourne le nombre de tuiles restantes dans le sac.
     */
    public int size() {
        return tiles.size();
    }
}
