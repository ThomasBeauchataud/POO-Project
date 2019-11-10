package pieces;

import common.Position;
import game.ChessBoardGameInterface;
import game.MovementRules;
import game.ChessBoard;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
	
	public Pawn(TeamColor teamColor, Position position) {
		super(teamColor, position);
	}

	@Override
	public List<Position> getPossibilities(ChessBoardGameInterface chessBoard) {
		List<Position> positions = new ArrayList<>();
		if (MovementRules.horizontalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
			return positions;
		}
		if (this.getTeamColor() == TeamColor.White) {
			if (!MovementRules.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor()) && !MovementRules.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
				if (this.getPosition().getY() - 1 >= 0 && chessBoard.getBoardPosition(this.getPosition().getX(), this.getPosition().getY() - 1) == null) {
					positions.add(new Position(this.getPosition().getX(), this.getPosition().getY() - 1));
				}
				if (this.isFirstTime() && chessBoard.getBoardPosition(this.getPosition().getX(), this.getPosition().getY() - 2) == null) {
					positions.add(new Position(this.getPosition().getX(), this.getPosition().getY() - 2));
				}
			}
			if (!MovementRules.verticalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
				if (!MovementRules.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
					if (this.getPosition().getY() - 1 >= 0 && this.getPosition().getX() - 1 >= 0 && chessBoard.getBoardPosition(this.getPosition().getX() - 1, this.getPosition().getY() - 1) != this.getTeamColor() && chessBoard.getBoardPosition(this.getPosition().getX() - 1, this.getPosition().getY() - 1) != null) {
						positions.add(new Position(this.getPosition().getX() - 1, this.getPosition().getY() - 1));
					}
				}
				if (!MovementRules.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
					if (this.getPosition().getY() - 1 >= 0 && this.getPosition().getX() + 1 < ChessBoard.boardSize && chessBoard.getBoardPosition(this.getPosition().getX() + 1, this.getPosition().getY() - 1) != this.getTeamColor() && chessBoard.getBoardPosition(this.getPosition().getX() + 1, this.getPosition().getY() - 1) != null) {
						positions.add(new Position(this.getPosition().getX() + 1, this.getPosition().getY() - 1));
					}
				}
			}
		}
		else if (this.getTeamColor() == TeamColor.Black) {
			if (!MovementRules.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor()) && !MovementRules.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
				if (this.getPosition().getY() + 1 < ChessBoard.boardSize && chessBoard.getBoardPosition(this.getPosition().getX(), this.getPosition().getY() + 1) == null) {
					positions.add(new Position(this.getPosition().getX(), this.getPosition().getY() + 1));
				}
				if (this.isFirstTime() && chessBoard.getBoardPosition(this.getPosition().getX(), this.getPosition().getY() + 2) == null) {
					positions.add(new Position(this.getPosition().getX(), this.getPosition().getY() + 2));
				}
			}
			if (!MovementRules.verticalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
				if (!MovementRules.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
					if (this.getPosition().getY() + 1 < ChessBoard.boardSize && this.getPosition().getX() - 1 >= 0 && chessBoard.getBoardPosition(this.getPosition().getX() - 1, this.getPosition().getY() + 1) != this.getTeamColor() && chessBoard.getBoardPosition(this.getPosition().getX() - 1, this.getPosition().getY() + 1) != null) {
						positions.add(new Position(this.getPosition().getX() - 1, this.getPosition().getY() + 1));
					}
				}
				if (!MovementRules.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
					if (this.getPosition().getY() + 1 < ChessBoard.boardSize && this.getPosition().getX() + 1 < ChessBoard.boardSize && chessBoard.getBoardPosition(this.getPosition().getX() + 1, this.getPosition().getY() + 1) != this.getTeamColor() && chessBoard.getBoardPosition(this.getPosition().getX() + 1, this.getPosition().getY() + 1) != null) {
						positions.add(new Position(this.getPosition().getX() + 1, this.getPosition().getY() + 1));
					}
				}
			}
		}
		return positions;
	}

}
