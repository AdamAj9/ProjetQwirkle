package g61500.qwirkle.model;

/**
 * Représente une tuile placée à une position spécifique sur la grille.
 * Contient la tuile elle-même ainsi que les coordonnées de la position.
 */
public record TileAtPosition(Tile tile, int row, int col) {

    /**
     * Retourne la tuile placée à la position spécifique.
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Retourne le numéro de ligne de la position.
     */
    public int getRow() {
        return row;
    }

    /**
     * Retourne le numéro de colonne de la position.
     */
    public int getCol() {
        return col;
    }

}
