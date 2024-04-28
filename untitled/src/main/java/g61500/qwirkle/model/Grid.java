package g61500.qwirkle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Classe représentant la grille du jeu Qwirkle.
 */
public class Grid implements Serializable {

    private static final long serialVersionUID = 1L;
    private Tile[][] tiles;
    private boolean isEmpty;
    private Bag tileBag;
    private GridView gridView;

    /**
     * Constructeur de la classe Grid.
     * Initialise une grille vide avec une taille de 91x91 et obtient l'instance du sac de tuiles.
     */
    public Grid() {
        tiles = new Tile[91][91];
        isEmpty = true;
        tileBag = Bag.getInstance();
        gridView = new GridView(this);
    }

    /**
     * Retourne la tuile à la position spécifiée dans la grille.
     * paramètre: x la coordonnée x de la tuile
     * paramètre: y la coordonnée y de la tuile
     */
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length) {
            return null;
        }
        return tiles[x][y];
    }


    /**
     * Retourne la taille de la grille.
     */
    public int getSize() {
        return tiles.length;
    }

    /**
     * Retourne le sac de tuiles associé à la grille.
     */
    public Bag getTileBag() {
        return tileBag;
    }


    /**
     * Ajoute une séquence de tuiles à la grille dans une direction spécifiée.
     * Cette méthode ne peut être utilisée qu'une seule fois lorsque le plateau est vide.
     * paramètre d: la direction dans laquelle ajouter les tuiles
     * paramètre line: la séquence de tuiles à ajouter
     * return 0 si les tuiles ont été ajoutées avec succès, -1 sinon
     */
    public int firstAdd(Direction d, Tile... line) {
        if ( !isEmpty ) {
            throw new QwirkleException("appel une seul fois cet methode");
        }
        // Initialiser les coordonnées x et y avec des valeurs de départ
        int x=45;
        int y=45;
        // Obtenir les déplacements en x et y en fonction de la direction
        int dx=d.getDeltaRow();
        int dy=d.getDeltaCol();
        // Vérifier si les tuiles peuvent être sélectionnées pour le premier ajout
        if(!canTilesBeSelected(line)){
            throw new QwirkleException("tu peux pas mettre ces tuiles");
        }
        // Placer les tuiles dans la grille en suivant la direction spécifiée
        for(Tile t:line){
            tiles[x][y]=t;

            x=x+dx;
            y=y+dy;
        }
        // Marquer la grille comme non vide
        isEmpty=false;
        return line.length;
    }

    /**
     * Ajoute une tuile à la position spécifiée dans la grille.
     * Vérifie si la tuile peut être placée en respectant les règles du jeu.
     * paramètre row: la ligne de la position dans la grille
     * paramètre col: la colonne de la position dans la grille
     * paramètre tile: la tuile à ajouter
     * return 0 si la tuile a été ajoutée avec succès
     * throws QwirkleException si une tuile a déjà été placée à cet emplacement ou si la tuile ne peut pas être placée à côté d'une autre tuile qui n'a ni la même couleur ni la même forme
     */

    public int add(int row, int col, Tile tile) {
        if(isEmpty){
            throw new QwirkleException("la grid est vide");
        }
        // Vérifier si la position est déjà occupée par une tuile
        if(tiles[row][col]!=null){
            throw new QwirkleException("deja une tuile");
        }
        if(noTilesAround(row,col)){
            throw new QwirkleException("pas de tuiles autour");
        }
        // Obtenir les tuiles à gauche, à droite, en haut et en bas de la position
        List<Tile> tileLeft=getTilesAtDirection(Direction.LEFT,row,col);
        List<Tile> tileRight=getTilesAtDirection(Direction.RIGHT,row,col);
        List<Tile> tileUp=getTilesAtDirection(Direction.UP,row,col);
        List<Tile> tileDown=getTilesAtDirection(Direction.DOWN,row,col);
        // Vérifier si la tuile peut être sélectionnée en fonction des tuiles adjacentes
        if(canTilesBeSelected(tileLeft,tileRight,tile)&&canTilesBeSelected(tileDown,tileUp,tile)){
            tiles[row][col]=tile;

        }else{
            throw new QwirkleException("tu ne peux pas poser ici");
        }
        int score=0;
        if((tileLeft.size()+tileRight.size()+1)==6){
            score=score+6;
        }
        if((tileUp.size()+tileDown.size()+1)==6){
            score=score+6;
        }
        score=score+tileLeft.size()+tileRight.size()+tileDown.size()+tileUp.size();
        return score;

    }

    /**
     * Ajoute une ligne de tuiles à partir de la position spécifiée dans la grille, dans la direction spécifiée.
     * Vérifie si les tuiles peuvent être placées en respectant les règles du jeu.
     * paramètre row: la ligne de départ de la position dans la grille
     * paramètre col: la colonne de départ de la position dans la grille
     * paramètre d: la direction dans laquelle ajouter les tuiles (HORIZONTAL ou VERTICAL)
     * paramètre line: les tuiles à ajouter
     * return 0 si les tuiles ont été ajoutées avec succès
     */

    public int add(int row, int col, Direction d, Tile... line) {
        if(isEmpty){
            throw new QwirkleException("la grid est vide");
        }
        // Vérifier si tous les espaces vers la direction spécifiée sont libres
        if(!allSpaceTowardDirectionIsFree(row,col, line.length,d)){
            throw new QwirkleException("deja une tuile occupé");
        }
        int dxT = d.getDeltaRow();
        int dyT= d.getDeltaCol();
        boolean noTilesAroundAll=false;
        int rowTest=row;
        int colTest=col;
        for (int i = 0; i < line.length ; i++) {
            if(!noTilesAround(rowTest,colTest)){
                noTilesAroundAll=true;
            }
            rowTest=rowTest+dxT;
            colTest=colTest+dyT;
        }
        if(noTilesAroundAll){
            throw new QwirkleException("pas de tuiles autour");
        }

        if(line.length==1){
            if(noTilesAround(row,col)) {
                throw new QwirkleException("pas de tuiles autour");
            }
            return add(row,col,line[0]);
        }else {
            // Traiter les différents cas en fonction de la direction
            if (d == Direction.RIGHT) {
                int score=0;
                // Obtenir les tuiles à gauche et à droite de la position
                List<Tile> tileLeft = getTilesAtDirection(Direction.LEFT, row, col);
                List<Tile> tileRight = getTilesAtDirection(Direction.RIGHT, row, col + (line.length - 1));
                // Créer une liste pour stocker les tuiles à ajouter
                List<Tile> varargasList = new ArrayList<>(Arrays.asList(line));

                // Ajouter les tuiles à la liste des tuiles à droite (ou à gauche)
                for (int i = 0; i < varargasList.size() - 1; i++) {
                    tileRight.add(0, varargasList.get(varargasList.size() - 1 - i));
                }
                // Vérifier si les tuiles respectent les règles de sélection avec les tuiles adjacentes
                for (int i = 0; i < line.length; i++) {
                    List<Tile> tileUp = getTilesAtDirection(Direction.UP, row, col + i);
                    List<Tile> tileDown = getTilesAtDirection(Direction.DOWN, row, col + i);
                    if (!canTilesBeSelected(tileUp, tileDown, line[i])) {
                        throw new QwirkleException("La suite donné ne respecte pas les regles");
                    }
                    if((tileUp.size()+tileDown.size()+1)==6){
                        score=score+6;
                    }

                    score=score+tileUp.size()+tileDown.size()+1;
                }
                // Vérifier si les tuiles respectent les règles de sélection avec les tuiles à gauche (ou à droite)
                if (!canTilesBeSelected(tileLeft, tileRight, line[0])) {
                    throw new QwirkleException("la suite donné ne respecte pas les regles");
                } else {
                    int dx = d.getDeltaRow();
                    int dy = d.getDeltaCol();
                    for (Tile t : line) {
                        tiles[row][col] = t;
                        row = row + dx;
                        col = col + dy;
                    }

                }
                if((tileLeft.size()+tileRight.size()+1)==6){
                    score=score+6;
                }

                score=score+tileLeft.size()+tileRight.size()+1;
                return score;
            } else if (d == Direction.LEFT) {
                int score=0;
                List<Tile> tileRight = getTilesAtDirection(Direction.RIGHT, row, col);
                List<Tile> tileLeft = getTilesAtDirection(Direction.LEFT, row, col + (line.length - 1));

                List<Tile> varargasList = new ArrayList<>(Arrays.asList(line));


                for (int i = 0; i < varargasList.size() - 1; i++) {
                    tileRight.add(0, varargasList.get(varargasList.size() - 1 - i));
                }

                for (int i = 0; i < line.length; i++) {
                    List<Tile> tileUp = getTilesAtDirection(Direction.UP, row, col + i);
                    List<Tile> tileDown = getTilesAtDirection(Direction.DOWN, row, col + i);
                    if (!canTilesBeSelected(tileUp, tileDown, line[i])) {
                        throw new QwirkleException("La suite donné ne respecte pas les regles");
                    }
                    if((tileUp.size()+tileDown.size()+1)==6){
                        score=score+6;
                    }

                    score=score+tileUp.size()+tileDown.size()+1;
                }
                if (!canTilesBeSelected(tileLeft, tileRight, line[0])) {
                    throw new QwirkleException("la suite donné ne respecte pas les regles");
                } else {
                    int dx = d.getDeltaRow();
                    int dy = d.getDeltaCol();
                    for (Tile t : line) {
                        tiles[row][col] = t;
                        row = row + dx;
                        col = col + dy;
                    }

                }
                if((tileLeft.size()+tileRight.size()+1)==6){
                    score=score+6;
                }

                score=score+tileLeft.size()+tileRight.size()+1;
                return score;
            } else if (d == Direction.UP) {
                int score=0;
                List<Tile> tileDown = getTilesAtDirection(Direction.DOWN, row, col);
                List<Tile> tileUp = getTilesAtDirection(Direction.UP, row, col + (line.length - 1));

                List<Tile> varargasList = new ArrayList<>(Arrays.asList(line));


                for (int i = 0; i < varargasList.size() - 1; i++) {
                    tileUp.add(0, varargasList.get(varargasList.size() - 1 - i));
                }

                for (int i = 0; i < line.length; i++) {
                    List<Tile> tileLeft = getTilesAtDirection(Direction.LEFT, row, col + i);
                    List<Tile> tileRight = getTilesAtDirection(Direction.RIGHT, row, col + i);
                    if (!canTilesBeSelected(tileUp, tileDown, line[i])) {
                        throw new QwirkleException("La suite donné ne respecte pas les regles");
                    }
                    if((tileLeft.size()+tileRight.size()+1)==6){
                        score=score+6;
                    }

                    score=score+tileLeft.size()+tileRight.size()+1;
                }
                if (!canTilesBeSelected(tileDown, tileUp, line[0])) {
                    throw new QwirkleException("la suite donné ne respecte pas les regles");
                } else {
                    int dx = d.getDeltaRow();
                    int dy = d.getDeltaCol();
                    for (Tile t : line) {
                        tiles[row][col] = t;
                        row = row + dx;
                        col = col + dy;
                    }

                }
                if((tileUp.size()+tileDown.size()+1)==6){
                    score=score+6;
                }

                score=score+tileUp.size()+tileDown.size()+1;
                return score;
            } else {
                int score=0;
                List<Tile> tileUp = getTilesAtDirection(Direction.UP, row, col);
                List<Tile> tileDown = getTilesAtDirection(Direction.DOWN, row, col + (line.length - 1));

                List<Tile> varargasList = new ArrayList<>(Arrays.asList(line));


                for (int i = 0; i < varargasList.size() - 1; i++) {
                    tileUp.add(0, varargasList.get(varargasList.size() - 1 - i));
                }

                for (int i = 0; i < line.length; i++) {
                    List<Tile> tileLeft = getTilesAtDirection(Direction.LEFT, row, col + i);
                    List<Tile> tileRight = getTilesAtDirection(Direction.RIGHT, row, col + i);
                    if (!canTilesBeSelected(tileUp, tileDown, line[i])) {
                        throw new QwirkleException("La suite donné ne respecte pas les regles");
                    }
                    if((tileLeft.size()+tileRight.size()+1)==6){
                        score=score+6;
                    }

                    score=score+tileLeft.size()+tileRight.size()+1;
                }
                if (!canTilesBeSelected(tileDown, tileUp, line[0])) {
                    throw new QwirkleException("la suite donné ne respecte pas les regles");
                } else {
                    int dx = d.getDeltaRow();
                    int dy = d.getDeltaCol();
                    for (Tile t : line) {
                        tiles[row][col] = t;
                        row = row + dx;
                        col = col + dy;
                    }

                }
                if((tileUp.size()+tileDown.size()+1)==6){
                    score=score+6;
                }

                score=score+tileUp.size()+tileDown.size()+1;
                return score;
            }
        }


    }

    /**
     * Ajoute des tuiles selon les positions spécifiées dans la grille.
     * Vérifie si les tuiles peuvent être placées en respectant les règles du jeu.
     * paramètre line: les tuiles à ajouter avec leurs positions
     */

    public int add(TileAtPosition... line) {
        if(isEmpty){
            throw new QwirkleException("la grid est vide");
        }
        if(noTilesAround(line[0].getRow(),line[0].getCol())){
            throw new QwirkleException("pas de tuiles autour");
        }
        // Si une seule tuile est fournie, l'ajouter à la position spécifiée
        if(line.length==1){
           return add(line[0].getRow(),line[0].getCol(),line[0].getTile());
        }else{
            // Calculer les écarts en x et y entre les positions des tuiles
            int dx=line[0].getRow()-line[1].getRow();
            int dy=line[0].getCol()-line[1].getCol();
            // Ajouter les tuiles en fonction de la direction déterminée par les écarts en x et y
            if(dx==1){
                // Créer une liste pour stocker les tuiles
                List<Tile> t=new ArrayList<>() ;
                for (TileAtPosition tile:line ) {
                    t.add(tile.getTile());
                }
                // Convertir la liste de tuiles en tableau
                Tile[] tTab=new Tile[t.size()];
                t.toArray(tTab);
                return add(line[0].getRow(),line[0].getCol(),Direction.DOWN,tTab);
            }else if(dx==-1){
                List<Tile> t=new ArrayList<>() ;
                for (TileAtPosition tile:line ) {
                    t.add(tile.getTile());
                }
                Tile[] tTab=new Tile[t.size()];
                t.toArray(tTab);
                return add(line[0].getRow(),line[0].getCol(),Direction.UP,tTab);
            }else if(dy==1){
                List<Tile> t=new ArrayList<>() ;
                for (TileAtPosition tile:line ) {
                    t.add(tile.getTile());
                }
                Tile[] tTab=new Tile[t.size()];
                t.toArray(tTab);
                return add(line[0].getRow(),line[0].getCol(),Direction.RIGHT,tTab);
            }else{
                List<Tile> t=new ArrayList<>() ;
                for (TileAtPosition tile:line ) {
                    t.add(tile.getTile());
                }
                Tile[] tTab=new Tile[t.size()];
                t.toArray(tTab);
                return add(line[0].getRow(),line[0].getCol(),Direction.DOWN,tTab);
            }
        }
    }

    /**
     * Vérifie si l'emplacement spécifié dans la grille est vide.
     * paramètre row: la ligne de l'emplacement dans la grille
     * paramètre col: la colonne de l'emplacement dans la grille
     * return true si l'emplacement est vide, false sinon
     */
    public boolean isEmpty(int row, int col) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Cette méthode, nommée uniqueShapes, est utilisée pour vérifier si une liste de formes (représentées par des objets de type Tile) contient des formes en double.
     */

    private boolean uniqueShapes(List<Tile> shapeList) {
        // Parcourt la liste de formes avec un indice i
        for (int i = 0; i < shapeList.size(); i++) {
            // Parcourt à nouveau la liste de formes avec un indice j
            for (int j = 0; j < shapeList.size(); j++) {
                // Vérifie si l'indice j est différent de l'indice i pour éviter de comparer la même forme
                if (j != i) {
                    // Compare les formes aux indices i et j pour voir si elles sont identiques
                    if(shapeList.get(i)==shapeList.get(j)){
                        // Renvoie false, car des formes en double ont été trouvées
                        return false;
                    }
                }
            }
        }
        // Si aucune forme en double n'a été trouvée, renvoie true
        return true;
    }

    /**
     * Cette méthode, nommée uniqueColors, est utilisée pour vérifier si une liste de couleurs (représentées par des objets de type Tile) contient des couleurs en double.
     */
    private boolean uniqueColors(List<Tile> colorList) {
        // Parcourt la liste de couleurs avec un indice i
        for (int i = 0; i < colorList.size(); i++) {
            // Parcourt à nouveau la liste de couleurs avec un indice j
            for (int j = 0; j < colorList.size(); j++) {
                // Vérifie si l'indice j est différent de l'indice i pour éviter de comparer la même couleur
                if (j != i) {
                    // Compare les couleurs aux indices i et j pour voir si elles sont identiques
                    if (colorList.get(i) == colorList.get(j)) {
                        // Renvoie false, car des couleurs identiques ont été trouvées
                        return false;
                    }
                }
            }
        }
        // Si aucune couleur identique n'a été trouvée, renvoie true
        return true;
    }

    /**
     * Cette méthode prend une direction, une position de départ (row, col) et retourne une liste de tuiles rencontrées dans cette direction à partir de la position donnée.
     */
    private List<Tile> getTilesAtDirection(Direction dir, int row, int col) {
        // Crée une nouvelle liste pour stocker les tuiles dans la direction spécifiée
        ArrayList<Tile> tilesAtDir = new ArrayList<>();
        // Indique s'il reste des tuiles dans la direction donnée
        boolean tilesLeft = true;
        // Obtient les changements de ligne (dx) et de colonne (dy) basés sur la direction
        int dx = dir.getDeltaRow();
        int dy = dir.getDeltaCol();
        // Boucle tant qu'il reste des tuiles dans la direction spécifiée
        while (tilesLeft) {
            // Déplace la position actuelle de row et col en fonction des changements de direction
            row = row + dx;
            col = col + dy;

            // Récupère la tuile à la position mise à jour
            Tile t = tiles[row][col];

            // Vérifie si la tuile n'est pas nulle et si la position est dans les limites de la matrice
            if (t != null && row <= 91 && row >= 0 && col <= 91 && col >= 0) {
                // Ajoute la tuile à la liste des tuiles dans la direction
                tilesAtDir.add(t);
            } else {
                // S'il n'y a pas de tuile ou si la position est hors limites, arrête la boucle
                tilesLeft = false;
            }
        }
        // Renvoie la liste de tuiles dans la direction spécifiée
        return tilesAtDir;
    }

    /**
     * Cette méthode est conçue pour vérifier si les tuiles sélectionnées dans deux groupes, ainsi qu'une tuile additionnelle, satisfont certaines conditions.
     * Le code effectue une série de vérifications basées sur les couleurs et les formes des tuiles pour déterminer si la sélection est valide selon les critères spécifiés.
     */

    private boolean canTilesBeSelected(List<Tile> tilesToSelect, List<Tile> tilesToSelect2, Tile tile) {
        // Crée deux listes distinctes pour stocker les couleurs et les formes des tuiles sélectionnées
        ArrayList tileListColor = new ArrayList();
        ArrayList tileListShape = new ArrayList();

        // Indicateurs de cohérence des formes et des couleurs
        boolean sameShape = true;
        boolean sameColor = true;

        // Parcourt les tuiles à sélectionner et ajoute leurs couleurs et formes aux listes respectives
        for (int i = 0; i < tilesToSelect.size(); i++) {
            tileListColor.add(tilesToSelect.get(i).getColor());
            tileListShape.add(tilesToSelect.get(i).getShape());
        }
        // Ajoute la couleur et la forme de la tuile actuelle à la liste
        tileListColor.add(tile.getColor());
        tileListShape.add(tile.getShape());

        // Parcourt les tuiles à sélectionner (deuxième groupe) et ajoute leurs couleurs et formes aux listes
        for (int i = 0; i < tilesToSelect2.size(); i++) {
            tileListColor.add(tilesToSelect2.get(i).getColor());
            tileListShape.add(tilesToSelect2.get(i).getShape());
        }

        // Vérifie si le nombre de formes est de 1 (condition spécifique)
        if (tileListShape.size() == 1) {
            return true;
        }
        // Vérifie si le nombre de formes est supérieur à 6 (condition spécifique)
        if (tileListShape.size() > 6) {
            return false;
        }

        // Récupère la première forme et couleur pour les comparaisons ultérieures
        Shape firstShape = (Shape) tileListShape.get(0);
        Color firstColor = (Color) tileListColor.get(0);

        // Parcourt la liste des couleurs et vérifie si elles sont toutes les mêmes que la première couleur
        for (int i = 1; i < tileListColor.size(); i++) {
            if (tileListColor.get(i) != firstColor) {
                sameColor = false;
            }
        }

        // Parcourt la liste des formes et vérifie si elles sont toutes les mêmes que la première forme
        for (int i = 1; i < tileListShape.size(); i++) {
            if (tileListShape.get(i) != firstShape) {
                sameShape = false;
            }
        }

        // Vérifie les combinaisons de cohérence des formes et des couleurs et renvoie le résultat
        if (sameShape && sameColor) {

            return false;
        } else if (!sameShape && !sameColor) {

            return false;
        } else if (sameColor && !uniqueShapes(tileListShape)) {

            return false;
        } else if (sameShape && !uniqueColors(tileListColor)) {

            return false;
        }
        return true;
    }

    /**
     * Cette méthode vérifie si un groupe de tuiles peut être sélectionné en fonction de leur couleur et de leur forme.
     * Si toutes les tuiles ont la même couleur et la même forme, elles ne peuvent pas être sélectionnées.
     * Si toutes les tuiles ont soit la même forme soit la même couleur, elles peuvent être sélectionnées.
     */
    private boolean canTilesBeSelected(Tile ...tiles){
        // S'il n'y a qu'une seule tuile, elle peut toujours être sélectionnée
        if(tiles.length==1){
            return true;
        }
        // Créer des listes pour stocker les couleurs et les formes des tuiles
        ArrayList tileListColor=new ArrayList<Tile>();
        ArrayList tileListShape=new ArrayList<Tile>();
        // Indicateurs pour suivre si toutes les tuiles ont la même forme et la même couleur
        boolean sameShape=true;
        boolean sameColor=true;
        // Remplir les listes avec les couleurs et les formes des tuiles données
        for (Tile tile:tiles){
            tileListColor.add(tile.getColor());
            tileListShape.add(tile.getShape());
        }
        // Obtenir la couleur et la forme de la première tuile
        Shape firstShape= (Shape) tileListShape.get(0);
        Color firstColor= (Color) tileListColor.get(0);
        // Vérifier si toutes les tuiles ont la même couleur
        for (int i=1;i<tileListColor.size();i++){
            if (tileListColor.get(i) != firstColor) {
                sameColor = false;

            }
        }
        // Vérifier si toutes les tuiles ont la même forme
        for (int i=1;i<tileListShape.size();i++){
            if (tileListShape.get(i) != firstShape) {
                sameShape = false;

            }
        }
        // Si à la fois sameShape et sameColor sont vrais, les tuiles ne peuvent pas être sélectionnées
        if(sameShape && sameColor){
            return false;
        }
        // Si soit sameShape soit sameColor est vrai, les tuiles peuvent être sélectionnées
        if(!sameShape && !sameColor){
            return false;
        }
        if(sameColor && !uniqueShapes(tileListShape)){

            return false;

        }else if(sameShape && !uniqueColors(tileListColor)){

            return false;
        }
        return true;
    }

    /**
     * cette méthode vérifie si tous les espaces dans une certaine direction à partir d'une position de départ sont vides (ne contiennent pas de tuiles).
     */
    private boolean allSpaceTowardDirectionIsFree(int startRow, int startCol, int nbTilesToPut, Direction d) {
        // Obtient la position de départ
        int x = startRow;
        int y = startCol;

        // Obtient les changements de ligne (dx) et de colonne (dy) basés sur la direction
        int dx = d.getDeltaRow();
        int dy = d.getDeltaCol();

        // Parcourt les espaces dans la direction spécifiée
        for (int i = 0; i < nbTilesToPut; i++) {
            // Vérifie si une tuile existe à la position actuelle
            if (tiles[x][y] != null) {

                return false;
            }
            // Avance vers la prochaine position dans la direction spécifiée
            x = x + dx;
            y = y + dy;
        }

        // Si tous les espaces sont vides dans la direction spécifiée, renvoie true
        return true;
    }

    private boolean outsideGrid(int row,int col){
        // Vérifie si la position (row, col) se trouve en dehors de la grille 90x90
        if(row<0||row>90||col<0 ||col>90){
            return true;  // En dehors de la grille
        }
        return false;  // À l'intérieur de la grille
    }
    private boolean noTilesAround(int row,int col){
        List<Integer> position=new ArrayList<>();  // Liste pour stocker les positions valides autour de la position donnée
        // Vérification pour la position au-dessus
        int rowUp=row-1;
        int colUp=col;
        if(!outsideGrid(rowUp,colUp)){
            position.add(rowUp);
            position.add(colUp);
        }
        // Vérification pour la position en dessous
        int rowDown=row+1;
        int colDown=col;
        if(!outsideGrid(rowDown,colDown)){
            position.add(rowDown);
            position.add(colDown);
        }
        // Vérification pour la position à gauche
        int rowLeft=row;
        int colLeft=col-1;
        if(!outsideGrid(rowLeft,colLeft)){
            position.add(rowLeft);
            position.add(colLeft);
        }
        // Vérification pour la position à droite
        int rowRight=row;
        int colRight=col+1;
        if(!outsideGrid(rowRight,colRight)){
            position.add(rowRight);
            position.add(colRight);
        }
        // Vérification de chaque position autour de la position donnée
        for (int i = 0; i <position.size()-1 ; i++) {
            int x=position.get(i);
            int y=position.get(i+1);
            // Si une tuile est présente à la position (x, y), alors des tuiles sont autour
            if(getTile(x,y)!=null){
                return false;
            }
        }
        return true;  // Aucune tuile autour


    }

}


