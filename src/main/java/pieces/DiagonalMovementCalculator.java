package pieces;

import game.GameLogic;
import view.ChessBoard;

import java.util.ArrayList;
import java.util.List;

class DiagonalMovementCalculator {

    List<Position> getPossibilities1(ChessBoard chessBoard, Piece piece, GameLogic gameLogic, int y) {
        List<Position> positions = new ArrayList<>();
        for(int x = piece.getPosition().getX() + 1; x < chessBoard.getBoardWidth() && y < chessBoard.getBoardHeight(); x++, y++) {
            if (chessBoard.getBoardPosition(x, y) == null) {
                if (chessBoard.checkState) {
                    if (gameLogic.isThisProtecting(chessBoard, x, y, piece.getTeamColor()))
                        chessBoard.colorSquare(x, y, false);
                }
                else
                    chessBoard.colorSquare(x, y, false);
            }
            else if (chessBoard.getBoardPosition(x, y) == piece.getTeamColor())
                break;
            else {
                if (chessBoard.checkState) {
                    if (gameLogic.isThisProtecting(chessBoard, x, y, piece.getTeamColor()))
                        chessBoard.colorSquare(x, y, false);
                }
                else
                    chessBoard.colorSquare(x, y, false);
                break;
            }
        }
        y = piece.getPosition().getY() - 1;
        for(int x = piece.getPosition().getX() - 1; x >= 0 && y >= 0; x--, y--)
        {
            if (chessBoard.getBoardPosition(x, y) == null)
            {
                if (chessBoard.checkState)
                {
                    if (gameLogic.isThisProtecting(chessBoard, x, y, piece.getTeamColor()))
                        chessBoard.colorSquare(x, y, false);
                }
                else
                    chessBoard.colorSquare(x, y, false);
            }
            else if (chessBoard.getBoardPosition(x, y) == piece.getTeamColor())
                break;
            else
            {
                if (chessBoard.checkState)
                {
                    if (gameLogic.isThisProtecting(chessBoard, x, y, piece.getTeamColor()))
                        chessBoard.colorSquare(x, y, false);
                }
                else
                    chessBoard.colorSquare(x, y, false);
                break;
            }
        }
        return positions;
    }

    List<Position> getPossibilities2(ChessBoard chessBoard, Piece piece, GameLogic gameLogic) {
        List<Position> positions = new ArrayList<>();
        int y = piece.getPosition().getY() + 1;
        for (int x = piece.getPosition().getX() - 1; x >= 0 && y < chessBoard.getBoardHeight(); x--, y++)
        {
            if (chessBoard.getBoardPosition(x, y) == null)
            {
                if (chessBoard.checkState)
                {
                    if (gameLogic.isThisProtecting(chessBoard, x, y, piece.getTeamColor()))
                        chessBoard.colorSquare(x, y, false);
                }
                else
                    chessBoard.colorSquare(x, y, false);
            }
            else if (chessBoard.getBoardPosition(x, y) == piece.getTeamColor())
                break;
            else
            {
                if (chessBoard.checkState)
                {
                    if (gameLogic.isThisProtecting(chessBoard, x, y, piece.getTeamColor()))
                        chessBoard.colorSquare(x, y, false);
                }
                else
                    chessBoard.colorSquare(x, y, false);
                break;
            }
        }
        y = piece.getPosition().getY() - 1;
        for (int x = piece.getPosition().getX() + 1; x < chessBoard.getBoardWidth() && y >= 0; x++, y--)
        {
            if (chessBoard.getBoardPosition(x, y) == null)
            {
                if (chessBoard.checkState)
                {
                    if (gameLogic.isThisProtecting(chessBoard, x, y, piece.getTeamColor()))
                        chessBoard.colorSquare(x, y, false);
                }
                else
                    chessBoard.colorSquare(x, y, false);
            }
            else if (chessBoard.getBoardPosition(x, y) == piece.getTeamColor())
                break;
            else
            {
                if (chessBoard.checkState)
                {
                    if (gameLogic.isThisProtecting(chessBoard, x, y, piece.getTeamColor()))
                        chessBoard.colorSquare(x, y, false);
                }
                else
                    chessBoard.colorSquare(x, y, false);
                break;
            }
        }
        return positions;
    }

}
