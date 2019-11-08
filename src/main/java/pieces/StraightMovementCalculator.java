package pieces;

import game.GameLogic;
import view.ChessBoard;

import java.util.ArrayList;
import java.util.List;

class StraightMovementCalculator {

    List<Position> getPossibilities1(ChessBoard chessBoard, Piece piece, GameLogic gameLogic, int y) {
        List<Position> positions = new ArrayList<>();
        if (chessBoard.getBoardPosition(piece.getPosition().getX(), y) == null) {
            if (chessBoard.checkState) {
                if (gameLogic.isThisProtecting(chessBoard, piece.getPosition().getX(), y, piece.getTeamColor())) {
                    positions.add(new Position(piece.getPosition().getX(), y));
                }
            }
            else {
                positions.add(new Position(piece.getPosition().getX(), y));
            }
        }
        else if (chessBoard.getBoardPosition(piece.getPosition().getX(), y) == piece.getTeamColor()) {
            return positions;
        }
        else {
            if (chessBoard.checkState) {
                if (gameLogic.isThisProtecting(chessBoard, piece.getPosition().getX(), y, piece.getTeamColor())) {
                    positions.add(new Position(piece.getPosition().getX(), y));
                }
            }
            else {
                positions.add(new Position(piece.getPosition().getX(), y));
            }
            return positions;
        }
        return positions;
    }
    
    List<Position> getPossibilities2(ChessBoard chessBoard, Piece piece, GameLogic gameLogic) {
        List<Position> positions = new ArrayList<>();
        for (int x = piece.getPosition().getX() - 1; x >= 0; x--) {
            if (chessBoard.getBoardPosition(x, piece.getPosition().getY()) == null) {
                if (chessBoard.checkState) {
                    if (gameLogic.isThisProtecting(chessBoard, x, piece.getPosition().getY(), piece.getTeamColor())) {
                        positions.add(new Position(x, piece.getPosition().getY()));
                    }
                }
                else {
                    positions.add(new Position(x, piece.getPosition().getY()));
                }
            }
            else if (chessBoard.getBoardPosition(x, piece.getPosition().getY()) == piece.getTeamColor()) {
                break;
            }
            else {
                if (chessBoard.checkState) {
                    if (gameLogic.isThisProtecting(chessBoard, x, piece.getPosition().getY(), piece.getTeamColor())) {
                        positions.add(new Position(x, piece.getPosition().getY()));
                    }
                }
                else {
                    positions.add(new Position(x, piece.getPosition().getY()));
                }
                break;
            }
        }
        for (int x = piece.getPosition().getX() + 1; x < chessBoard.getBoardWidth(); x++) {
            if (chessBoard.getBoardPosition(x, piece.getPosition().getY()) == null) {
                if (chessBoard.checkState) {
                    if (gameLogic.isThisProtecting(chessBoard, x, piece.getPosition().getY(), piece.getTeamColor())) {
                        positions.add(new Position(x, piece.getPosition().getY()));
                    }
                }
                else {
                    positions.add(new Position(x, piece.getPosition().getY()));
                }
            }
            else if (chessBoard.getBoardPosition(x, piece.getPosition().getY()) == piece.getTeamColor()) {
                break;
            }
            else {
                if (chessBoard.checkState) {
                    if (gameLogic.isThisProtecting(chessBoard, x, piece.getPosition().getY(), piece.getTeamColor())) {
                        positions.add(new Position(x, piece.getPosition().getY()));
                    }
                }
                else {
                    positions.add(new Position(x, piece.getPosition().getY()));
                }
                break;
            }
        }
        return positions;
    }
}
