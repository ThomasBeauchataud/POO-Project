package game;

import pieces.PieceInterface;
import pieces.TeamColor;

import java.util.List;

/**
 * Manage Game situations
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface GameManagementInterface {

    /**
     * Return true if there is a Checkmate
     * @return boolean
     */
    boolean isCheckmate();

    /**
     * Set the Checkmate situation
     * @param checkmate boolean
     */
    void setCheckmate(boolean checkmate);

    /**
     * Return if there is a Check situation
     * @return boolean
     */
    boolean isCheckState();

    /**
     * Set the Check situation
     * @param checkState boolean
     */
    void setCheckState(boolean checkState);

    /**
     * Return if the actual player can move
     * @return boolean
     */
    boolean isStalemate();

    /**
     * Set if the actual player can move
     * @param stalemate boolean
     */
    void setStalemate(boolean stalemate);

    /**
     * Get Pieces that produce the Check situation
     * @return PiecesInterface[]
     */
    List<PieceInterface> getCheckPieces();

    /**
     * Get Pieces that can move to cancel the Check situation
     * @return PieceInterface[]
     */
    List<PieceInterface> getSaviorPieces();

    /**
     * Return the select Piece
     * @return PieceInterface
     */
    PieceInterface getSelectedPiece();

    /**
     * Set the select Piece
     * @param selectedPiece PieceInterface
     */
    void setSelectedPiece(PieceInterface selectedPiece);

    /**
     * Get the current player TeamColor
     * @return TeamColor
     */
    TeamColor getCurrentPlayer();

    /**
     * Set the current player TeamColor
     * @param currentPlayer TeamColor
     */
    void setCurrentPlayer(TeamColor currentPlayer);

    /**
     * Reset the game
     */
    void resetGame();

}
