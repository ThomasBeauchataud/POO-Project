package pieces;

import common.Position;
import game.ChessBoardGameInterface;
import game.MovementRules;
import game.ChessBoard;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class Bishop extends Piece {

	public Bishop(TeamColor teamColor, Position position) {
		super(teamColor, position);
	}
	
	@Override
	public List<Position> getPossibilities(ChessBoardGameInterface chessBoard) {
		int y = this.getPosition().getY() + 1;
		List<Position> positions = new ArrayList<>();
		if (MovementRules.horizontalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()) || MovementRules.verticalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
			return positions;
		}
		if (!MovementRules.slashDiagonalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor())) {
			for(int x = this.getPosition().getX() + 1; x < ChessBoard.boardSize && y < ChessBoard.boardSize; x++, y++) {
				if (chessBoard.getBoardPosition(x, y) == null) {
					if (chessBoard.getGameManagement().isCheckState()) {
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor())) {
							positions.add(new Position(x, y));
						}
					}
					else {
						positions.add(new Position(x, y));
					}
				}
				else if (chessBoard.getBoardPosition(x, y) == this.getTeamColor()) {
					break;
				}
				else {
					if (chessBoard.getGameManagement().isCheckState()) {
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor())) {
							positions.add(new Position(x, y));
						}
					}
					else {
						positions.add(new Position(x, y));
					}
					break;
				}
			}
			y = this.getPosition().getY() - 1;
			for(int x = this.getPosition().getX() - 1; x >= 0 && y >= 0; x--, y--) {
				if (chessBoard.getBoardPosition(x, y) == null) {
					if (chessBoard.getGameManagement().isCheckState()) {
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor())) {
							positions.add(new Position(x, y));
						}
					}
					else {
						positions.add(new Position(x, y));
					}
				}
				else if (chessBoard.getBoardPosition(x, y) == this.getTeamColor()) {
					break;
				}
				else {
					if (chessBoard.getGameManagement().isCheckState()) {
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor())) {
							positions.add(new Position(x, y));
						}
					}
					else {
						positions.add(new Position(x, y));
					}
					break;
				}
			}
		}
		if (!MovementRules.backslashDiagonalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor())) {
			y = this.getPosition().getY() + 1;
			for (int x = this.getPosition().getX() - 1; x >= 0 && y < ChessBoard.boardSize; x--, y++) {
				if (chessBoard.getBoardPosition(x, y) == null) {
					if (chessBoard.getGameManagement().isCheckState()) {
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor())) {
							positions.add(new Position(x, y));
						}
					}
					else {
						positions.add(new Position(x, y));
					}
				}
				else if (chessBoard.getBoardPosition(x, y) == this.getTeamColor()) {
					break;
				}
				else {
					if (chessBoard.getGameManagement().isCheckState()) {
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor())) {
							positions.add(new Position(x, y));
						}
					}
					else {
						positions.add(new Position(x, y));
					}
					break;
				}
			}
			y = this.getPosition().getY() - 1;
			for (int x = this.getPosition().getX() + 1; x < ChessBoard.boardSize && y >= 0; x++, y--) {
				if (chessBoard.getBoardPosition(x, y) == null) {
					if (chessBoard.getGameManagement().isCheckState()) {
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor())) {
							positions.add(new Position(x, y));
						}
					}
					else {
						positions.add(new Position(x, y));
					}
				}
				else if (chessBoard.getBoardPosition(x, y) == this.getTeamColor()) {
					break;
				}
				else {
					if (chessBoard.getGameManagement().isCheckState()) {
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor())) {
							positions.add(new Position(x, y));
						}
					}
					else {
						positions.add(new Position(x, y));
					}
					break;
				}
			}
		}
		return positions;
	}
}
