package game;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import pieces.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Game Logic Management
 */
@Aspect
public class GameLogic {

    /**
     * Check if a Piece can move or not before searching movements possibilities
     * A Piece can't move in a check situation or if it a savior
     * @param proceedingJoinPoint ProceedingJoinPoint
     * @return Position[]
     * @throws Throwable if the proceed method fail
     */
    @Around("execution(public java.util.List<common.Position> pieces.Piece.getPossibilities(game.ChessBoardGameInterface))")
    public Object test(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ChessBoard chessBoard = (ChessBoard) proceedingJoinPoint.getArgs()[0];
        PieceInterface piece = (Piece) proceedingJoinPoint.getTarget();
        if (chessBoard.getGameManagement().isCheckState() && !piece.isSavior()) {
            return new ArrayList<>();
        } else {
            return proceedingJoinPoint.proceed();
        }
    }

    /**
     * Check after a Piece movement if one player is in a check situation or if one player has won
     * @param joinPoint JoinPoint
     */
    @After("execution(public void game.ChessBoard.movePiece(double, double))")
    public void afterPieceMovement(JoinPoint joinPoint) {
        ChessBoard chessBoard = (ChessBoard) joinPoint.getTarget();
        //Change the player
        TeamColor player = getEnemyTeamColor(chessBoard.getGameManagement().getCurrentPlayer());
        chessBoard.getGameManagement().setCurrentPlayer(player);
        chessBoard.getStatusBar().removeAlert(getEnemyTeamColor(player));
        chessBoard.getGameManagement().setCheckState(false);
        for (PieceInterface item : chessBoard.getGameManagement().getSaviorPieces()) {
            item.setSavior(false);
        }
        //If the player is in a check situation
        if (MovementRules.isCheck(chessBoard, chessBoard.getKing(player).getPosition().getX(), chessBoard.getKing(player).getPosition().getY(), player, true)) {
            chessBoard.getGameManagement().getCheckPieces().clear();
            chessBoard.getGameManagement().getSaviorPieces().clear();
            chessBoard.getGameManagement().setCheckState(true);
            findAllCheckPieces(chessBoard, chessBoard.getKing(player).getPosition().getX(), chessBoard.getKing(player).getPosition().getY(), player);
            //If the player is in a checkmate situation
            if (isCheckmate(chessBoard, chessBoard.getKing(player).getPosition().getX(), chessBoard.getKing(player).getPosition().getY(), player)) {
                chessBoard.getGameManagement().setCheckmate(true);
                chessBoard.getStatusBar().alertCheckmate(player);
                chessBoard.getStatusBar().alertWinner(getEnemyTeamColor(player));
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
        chessBoard.getTimer().setPlayerTurn(player);
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
        for (int y = 0; y < ChessBoard.boardSize; y++) {
            for (int x = 0; x < ChessBoard.boardSize; x++) {
                if (chessBoard.getBoardPosition(x, y) == teamColor) {
                    nbPiece++;
                }
            }
        }
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

    //TODO make this method returning a Piece list of add this list of the game management
    private void findAllCheckPieces(ChessBoardGameInterface chessBoard, int xPos, int yPos, TeamColor teamColor) {
        int y;
        int x;
        TeamColor enemyType = getEnemyTeamColor(teamColor);

        // Horizontal Left
        for (x = xPos - 1; x >= 0; x--)
        {
            if (chessBoard.getBoardPosition(x, yPos) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(x, yPos) == enemyType)
            {
                if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook))
                    chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(x, yPos));
                else
                    break;
            }
        }
        // Horizontal Right
        for (x = xPos + 1; x < ChessBoard.boardSize; x++)
        {
            if (chessBoard.getBoardPosition(x, yPos) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(x, yPos) == enemyType)
            {
                if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook))
                    chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(x, yPos));
                else
                    break;
            }
        }
        // Vertical Up
        for (y = yPos - 1; y >= 0; y--)
        {
            if (chessBoard.getBoardPosition(xPos, y) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(xPos, y) == enemyType)
            {
                if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook))
                    chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(xPos, y));
                else
                    break;
            }
        }
        // Vertical Down
        for (y = yPos + 1; y < ChessBoard.boardSize; y++)
        {
            if (chessBoard.getBoardPosition(xPos, y) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(xPos, y) == enemyType)
            {
                if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook))
                    chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(xPos, y));
                else
                    break;
            }
        }
        // Diagonal 1 \ Up
        for (y = yPos - 1, x = xPos - 1; y >= 0 && x >= 0; y--, x--)
        {
            if (chessBoard.getBoardPosition(x, y) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(x, y) == enemyType)
            {
                if (y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn))
                    chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(x, y));
                else if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
                    chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(x, y));
                else
                    break;
            }
        }
        // Diagonal 1 \ Down
        for (y = yPos + 1, x = xPos + 1; y < ChessBoard.boardSize && x < ChessBoard.boardSize; y++, x++)
        {
            if (chessBoard.getBoardPosition(x, y) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(x, y) == enemyType)
            {
                if (y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn))
                    chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(x, y));
                else if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
                    chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(x, y));
                else
                    break;
            }
        }
        // Diagonal 2 / Up
        for (y = yPos - 1, x = xPos + 1; y >= 0 && x < ChessBoard.boardSize; y--, x++)
        {
            if (chessBoard.getBoardPosition(x, y) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(x, y) == enemyType)
            {
                if (y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn))
                    chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(x, y));
                else if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
                    chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(x, y));
                else
                    break;
            }
        }
        // Diagonal 2 / Down
        for (y = yPos + 1, x = xPos - 1; y < ChessBoard.boardSize && x >= 0; y++, x--)
        {
            if (chessBoard.getBoardPosition(x, y) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(x, y) == enemyType)
            {
                if (y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn))
                    chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(x, y));
                if (chessBoard.getBoardPosition(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
                    chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(x, y));
                else
                    break;
            }
        }
        // Knight
        for (y = -2; y <= 2; y++)
        {
            if (y != 0)
            {
                x = y % 2 == 0 ? 1 : 2;
                if (yPos + y >= 0 && yPos + y < ChessBoard.boardSize && xPos - x >= 0 && xPos - x < ChessBoard.boardSize && chessBoard.getBoardPosition(xPos - x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos - x, yPos + y) != null)
                {
                    if (chessBoard.getPiece(xPos - x, yPos + y) != null && chessBoard.getPiece(xPos - x, yPos + y) instanceof Knight)
                        chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(xPos - x, yPos + y));
                }
                if (yPos + y >= 0 && yPos + y < ChessBoard.boardSize && xPos + x >= 0 && xPos + x < ChessBoard.boardSize && chessBoard.getBoardPosition(xPos + x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos + x, yPos + y) != null)
                {
                    if (chessBoard.getPiece(xPos + x, yPos + y) != null && chessBoard.getPiece(xPos + x, yPos + y) instanceof Knight)
                        chessBoard.getGameManagement().getCheckPieces().add(chessBoard.getPiece(xPos + x, yPos + y));
                }
            }
        }
    }

    private boolean isCheckmate(ChessBoardGameInterface chessBoard, int xPos, int yPos, TeamColor teamColor) {
        boolean checkmate = true;
        int x;
        int y;
        for (y = yPos - 1; y <= yPos + 1; y++) {
            for (x = xPos - 1; x <= xPos + 1; x++) {
                if(pieceInCenterAndNextToEnemyAndNotInCheck(chessBoard, x, y, teamColor)) {
                    checkmate = false;
                    break;
                }
            }
            if (!checkmate)
                break;
        }
        if (chessBoard.getGameManagement().getCheckPieces().size() < 2)
        {
            PieceInterface checkPiece = chessBoard.getGameManagement().getCheckPieces().get(0);
            canCapture(chessBoard, checkPiece);
            canProtect(chessBoard, xPos, yPos, teamColor, checkPiece);
            if (!chessBoard.getGameManagement().getSaviorPieces().isEmpty())
            {
                for(Iterator<PieceInterface> piece = chessBoard.getGameManagement().getSaviorPieces().iterator(); piece.hasNext(); )
                {
                    PieceInterface item = piece.next();
                    item.setSavior(true);
                    if (MovementRules.verticalProtection(chessBoard, item.getPosition().getX(), item.getPosition().getY(), item.getTeamColor()) || MovementRules.horizontalProtection(chessBoard, item.getPosition().getX(), item.getPosition().getY(), item.getTeamColor()) ||
                            MovementRules.slashDiagonalProtection(chessBoard, item.getPosition().getX(), item.getPosition().getY(), item.getTeamColor()) || MovementRules.backslashDiagonalProtection(chessBoard, item.getPosition().getX(), item.getPosition().getY(), item.getTeamColor()))
                    {
                        item.setSavior(false);
                        piece.remove();
                    }
                }
            }
            if (!chessBoard.getGameManagement().getSaviorPieces().isEmpty())
                checkmate = false;
        }
        return (checkmate);
    }

    private void canProtect(ChessBoardGameInterface chessboard, int xPos, int yPos, TeamColor teamColor, PieceInterface checkPiece)
    {
        if (checkPiece instanceof Knight || checkPiece instanceof Pawn)
            return;
        // Vertical up threat
        if (xPos == checkPiece.getPosition().getX() && yPos > checkPiece.getPosition().getY())
            for (int y = checkPiece.getPosition().getY() + 1; y < yPos; y++)
                findAllSaviorPieces(chessboard, checkPiece.getPosition().getX(), y, checkPiece.getTeamColor(), true);
        // Vertical down threat
        if (xPos == checkPiece.getPosition().getX() && yPos < checkPiece.getPosition().getY())
            for (int y = checkPiece.getPosition().getY() - 1; y > yPos; y--)
                findAllSaviorPieces(chessboard, checkPiece.getPosition().getX(), y, checkPiece.getTeamColor(), true);
        // Horizontal left threat
        if (xPos > checkPiece.getPosition().getX() && yPos == checkPiece.getPosition().getY())
            for (int x = checkPiece.getPosition().getX() + 1; x < xPos; x++)
                findAllSaviorPieces(chessboard, x, checkPiece.getPosition().getY(), checkPiece.getTeamColor(), true);
        // Horizontal right threat
        if (xPos < checkPiece.getPosition().getX() && yPos == checkPiece.getPosition().getY())
            for (int x = checkPiece.getPosition().getX() - 1; x > xPos; x--)
                findAllSaviorPieces(chessboard, x, checkPiece.getPosition().getY(), checkPiece.getTeamColor(), true);
        // Diagonal 1 \ up threat
        int y = checkPiece.getPosition().getY() + 1;
        if (xPos > checkPiece.getPosition().getX() && yPos > checkPiece.getPosition().getY())
            for (int x = checkPiece.getPosition().getX() + 1; x < xPos && y < yPos; x++, y++)
                findAllSaviorPieces(chessboard, x, y, checkPiece.getTeamColor(), true);
        // Diagonal 1 \ down threat
        y = checkPiece.getPosition().getY() - 1;
        if (xPos < checkPiece.getPosition().getX() && yPos < checkPiece.getPosition().getY())
            for (int x = checkPiece.getPosition().getX() - 1; x > xPos && y > yPos; x--, y--)
                findAllSaviorPieces(chessboard, x, y, checkPiece.getTeamColor(), true);
        // Diagonal 2 / up threat
        y = checkPiece.getPosition().getY() + 1;
        if (xPos < checkPiece.getPosition().getX() && yPos > checkPiece.getPosition().getY())
            for (int x = checkPiece.getPosition().getX() - 1; x > xPos && y < yPos; x--, y++)
                findAllSaviorPieces(chessboard, x, y, checkPiece.getTeamColor(), true);
        // Diagonal 2 / down threat
        y = checkPiece.getPosition().getY() - 1;
        if (xPos > checkPiece.getPosition().getX() && yPos < checkPiece.getPosition().getY())
            for (int x = checkPiece.getPosition().getX() + 1; x < xPos && y > yPos; x++, y--)
                findAllSaviorPieces(chessboard, x, y, checkPiece.getTeamColor(), true);
    }

    private void findAllSaviorPieces(ChessBoardGameInterface chessBoard, int xPos, int yPos, TeamColor teamColor, boolean protect) {
        int y;
        int x;
        TeamColor enemyType = getEnemyTeamColor(teamColor);

        // Horizontal Left
        for (x = xPos - 1; x >= 0; x--)
        {
            if (chessBoard.getBoardPosition(x, yPos) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(x, yPos) == enemyType)
            {
                if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook))
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(x, yPos));
                else
                    break;
            }
        }
        // Horizontal Right
        for (x = xPos + 1; x < ChessBoard.boardSize; x++)
        {
            if (chessBoard.getBoardPosition(x, yPos) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(x, yPos) == enemyType)
            {
                if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook))
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(x, yPos));
                else
                    break;
            }
        }
        // Vertical Up
        for (y = yPos - 1; y >= 0; y--)
        {
            if (chessBoard.getBoardPosition(xPos, y) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(xPos, y) == enemyType)
            {
                if (enemyType == TeamColor.Black && protect && y == yPos - 1 && chessBoard.getPiece(xPos, y) != null && chessBoard.getPiece(xPos, y) instanceof Pawn)
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(xPos, y));
                if (enemyType == TeamColor.Black && protect && y == yPos - 2 && chessBoard.getPiece(xPos, y) != null && chessBoard.getPiece(xPos, y) instanceof Pawn && chessBoard.getPiece(xPos, y).isFirstTime())
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(xPos, y));
                if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook))
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(xPos, y));
                else
                    break;
            }
        }
        // Vertical Down
        for (y = yPos + 1; y < ChessBoard.boardSize; y++)
        {
            if (chessBoard.getBoardPosition(xPos, y) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(xPos, y) == enemyType)
            {
                if (enemyType == TeamColor.White && protect && y == yPos + 1 && chessBoard.getPiece(xPos, y) != null && chessBoard.getPiece(xPos, y) instanceof Pawn)
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(xPos, y));
                if (enemyType == TeamColor.White && protect && y == yPos + 2 && chessBoard.getPiece(xPos, y) != null && chessBoard.getPiece(xPos, y) instanceof Pawn && chessBoard.getPiece(xPos, y).isFirstTime())
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(xPos, y));
                if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook))
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(xPos, y));
                else
                    break;
            }
        }
        // Diagonal 1 \ Up
        for (y = yPos - 1, x = xPos - 1; y >= 0 && x >= 0; y--, x--)
        {
            if (chessBoard.getBoardPosition(x, y) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(x, y) == enemyType)
            {
                if (!protect && y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn))
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(x, y));
                if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(x, y));
                else
                    break;
            }
        }
        // Diagonal 1 \ Down
        for (y = yPos + 1, x = xPos + 1; y < ChessBoard.boardSize && x < ChessBoard.boardSize; y++, x++)
        {
            if (chessBoard.getBoardPosition(x, y) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(x, y) == enemyType)
            {
                if (!protect && y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn))
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(x, y));
                if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(x, y));
                else
                    break;
            }
        }
        // Diagonal 2 / Up
        for (y = yPos - 1, x = xPos + 1; y >= 0 && x < ChessBoard.boardSize; y--, x++)
        {
            if (chessBoard.getBoardPosition(x, y) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(x, y) == enemyType)
            {
                if (!protect && y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn))
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(x, y));
                if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(x, y));
                else
                    break;
            }
        }
        // Diagonal 2 / Down
        for (y = yPos + 1, x = xPos - 1; y < ChessBoard.boardSize && x >= 0; y++, x--)
        {
            if (chessBoard.getBoardPosition(x, y) == teamColor)
                break;
            else if (chessBoard.getBoardPosition(x, y) == enemyType)
            {
                if (!protect && y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn))
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(x, y));
                if (chessBoard.getBoardPosition(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
                    chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(x, y));
                else
                    break;
            }
        }
        // Knight
        for (y = -2; y <= 2; y++)
        {
            if (y != 0)
            {
                x = y % 2 == 0 ? 1 : 2;
                if (yPos + y >= 0 && yPos + y < ChessBoard.boardSize && xPos - x >= 0 && xPos - x < ChessBoard.boardSize && chessBoard.getBoardPosition(xPos - x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos - x, yPos + y) != null)
                {
                    if (chessBoard.getPiece(xPos - x, yPos + y) != null && chessBoard.getPiece(xPos - x, yPos + y) instanceof Knight)
                        chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(xPos - x, yPos + y));
                }
                if (yPos + y >= 0 && yPos + y < ChessBoard.boardSize && xPos + x >= 0 && xPos + x < ChessBoard.boardSize && chessBoard.getBoardPosition(xPos + x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos + x, yPos + y) != null)
                {
                    if (chessBoard.getPiece(xPos + x, yPos + y) != null && chessBoard.getPiece(xPos + x, yPos + y) instanceof Knight)
                        chessBoard.getGameManagement().getSaviorPieces().add(chessBoard.getPiece(xPos + x, yPos + y));
                }
            }
        }
    }

    private void canCapture(ChessBoardGameInterface chessboard, PieceInterface checkPiece)
    {
        findAllSaviorPieces(chessboard, checkPiece.getPosition().getX(), checkPiece.getPosition().getY(), checkPiece.getTeamColor(), false);
    }

    private boolean isLimitPieceStalemate(ChessBoardGameInterface chessBoard) {
        if (piecesCount(chessBoard, Queen.class, TeamColor.White) != 0 || piecesCount(chessBoard, Queen.class, TeamColor.Black) != 0)
            return (false);
        else if (piecesCount(chessBoard, Rook.class, TeamColor.White) != 0 || piecesCount(chessBoard, Rook.class, TeamColor.Black) != 0)
            return (false);
        else if (piecesCount(chessBoard, Knight.class, TeamColor.White) > 1 || piecesCount(chessBoard, Knight.class, TeamColor.Black) > 1)
            return (false);
        else if (((piecesCount(chessBoard, Bishop.class, TeamColor.White) != 0) && piecesCount(chessBoard, Knight.class, TeamColor.White) != 0) ||
                ((piecesCount(chessBoard, Bishop.class, TeamColor.Black) != 0) && piecesCount(chessBoard, Knight.class, TeamColor.Black) != 0))
            return (false);
        else if ((piecesCount(chessBoard, Bishop.class, TeamColor.White) == 2) || (piecesCount(chessBoard, Bishop.class, TeamColor.Black) == 2))
            return (false);
        else return piecesCount(chessBoard, Pawn.class, TeamColor.White) <= 1 && piecesCount(chessBoard, Pawn.class, TeamColor.White) <= 1;
    }

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

    public static TeamColor getEnemyTeamColor(TeamColor teamColor) {
        if (teamColor == TeamColor.White) {
            return TeamColor.Black;
        }
        else {
            return TeamColor.White;
        }
    }

}
