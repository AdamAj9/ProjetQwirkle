package g61500.qwirkle.model;

/**
 * L'énumération Direction représente les différentes directions possibles.
 * Chaque direction est associée à un déplacement sur les axes des lignes (rows) et des colonnes (cols).
 */
public enum Direction {

    LEFT(0, -1),
    RIGHT(0, 1),
    UP(-1, 0),
    DOWN(1, 0),
    VERTICAL(1, 0),
    HORIZONTAL(0, 1);

    private final int deltaRow;
    private final int deltaCol;

    /**
     * Constructeur de la direction avec les déplacements associés sur les axes des lignes et des colonnes.
     * le paramètre deltaRow le déplacement sur l'axe des lignes
     * le paramètre deltaCol le déplacement sur l'axe des colonnes
     */
    Direction(int deltaRow, int deltaCol) {
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }

    /**
     * Retourne le déplacement sur l'axe des lignes associé à la direction.
     */
    public int getDeltaRow() {
        return deltaRow;
    }

    /**
     * Retourne le déplacement sur l'axe des colonnes associé à la direction.
     */
    public int getDeltaCol() {
        return deltaCol;
    }

    /**
     * Retourne la direction opposée à la direction actuelle.
     */
    public Direction opposite() {
        switch (this) {
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            default:
                throw new AssertionError("Direction inconnue: " + this);
        }
    }

    /**
     * Retourne la direction correspondant à l'index donné.
     * le paramètre index : l'index de la direction
     * return la direction correspondant à l'index
     * throws QwirkleException : si l'index donné est invalide pour une direction
     */
    public static Direction fromIndex(int index) throws QwirkleException {
        switch (index) {
            case 0:
                return LEFT;
            case 1:
                return RIGHT;
            case 2:
                return UP;
            case 3:
                return DOWN;
            case 4:
                return VERTICAL;
            case 5:
                return HORIZONTAL;
            default:
                throw new QwirkleException("Index invalide pour la direction : " + index);
        }
    }
}


