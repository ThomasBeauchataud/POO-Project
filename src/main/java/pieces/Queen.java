package pieces;

import view.ChessBoard;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

	private final DiagonalMovementCalculator diagonalMovementCalculator = new DiagonalMovementCalculator();
	private final StraightMovementCalculator straightMovementCalculator = new StraightMovementCalculator();
	
	public Queen(TeamColor teamColor, Position position) {
		super(teamColor, position);
	}
	
	@Override
	public List<Position> getPossibilities(ChessBoard chessBoard) {
		List<Position> positions = new ArrayList<>();
		int y = this.getPosition().getY() + 1;
		if (chessBoard.checkState && !this.isSavior()) {
			return positions;
		}
		if (!gameLogic.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor()) && !gameLogic.verticalProtection(chessBoard, this.getPosition(), this.getTeamColor()) && !gameLogic.horizontalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
			positions.addAll(diagonalMovementCalculator.getPossibilities1(chessBoard, this, gameLogic, y));
		}
		if (!gameLogic.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor()) && !gameLogic.verticalProtection(chessBoard, this.getPosition(), this.getTeamColor()) && !gameLogic.horizontalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
			positions.addAll(diagonalMovementCalculator.getPossibilities2(chessBoard, this, gameLogic));
		}
		if (!gameLogic.horizontalProtection(chessBoard, this.getPosition(), this.getTeamColor()) && !gameLogic.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor()) && !gameLogic.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
			for (y = this.getPosition().getY() - 1; y >= 0; y--) {
				if (chessBoard.getBoardPosition(this.getPosition().getX(), y) == null) {
					if (chessBoard.checkState) {
						if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX(), y, this.getTeamColor())) {
							positions.add(new Position(this.getPosition().getX(), y));
						}
					}
					else {
						positions.add(new Position(this.getPosition().getX(), y));
					}
				}
				else if (chessBoard.getBoardPosition(this.getPosition().getX(), y) == this.getTeamColor()) {
					break;
				}
				else {
					if (chessBoard.checkState) {
						if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX(), y, this.getTeamColor())) {
							positions.add(new Position(this.getPosition().getX(), y));
						}
					}
					else {
						positions.add(new Position(this.getPosition().getX(), y));
					}
					break;
				}
			}
			for (y = this.getPosition().getY() + 1; y < chessBoard.getBoardHeight(); y++) {
				if (chessBoard.getBoardPosition(this.getPosition().getX(), y) == null) {
					if (chessBoard.checkState) {
						if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX(), y, this.getTeamColor())) {
							positions.add(new Position(this.getPosition().getX(), y));
						}
					}
					else {
						positions.add(new Position(this.getPosition().getX(), y));
					}
				}
				else if (chessBoard.getBoardPosition(this.getPosition().getX(), y) == this.getTeamColor()) {
					break;
				}
				else {
					if (chessBoard.checkState) {
						if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX(), y, this.getTeamColor())) {
							positions.add(new Position(this.getPosition().getX(), y));
						}
					}
					else {
						positions.add(new Position(this.getPosition().getX(), y));
					}
					break;
				}
			}
		}
		if (!gameLogic.verticalProtection(chessBoard, this.getPosition(), this.getTeamColor()) && !gameLogic.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor()) && !gameLogic.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
			positions.addAll(straightMovementCalculator.getPossibilities2(chessBoard, this, gameLogic));
		}
		return positions;
	}
}
