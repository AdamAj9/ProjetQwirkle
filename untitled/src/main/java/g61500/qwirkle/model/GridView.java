package g61500.qwirkle.model;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe GridView représente une vue de la grille du jeu.
 * Elle permet d'accéder à des informations sur la grille et de vérifier si une case spécifique est vide.
 */
public class GridView {
    private final Grid grid;

    /**
     * Constructeur de la classe GridView.
     * paramètre grid: la grille du jeu à visualiser
     */
    public GridView(Grid grid) {
        this.grid = grid;
    }

    /**
     * Vérifie si la case spécifiée par les coordonnées (row, col) est vide.
     * row: le numéro de ligne de la case
     * col: le numéro de colonne de la case
     * return true si la case est vide, false sinon
     */
    public boolean isEmpty(int row, int col) {
        return grid.isEmpty(row, col);
    }

    /**
     * Retourne la grille du jeu associée à cette vue
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * recupere les valurs minimal et maximales de X et Y afin de n'afficher que les tuiles jouer et non tout
     * le Grid
     * @return les valeurs minimales et maximales de X et Y dans le tableau 2D
     */
    public List<Integer> minMaxValues(){
        int x=0;  // La première position non vide en X
        boolean xFound=false;  // Indicateur pour savoir si la première position non vide en X a été trouvée
        int x1=0;  // La dernière position non vide en X
        List<Integer> values=new ArrayList<>();  // Liste pour stocker les valeurs min et max en X et Y

        // Parcours de la grille pour trouver les valeurs min et max en X
        for (int i = 0; i < 91; i++) {
            for (int j = 0; j <91 ; j++) {
                if(grid.getTile(i,j)!=null && xFound==false){
                    xFound=true;  // Indique que la première position non vide en X a été trouvée
                    x=i;
                }
                if(grid.getTile(i,j)!=null ){

                    x1=i;  // Met à jour la dernière position non vide en X
                }
            }
        }
        values.add(x);  // Ajoute la première position non vide en X à la liste
        values.add(x1);  // Ajoute la dernière position non vide en X à la liste

        int y=0;  // La première position non vide en Y
        boolean yFound=false;  // Indicateur pour savoir si la première position non vide en Y a été trouvée
        int y1=0;  // La dernière position non vide en Y
        for (int i = 0; i < 91; i++) {
            for (int j = 0; j <91 ; j++) {
                if(grid.getTile(j,i)!=null && yFound==false){
                    yFound=true;  // Indique que la première position non vide en Y a été trouvé
                    y=i;
                }
                if(grid.getTile(j,i)!=null ){

                    y1=i;  // Met à jour la dernière position non vide en Y
                }
            }
        }
        values.add(y);  // Ajoute la première position non vide en Y à la liste
        values.add(y1);  // Ajoute la dernière position non vide en Y à la liste

        return values;  // Retourne la liste des valeurs min et max en X et Y
    }
}

