package g61500.qwirkle.model;

/**
 * Représente une tuile du jeu Qwirkle.
 * Une tuile est définie par sa couleur et sa forme.
 * Cette classe est immuable
 */
public record Tile(Color color, Shape shape) {

    /**
     * Retourne la couleur de la tuile.
     */
    public Color getColor() { return color;
    }

    /**
     * Retourne la forme de la tuile.
     */
    public Shape getShape() {
        return shape;
    }

}
