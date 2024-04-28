package g61500.qwirkle.model;

/**
 * Exception personnalisée pour le jeu Qwirkle.
 * Cette exception est utilisée pour signaler des erreurs spécifiques au jeu Qwirkle.
 */
public class QwirkleException extends RuntimeException {

    /**
     * Constructeur de l'exception QwirkleException avec un message d'erreur.
     * paramètre message: le message d'erreur associé à l'exception
     */
    public QwirkleException(String message) {
        super(message);
    }
}