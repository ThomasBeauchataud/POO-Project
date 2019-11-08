package game;

import pieces.PieceInterface;
import pieces.TeamColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage Game situations
 */
public class GameManagement implements GameManagementInterface {

    private boolean checkmate = false;
    private boolean checkState = false;
    private boolean stalemate = false;
    private List<PieceInterface> checkPieces = new ArrayList<>();
    private List<PieceInterface> saviorPieces = new ArrayList<>();
    private PieceInterface selectedPiece = null;
    private TeamColor currentPlayer;

    @Override
    public boolean isCheckmate() {
        return checkmate;
    }

    @Override
    public void setCheckmate(boolean checkmate) {
        this.checkmate = checkmate;
    }

    @Override
    public boolean isCheckState() {
        return checkState;
    }

    @Override
    public void setCheckState(boolean checkState) {
        this.checkState = checkState;
    }

    @Override
    public boolean isStalemate() {
        return stalemate;
    }

    @Override
    public void setStalemate(boolean stalemate) {
        this.stalemate = stalemate;
    }

    @Override
    public List<PieceInterface> getCheckPieces() {
        return checkPieces;
    }

    @Override
    public List<PieceInterface> getSaviorPieces() {
        return saviorPieces;
    }

    @Override
    public PieceInterface getSelectedPiece() {
        return selectedPiece;
    }

    @Override
    public void setSelectedPiece(PieceInterface selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    @Override
    public TeamColor getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void setCurrentPlayer(TeamColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void resetGame() {
        selectedPiece = null;
        checkPieces.clear();
        saviorPieces.clear();
        currentPlayer = TeamColor.White;
    }

}
