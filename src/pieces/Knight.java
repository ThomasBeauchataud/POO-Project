package pieces;

import common.Position;
import game.ChessBoard;
import game.ChessBoardGameInterface;
import game.MovementRules;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
	
	public Knight(TeamColor teamColor, Position position) {
		super(teamColor, position);
	}

	@Override
	public List<Position> getPossibilities(ChessBoardGameInterface chessBoard) {
		int x;
		List<Position> positions = new ArrayList<>();
		if (MovementRules.verticalProtection(chessBoard, this.getPosition(), this.getTeamColor()) || MovementRules.horizontalProtection(chessBoard, this.getPosition(), this.getTeamColor()) || MovementRules.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor()) || MovementRules.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
			return positions;
		}
		for (int y = -2; y <= 2; y++) {
			if (y != 0) {
				x = y % 2 == 0 ? 1 : 2;
				if (this.getPosition().getY() + y >= 0 && this.getPosition().getY() + y < ChessBoard.boardSize && this.getPosition().getX() - x >= 0 && this.getPosition().getX() - x < ChessBoard.boardSize && chessBoard.getBoardPosition(this.getPosition().getX() - x, this.getPosition().getY() + y) != this.getTeamColor()) {
					positions.add(new Position(this.getPosition().getX() - x, this.getPosition().getY() + y));
				}
				if (this.getPosition().getY() + y >= 0 && this.getPosition().getY() + y < ChessBoard.boardSize && this.getPosition().getX() + x >= 0 && this.getPosition().getX() + x < ChessBoard.boardSize && chessBoard.getBoardPosition(this.getPosition().getX() + x, this.getPosition().getY() + y) != this.getTeamColor()) {
					positions.add(new Position(this.getPosition().getX() + x, this.getPosition().getY() + y));
				}
			}
		}
		return positions;
	}	
}
