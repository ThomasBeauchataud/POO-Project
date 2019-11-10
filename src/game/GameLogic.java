package game;

import common.Position;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import pieces.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Game Logic Management
 * TODO Add the possibility that the king eat a check piece
 */
@Aspect
@SuppressWarnings({"Duplicates","unchecked"})
public class GameLogic {

    /**
     * Prevent a player to move a piece is the game is over
     * @param proceedingJoinPoint ProceedingJoinPoint
     * @return null
     * @throws Throwable if the proceed method fails
     */
    @Around("execution(public void game.ChessBoard.selectPiece(double, double))")
    public Object beforeSelectPiece(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ChessBoard chessBoard = (ChessBoard) proceedingJoinPoint.getTarget();
        if (chessBoard.getGameManagement().isCheckmate() || chessBoard.getGameManagement().isStalemate() || chessBoard.getTimer().isTimeOver()) {
            return null;
        }
        return proceedingJoinPoint.proceed();
    }

    /**
     * Adding savior position to savior pieces
     * @param proceedingJoinPoint ProceedingJoinPoint
     * @return PieceInterface[]
     * @throws Throwable if the proceed method fails
     */
    @Around("execution(private java.util.List<pieces.PieceInterface> game.GameLogic.findAllSaviorPieces(game.ChessBoardGameInterface,int,int,pieces.TeamColor,boolean))")
    public Object afterGettingSaviorPieces(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<PieceInterface> saviorPieces = (List<PieceInterface>) proceedingJoinPoint.proceed();
        for(PieceInterface piece : saviorPieces) {
            piece.getSaviorPositions().add(new Position((int)proceedingJoinPoint.getArgs()[1], (int)proceedingJoinPoint.getArgs()[2]));
        }
        return saviorPieces;
    }

    /**
     * After the reset of the game, reset the timer and alert messages
     * @param joinPoint JoinPoint
     */
    @After("execution(public void game.ChessBoard.resetGame())")
    public void afterResetGame(JoinPoint joinPoint) {
        ChessBoard chessBoard = (ChessBoard) joinPoint.getTarget();
        chessBoard.getGameManagement().resetGame();
        chessBoard.getStatusBar().reset();
        chessBoard.getTimer().reset();

    }

    /**
     * Check if a Piece can move or not before searching movements possibilities
     * A Piece can't move in a check situation or if it a savior
     * If the piece is a savior, return savior positions
     * @param proceedingJoinPoint ProceedingJoinPoint
     * @return Position[]
     * @throws Throwable if the proceed method fails
     */
    @Around("execution(public java.util.List<common.Position> pieces.Piece.getPossibilities(game.ChessBoardGameInterface))")
    public Object canMove(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ChessBoard chessBoard = (ChessBoard) proceedingJoinPoint.getArgs()[0];
        PieceInterface piece = (Piece) proceedingJoinPoint.getTarget();
        if(chessBoard.getGameManagement().isCheckState()) {
            if (piece.isSavior()) {
                return piece.getSaviorPositions();
            }
            return new ArrayList<>();
        }
        return proceedingJoinPoint.proceed();
    }

    /**
     * Check after a Piece movement if one player is in a check situation or if one player has won
     * @param joinPoint JoinPoint
     */
    @After("execution(private void game.ChessBoard.movePiece(double, double))")
    public void afterPieceMovement(JoinPoint joinPoint) {
        ChessBoard chessBoard = (ChessBoard) joinPoint.getTarget();
        //Change the player
        TeamColor player = getEnemyTeamColor(chessBoard.getGameManagement().getCurrentPlayer());
        chessBoard.getGameManagement().setCurrentPlayer(player);
        chessBoard.getStatusBar().removeAlert(getEnemyTeamColor(player));
        chessBoard.getGameManagement().setCheckState(false);
        for (PieceInterface item : chessBoard.getGameManagement().getSaviorPieces()) {
            item.setSavior(false);
            item.getSaviorPositions().clear();
        }
        //If the player is in a check situation
        if (MovementRules.isCheck(chessBoard, chessBoard.getKing(player).getPosition().getX(), chessBoard.getKing(player).getPosition().getY(), player, true)) {
            chessBoard.getGameManagement().getCheckPieces().clear();
            chessBoard.getGameManagement().getSaviorPieces().clear();
            chessBoard.getGameManagement().setCheckState(true);
            chessBoard.getGameManagement().getCheckPieces().addAll(findAllCheckPieces(chessBoard, chessBoard.getKing(player).getPosition(), player));
            //If the player is in a checkmate situation
            if (isCheckmate(chessBoard, chessBoard.getKing(player).getPosition(), player)) {
                chessBoard.getGameManagement().setCheckmate(true);
                chessBoard.getStatusBar().alertCheckmate(player);
            }
            else {
                chessBoard.getStatusBar().alertCheck(player);
            }
        }
        //If the player is in a stalemate situation
        else if (isStalemate(chessBoard, chessBoard.getKing(TeamColor.Black), player)) {
            chessBoard.getStatusBar().alertStalemate();
        }
        else {
            chessBoard.getStatusBar().alertTurn(player);
        }
    }

    /**
     * Return if the player if Stalemate or note
     * @param chessBoard ChessBoardInterface
     * @param king Piece
     * @param teamColor TeamColor
     * @return boolean
     */
    private boolean isStalemate(ChessBoardGameInterface chessBoard, Piece king, TeamColor teamColor) {
        if (isOneKingStalemate(chessBoard, king, teamColor) || isLimitPieceStalemate(chessBoard)) {
            chessBoard.getGameManagement().setStalemate(false);
            return true;
        }
        return false;
    }

    /**
     * Return true if the player has only one King and is Stalemate
     * @param chessBoard ChessBoard
     * @param king Piece
     * @param teamColor TeamColor
     * @return boolean
     */
    private boolean isOneKingStalemate(ChessBoardGameInterface chessBoard, Piece king, TeamColor teamColor) {
        int nbPiece = 0;
        boolean stalemate = true;
        //Count the number of resting pieces
        for (int y = 0; y < ChessBoard.boardSize; y++) {
            for (int x = 0; x < ChessBoard.boardSize; x++) {
                if (chessBoard.getBoardPosition(x, y) == teamColor) {
                    nbPiece++;
                }
            }
        }
        //If the king is alone
        if (nbPiece == 1) {
            for (int y = king.getPosition().getY() - 1; y <= king.getPosition().getY() + 1; y++) {
                for (int x = king.getPosition().getX() - 1; x <= king.getPosition().getX() + 1; x++) {
                    if(pieceInCenterAndNextToEnemyAndNotInCheck(chessBoard, x, y, teamColor)) {
                        stalemate = false;
                        break;
                    }
                }
                if (!stalemate) {
                    break;
                }
            }
        }
        else {
            stalemate = false;
        }
        return stalemate;
    }

    /**
     * Return true if a Piece is not next to a border and next to a enemy and the player is in check situation
     * @param chessBoard ChessBoard
     * @param x int
     * @param y int
     * @param teamColor TeamColor
     * @return boolean
     */
    private boolean pieceInCenterAndNextToEnemyAndNotInCheck(ChessBoardGameInterface chessBoard, int x, int y, TeamColor teamColor) {
        if(y >= 0 && y < ChessBoard.boardSize && x >= 0 && x < ChessBoard.boardSize && chessBoard.getBoardPosition(x, y) != teamColor) {
            return !MovementRules.isCheck(chessBoard, x, y, teamColor, true);
        }
        return false;
    }

    /**
     * Return this list of all pieces which create a check situation
     * @param chessBoard ChessBoard
     * @param position Position, the Kink position
     * @param teamColor TeamColor, the player
     * @return PieceInterface[]
     */
    private List<PieceInterface> findAllCheckPieces(ChessBoardGameInterface chessBoard, Position position, TeamColor teamColor) {
        int xPos = position.getX();
        int yPos = position.getY();
        List<PieceInterface> checkPieces = new ArrayList<>();
        TeamColor enemyType = getEnemyTeamColor(teamColor);
        //Horizontal Left Alignment
        for (int x = xPos - 1; x >= 0; x--) {
            //If the first piece on the alignment is ally, there is no check pieces on the alignment
            if (chessBoard.getBoardPosition(x, yPos) == teamColor) {
                break;
            }
            //If the piece on the alignment is enemy
            else if (chessBoard.getBoardPosition(x, yPos) == enemyType) {
                //If its a Rook or a Queen
                if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook)) {
                    checkPieces.add(chessBoard.getPiece(x, yPos));
                }
                break;
            }
        }
        //Horizontal Right Alignment
        for (int x = xPos + 1; x < ChessBoard.boardSize; x++) {
            //If the first piece on the alignment is ally, there is no check pieces on the alignment
            if (chessBoard.getBoardPosition(x, yPos) == teamColor) {
                break;
            }
            //If the piece on the alignment is enemy
            else if (chessBoard.getBoardPosition(x, yPos) == enemyType) {
                //If its a Rook or a Queen
                if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook)) {
                    checkPieces.add(chessBoard.getPiece(x, yPos));
                }
                break;
            }
        }
        //Vertical Up Alignment
        for (int y = yPos - 1; y >= 0; y--) {
            //If the first piece on the alignment is ally, there is no check pieces on the alignment
            if (chessBoard.getBoardPosition(xPos, y) == teamColor) {
                break;
            }
            //If the piece on the alignment is enemy
            else if (chessBoard.getBoardPosition(xPos, y) == enemyType) {
                //If its a Rook or a Queen
                if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook)) {
                    checkPieces.add(chessBoard.getPiece(xPos, y));
                }
                break;
            }
        }
        //Vertical Down Alignment
        for (int y = yPos + 1; y < ChessBoard.boardSize; y++) {
            //If the first piece on the alignment is ally, there is no check pieces on the alignment
            if (chessBoard.getBoardPosition(xPos, y) == teamColor) {
                break;
            }
            //If the piece on the alignment is enemy
            else if (chessBoard.getBoardPosition(xPos, y) == enemyType) {
                //If its a Rook or a Queen
                if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook)) {
                    checkPieces.add(chessBoard.getPiece(xPos, y));
                }
                break;
            }
        }
        //Diagonal Left Up Alignment
        for (int y = yPos - 1, x = xPos - 1; y >= 0 && x >= 0; y--, x--) {
            //If the first piece on the alignment is ally, there is no check pieces on the alignment
            if (chessBoard.getBoardPosition(x, y) == teamColor) {
                break;
            }
            //If the piece on the alignment is enemy
            else if (chessBoard.getBoardPosition(x, y) == enemyType) {
                //If its a Pawn and its next to the piece
                if (y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn)) {
                    checkPieces.add(chessBoard.getPiece(x, y));
                }
                //If its a Queen or a Bishop
                else if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop)) {
                    checkPieces.add(chessBoard.getPiece(x, y));
                }
                break;
            }
        }
        //Diagonal Down Right Alignment
        for (int y = yPos + 1, x = xPos + 1; y < ChessBoard.boardSize && x < ChessBoard.boardSize; y++, x++) {
            //If the first piece on the alignment is ally, there is no check pieces on the alignment
            if (chessBoard.getBoardPosition(x, y) == teamColor) {
                break;
            }
            else if (chessBoard.getBoardPosition(x, y) == enemyType) {
                //If its a Pawn and its next to the piece
                if (y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn)) {
                    checkPieces.add(chessBoard.getPiece(x, y));
                }
                //If its a Queen or a Bishop
                else if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop)) {
                    checkPieces.add(chessBoard.getPiece(x, y));
                }
                break;
            }
        }
        //Diagonal Up Right Alignment
        for (int y = yPos - 1, x = xPos + 1; y >= 0 && x < ChessBoard.boardSize; y--, x++) {
            //If the first piece on the alignment is ally, there is no check pieces on the alignment
            if (chessBoard.getBoardPosition(x, y) == teamColor) {
                break;
            }
            //If the piece on the alignment is enemy
            else if (chessBoard.getBoardPosition(x, y) == enemyType) {
                //If its a Pawn and its next to the piece
                if (y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn)) {
                    checkPieces.add(chessBoard.getPiece(x, y));
                }
                //If its a Queen or a Bishop
                else if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop)) {
                    checkPieces.add(chessBoard.getPiece(x, y));
                }
                break;
            }
        }
        //Diagonal Down Right Alignment
        for (int y = yPos + 1, x = xPos - 1; y < ChessBoard.boardSize && x >= 0; y++, x--) {
            //If the first piece on the alignment is ally, there is no check pieces on the alignment
            if (chessBoard.getBoardPosition(x, y) == teamColor) {
                break;
            }
            //If the piece on the alignment is enemy
            else if (chessBoard.getBoardPosition(x, y) == enemyType) {
                //If its a Pawn and its next to the piece
                if (y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn)) {
                    checkPieces.add(chessBoard.getPiece(x, y));
                }
                //If its a Queen or a Bishop
                if (chessBoard.getBoardPosition(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop)) {
                    checkPieces.add(chessBoard.getPiece(x, y));
                }
                break;
            }
        }
        //Knight Alignment
        for (int y = -2; y <= 2; y++) {
            if (y != 0) {
                int x = y % 2 == 0 ? 1 : 2;
                if (yPos + y >= 0 && yPos + y < ChessBoard.boardSize && xPos - x >= 0 && xPos - x < ChessBoard.boardSize && chessBoard.getBoardPosition(xPos - x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos - x, yPos + y) != null) {
                    if (chessBoard.getPiece(xPos - x, yPos + y) != null && chessBoard.getPiece(xPos - x, yPos + y) instanceof Knight) {
                        checkPieces.add(chessBoard.getPiece(xPos - x, yPos + y));
                    }
                }
                if (yPos + y >= 0 && yPos + y < ChessBoard.boardSize && xPos + x >= 0 && xPos + x < ChessBoard.boardSize && chessBoard.getBoardPosition(xPos + x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos + x, yPos + y) != null) {
                    if (chessBoard.getPiece(xPos + x, yPos + y) != null && chessBoard.getPiece(xPos + x, yPos + y) instanceof Knight) {
                        checkPieces.add(chessBoard.getPiece(xPos + x, yPos + y));
                    }
                }
            }
        }
        return checkPieces;
    }

    /**
     * Return true if the player is in a Checkmate situation
     * @param chessBoard ChessBoard
     * @param position Position, the King Position
     * @param teamColor TeamColor, the player
     * @return boolean
     */
    private boolean isCheckmate(ChessBoardGameInterface chessBoard, Position position, TeamColor teamColor) {
        int xPos = position.getX();
        int yPos = position.getY();
        boolean checkmate = true;
        for (int y = yPos - 1; y <= yPos + 1; y++) {
            for (int x = xPos - 1; x <= xPos + 1; x++) {
                if(pieceInCenterAndNextToEnemyAndNotInCheck(chessBoard, x, y, teamColor)) {
                    checkmate = false;
                    break;
                }
            }
            if (!checkmate)
                break;
        }
        //If there is less than 3 piece that create a check situation
        if (chessBoard.getGameManagement().getCheckPieces().size() < 2) {
            PieceInterface checkPiece = chessBoard.getGameManagement().getCheckPieces().get(0);
            canCapture(chessBoard, checkPiece);
            canProtect(chessBoard, position, checkPiece);
            if (!chessBoard.getGameManagement().getSaviorPieces().isEmpty()) {
                for(Iterator<PieceInterface> piece = chessBoard.getGameManagement().getSaviorPieces().iterator(); piece.hasNext(); ) {
                    PieceInterface item = piece.next();
                    item.setSavior(true);
                    if (MovementRules.verticalProtection(chessBoard, item.getPosition().getX(), item.getPosition().getY(), item.getTeamColor()) || MovementRules.horizontalProtection(chessBoard, item.getPosition().getX(), item.getPosition().getY(), item.getTeamColor()) || MovementRules.slashDiagonalProtection(chessBoard, item.getPosition().getX(), item.getPosition().getY(), item.getTeamColor()) || MovementRules.backslashDiagonalProtection(chessBoard, item.getPosition().getX(), item.getPosition().getY(), item.getTeamColor())) {
                        item.setSavior(false);
                        piece.remove();
                    }
                }
            }
            if (!chessBoard.getGameManagement().getSaviorPieces().isEmpty()) {
                checkmate = false;
            }
        }
        return checkmate;
    }

    /**
     * Find all piece that can protected from a check situation from the piece checkPiece and store them in the GameManagement
     * @param chessboard ChessBoard
     * @param position Position, the King position
     * @param checkPiece PieceInterface
     */
    private void canProtect(ChessBoardGameInterface chessboard, Position position, PieceInterface checkPiece) {
        List<PieceInterface> saviorPieces =  new ArrayList<>();
        //Handle Vertical Up threat
        if (position.getX() == checkPiece.getPosition().getX() && position.getY() > checkPiece.getPosition().getY()) {
            for (int y = checkPiece.getPosition().getY() + 1; y < position.getY(); y++) {
                saviorPieces.addAll(findAllSaviorPieces(chessboard, checkPiece.getPosition().getX(), y, checkPiece.getTeamColor(), true));
            }
        }
        //Handle Vertical Down threat
        if (position.getX() == checkPiece.getPosition().getX() && position.getY() < checkPiece.getPosition().getY()) {
            for (int y = checkPiece.getPosition().getY() - 1; y > position.getY(); y--) {
                saviorPieces.addAll(findAllSaviorPieces(chessboard, checkPiece.getPosition().getX(), y, checkPiece.getTeamColor(), true));
            }
        }
        //Handle Horizontal Left threat
        if (position.getX() > checkPiece.getPosition().getX() && position.getY() == checkPiece.getPosition().getY()) {
            for (int x = checkPiece.getPosition().getX() + 1; x < position.getX(); x++) {
                saviorPieces.addAll(findAllSaviorPieces(chessboard, x, checkPiece.getPosition().getY(), checkPiece.getTeamColor(), true));
            }
        }
        //Handle Horizontal Right threat
        if (position.getX() < checkPiece.getPosition().getX() && position.getY() == checkPiece.getPosition().getY()) {
            for (int x = checkPiece.getPosition().getX() - 1; x > position.getX(); x--) {
                saviorPieces.addAll(findAllSaviorPieces(chessboard, x, checkPiece.getPosition().getY(), checkPiece.getTeamColor(), true));
            }
        }
        //Handle Diagonal Up Left threat
        int y = checkPiece.getPosition().getY() + 1;
        if (position.getX() > checkPiece.getPosition().getX() && position.getY() > checkPiece.getPosition().getY()) {
            for (int x = checkPiece.getPosition().getX() + 1; x < position.getX() && y < position.getY(); x++, y++) {
                saviorPieces.addAll(findAllSaviorPieces(chessboard, x, y, checkPiece.getTeamColor(), true));
            }
        }
        //Handle Diagonal Down Right threat
        y = checkPiece.getPosition().getY() - 1;
        if (position.getX() < checkPiece.getPosition().getX() && position.getY() < checkPiece.getPosition().getY()) {
            for (int x = checkPiece.getPosition().getX() - 1; x > position.getX() && y > position.getY(); x--, y--) {
                saviorPieces.addAll(findAllSaviorPieces(chessboard, x, y, checkPiece.getTeamColor(), true));
            }
        }
        //Handle Diagonal Up Right threat
        y = checkPiece.getPosition().getY() + 1;
        if (position.getX() < checkPiece.getPosition().getX() && position.getY() > checkPiece.getPosition().getY()) {
            for (int x = checkPiece.getPosition().getX() - 1; x > position.getX() && y < position.getY(); x--, y++) {
                saviorPieces.addAll(findAllSaviorPieces(chessboard, x, y, checkPiece.getTeamColor(), true));
            }
        }
        //Handle Diagonal Down Left threat
        y = checkPiece.getPosition().getY() - 1;
        if (position.getX() > checkPiece.getPosition().getX() && position.getY() < checkPiece.getPosition().getY()) {
            for (int x = checkPiece.getPosition().getX() + 1; x < position.getX() && y > position.getY(); x++, y--) {
                saviorPieces.addAll(findAllSaviorPieces(chessboard, x, y, checkPiece.getTeamColor(), true));
            }
        }
        chessboard.getGameManagement().getSaviorPieces().addAll(saviorPieces);
    }

    /**
     * Return all Pieces that can save for a check situation by placing them between the king and the check piece
     * @param chessBoard ChessBoardGameInterface
     * @param xPos int
     * @param yPos int
     * @param enemyPlayer TeamColor
     * @param protect boolean, false we are looking for capture and not for protection
     * @return PieceInterface[]
     */
    private List<PieceInterface> findAllSaviorPieces(ChessBoardGameInterface chessBoard, int xPos, int yPos, TeamColor enemyPlayer, boolean protect) {
        List<PieceInterface> saviorPieces =  new ArrayList<>();
        TeamColor player = getEnemyTeamColor(enemyPlayer);
        //Horizontal Left Alignment
        for(int x = xPos - 1; x >= 0; x--) {
            if (chessBoard.getBoardPosition(x, yPos) == enemyPlayer) {
                break;
            }
            else if (chessBoard.getBoardPosition(x, yPos) == player) {
                if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook)) {
                    saviorPieces.add(chessBoard.getPiece(x, yPos));
                } else {
                    break;
                }
            }
        }
        //Horizontal Right Alignment
        for(int x = xPos + 1; x < ChessBoard.boardSize; x++) {
            if (chessBoard.getBoardPosition(x, yPos) == enemyPlayer) {
                break;
            }
            else if (chessBoard.getBoardPosition(x, yPos) == player) {
                if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook)) {
                    saviorPieces.add(chessBoard.getPiece(x, yPos));
                }
                else {
                    break;
                }
            }
        }
        //Vertical Up Alignment
        for (int y = yPos - 1; y >= 0; y--) {
            if (chessBoard.getBoardPosition(xPos, y) == enemyPlayer) {
                break;
            }
            else if (chessBoard.getBoardPosition(xPos, y) == player) {
                if (player == TeamColor.Black && protect && y == yPos - 1 && chessBoard.getPiece(xPos, y) != null && chessBoard.getPiece(xPos, y) instanceof Pawn) {
                    saviorPieces.add(chessBoard.getPiece(xPos, y));
                }
                if (player == TeamColor.Black && protect && y == yPos - 2 && chessBoard.getPiece(xPos, y) != null && chessBoard.getPiece(xPos, y) instanceof Pawn && chessBoard.getPiece(xPos, y).isFirstTime()) {
                    saviorPieces.add(chessBoard.getPiece(xPos, y));
                }
                if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook)) {
                    saviorPieces.add(chessBoard.getPiece(xPos, y));
                }
                else {
                    break;
                }
            }
        }
        //Vertical Down Alignment
        for (int y = yPos + 1; y < ChessBoard.boardSize; y++) {
            if (chessBoard.getBoardPosition(xPos, y) == enemyPlayer) {
                break;
            }
            else if (chessBoard.getBoardPosition(xPos, y) == player) {
                if (player == TeamColor.White && protect && y == yPos + 1 && chessBoard.getPiece(xPos, y) != null && chessBoard.getPiece(xPos, y) instanceof Pawn) {
                    saviorPieces.add(chessBoard.getPiece(xPos, y));
                }
                if (player == TeamColor.White && protect && y == yPos + 2 && chessBoard.getPiece(xPos, y) != null && chessBoard.getPiece(xPos, y) instanceof Pawn && chessBoard.getPiece(xPos, y).isFirstTime()) {
                    saviorPieces.add(chessBoard.getPiece(xPos, y));
                }
                if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook)) {
                    saviorPieces.add(chessBoard.getPiece(xPos, y));
                }
                else {
                    break;
                }
            }
        }
        //Diagonal Up Left Alignment
        for (int y = yPos - 1, x = xPos - 1; y >= 0 && x >= 0; y--, x--) {
            if (chessBoard.getBoardPosition(x, y) == enemyPlayer) {
                break;
            }
            else if (chessBoard.getBoardPosition(x, y) == player) {
                if (!protect && y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (enemyPlayer == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn)) {
                    saviorPieces.add(chessBoard.getPiece(x, y));
                }
                if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop)) {
                    saviorPieces.add(chessBoard.getPiece(x, y));
                }
                else {
                    break;
                }
            }
        }
        //Diagonal Down Right Alignment
        for (int y = yPos + 1, x = xPos + 1; y < ChessBoard.boardSize && x < ChessBoard.boardSize; y++, x++) {
            if (chessBoard.getBoardPosition(x, y) == enemyPlayer) {
                break;
            }
            else if (chessBoard.getBoardPosition(x, y) == player) {
                if (!protect && y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (enemyPlayer == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn)) {
                    saviorPieces.add(chessBoard.getPiece(x, y));
                }
                if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop)) {
                    saviorPieces.add(chessBoard.getPiece(x, y));
                }
                else {
                    break;
                }
            }
        }
        //Diagonal Up Right Alignment
        for (int y = yPos - 1, x = xPos + 1; y >= 0 && x < ChessBoard.boardSize; y--, x++) {
            if (chessBoard.getBoardPosition(x, y) == enemyPlayer) {
                break;
            }
            else if (chessBoard.getBoardPosition(x, y) == player) {
                if (!protect && y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (enemyPlayer == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn)) {
                    saviorPieces.add(chessBoard.getPiece(x, y));
                }
                if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop)) {
                    saviorPieces.add(chessBoard.getPiece(x, y));
                }
                else {
                    break;
                }
            }
        }
        //Diagonal Down Left Alignment
        for (int y = yPos + 1, x = xPos - 1; y < ChessBoard.boardSize && x >= 0; y++, x--) {
            if (chessBoard.getBoardPosition(x, y) == enemyPlayer) {
                break;
            }
            else if (chessBoard.getBoardPosition(x, y) == player) {
                if (!protect && y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (enemyPlayer == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn)) {
                    saviorPieces.add(chessBoard.getPiece(x, y));
                }
                if (chessBoard.getBoardPosition(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop)) {
                    saviorPieces.add(chessBoard.getPiece(x, y));
                }
                else {
                    break;
                }
            }
        }
        //Knight Alignment
        for (int y = -2; y <= 2; y++) {
            if (y != 0) {
                int x = y % 2 == 0 ? 1 : 2;
                if (yPos + y >= 0 && yPos + y < ChessBoard.boardSize && xPos - x >= 0 && xPos - x < ChessBoard.boardSize && chessBoard.getBoardPosition(xPos - x, yPos + y) != enemyPlayer && chessBoard.getBoardPosition(xPos - x, yPos + y) != null) {
                    if (chessBoard.getPiece(xPos - x, yPos + y) != null && chessBoard.getPiece(xPos - x, yPos + y) instanceof Knight) {
                        saviorPieces.add(chessBoard.getPiece(xPos - x, yPos + y));
                    }
                }
                if (yPos + y >= 0 && yPos + y < ChessBoard.boardSize && xPos + x >= 0 && xPos + x < ChessBoard.boardSize && chessBoard.getBoardPosition(xPos + x, yPos + y) != enemyPlayer && chessBoard.getBoardPosition(xPos + x, yPos + y) != null) {
                    if (chessBoard.getPiece(xPos + x, yPos + y) != null && chessBoard.getPiece(xPos + x, yPos + y) instanceof Knight) {
                        saviorPieces.add(chessBoard.getPiece(xPos + x, yPos + y));
                    }
                }
            }
        }
        return saviorPieces;
    }

    /**
     * Find all piece that can capture the check piece and store them in the GameManagement
     * @param chessboard ChessBoardGameInterface
     * @param checkPiece PieceInterface
     */
    private void canCapture(ChessBoardGameInterface chessboard, PieceInterface checkPiece) {
        chessboard.getGameManagement().getSaviorPieces().addAll(findAllSaviorPieces(chessboard, checkPiece.getPosition().getX(), checkPiece.getPosition().getY(), checkPiece.getTeamColor(), false));
    }

    /**
     * Return true is where in a stalemate situation
     * @param chessBoard ChessBoardGameInterface
     * @return boolean
     */
    private boolean isLimitPieceStalemate(ChessBoardGameInterface chessBoard) {
        if (piecesCount(chessBoard, Queen.class, TeamColor.White) != 0 || piecesCount(chessBoard, Queen.class, TeamColor.Black) != 0) {
            return false;
        }
        else if (piecesCount(chessBoard, Rook.class, TeamColor.White) != 0 || piecesCount(chessBoard, Rook.class, TeamColor.Black) != 0) {
            return false;
        }
        else if (piecesCount(chessBoard, Knight.class, TeamColor.White) > 1 || piecesCount(chessBoard, Knight.class, TeamColor.Black) > 1) {
            return false;
        }
        else if (((piecesCount(chessBoard, Bishop.class, TeamColor.White) != 0) && piecesCount(chessBoard, Knight.class, TeamColor.White) != 0) ||
                ((piecesCount(chessBoard, Bishop.class, TeamColor.Black) != 0) && piecesCount(chessBoard, Knight.class, TeamColor.Black) != 0)) {
            return false;
        }
        else if ((piecesCount(chessBoard, Bishop.class, TeamColor.White) == 2) || (piecesCount(chessBoard, Bishop.class, TeamColor.Black) == 2)) {
            return false;
        }
        else {
            return piecesCount(chessBoard, Pawn.class, TeamColor.White) <= 1 && piecesCount(chessBoard, Pawn.class, TeamColor.White) <= 1;
        }
    }

    /**
     * Return the number of Piece of a specific class resting on the board
     * @param chessBoard ChessBoardGameInterface
     * @param pieceClass Class
     * @param teamColor TeamColor
     * @return int
     */
    private int piecesCount(ChessBoardGameInterface chessBoard, Class pieceClass, TeamColor teamColor) {
        int count = 0;
        for(int x = 0 ; x < ChessBoard.boardSize ; x++) {
            for(int y = 0 ; y < ChessBoard.boardSize ; y++) {
                PieceInterface piece = chessBoard.getPiece(x,y);
                if(piece != null && piece.getClass() == pieceClass && piece.getTeamColor() == teamColor) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Return the opposite TeamColor
     * @param teamColor TeamColor
     * @return TeamColor
     */
    public static TeamColor getEnemyTeamColor(TeamColor teamColor) {
        if (teamColor == TeamColor.White) {
            return TeamColor.Black;
        }
        else {
            return TeamColor.White;
        }
    }

}
