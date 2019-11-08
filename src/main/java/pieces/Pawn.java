package pieces;

import view.ChessBoard;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
	
	public Pawn(TeamColor teamColor, Position position) {
		super(teamColor, position);
	}

	@Override
	public List<Position> getPossibilities(ChessBoard chessBoard) {
		List<Position> positions = new ArrayList<>();
		if (chessBoard.checkState && !this.isSavior()) {
			return positions;
		}
		if (gameLogic.horizontalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
			return positions;
		}
		if (this.getTeamColor() == TeamColor.White) {
			if (!gameLogic.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor()) && !gameLogic.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
				if (this.getPosition().getY() - 1 >= 0 && chessBoard.getBoardPosition(this.getPosition().getX(), this.getPosition().getY() - 1) == null) {
					if (chessBoard.checkState) {
						if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX(), this.getPosition().getY() - 1, this.getTeamColor())) {
							positions.add(new Position(this.getPosition().getX(), this.getPosition().getY() - 1));
						}
					}
					else {
						positions.add(new Position(this.getPosition().getX(), this.getPosition().getY() - 1));
					}
				}
				if (this.isFirstTime() && chessBoard.getBoardPosition(this.getPosition().getX(), this.getPosition().getY() - 2) == null) {
					if (chessBoard.checkState) {
						if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX(), this.getPosition().getY() - 2, this.getTeamColor())) {
							positions.add(new Position(this.getPosition().getX(), this.getPosition().getY() - 2));
						}
					}
					else {
						positions.add(new Position(this.getPosition().getX(), this.getPosition().getY() - 2));
					}
				}
			}
			if (!gameLogic.verticalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
				if (!gameLogic.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
					if (this.getPosition().getY() - 1 >= 0 && this.getPosition().getX() - 1 >= 0 && chessBoard.getBoardPosition(this.getPosition().getX() - 1, this.getPosition().getY() - 1) != this.getTeamColor() && chessBoard.getBoardPosition(this.getPosition().getX() - 1, this.getPosition().getY() - 1) != null) {
						if (chessBoard.checkState) {
							if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX() - 1, this.getPosition().getY() - 1, this.getTeamColor())) {
								positions.add(new Position(this.getPosition().getX() - 1, this.getPosition().getY() - 1));
							}
						}
						else {
							positions.add(new Position(this.getPosition().getX() - 1, this.getPosition().getY() - 1));
						}
					}
				}
				if (!gameLogic.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
					if (this.getPosition().getY() - 1 >= 0 && this.getPosition().getX() + 1 < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(this.getPosition().getX() + 1, this.getPosition().getY() - 1) != this.getTeamColor() && chessBoard.getBoardPosition(this.getPosition().getX() + 1, this.getPosition().getY() - 1) != null) {
						if (chessBoard.checkState) {
							if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX() + 1, this.getPosition().getY() - 1, this.getTeamColor())) {
								positions.add(new Position(this.getPosition().getX() + 1, this.getPosition().getY() - 1));
							}
						}
						else {
							positions.add(new Position(this.getPosition().getX() + 1, this.getPosition().getY() - 1));
						}
					}
				}
			}
		}
		else if (this.getTeamColor() == TeamColor.Black) {
			if (!gameLogic.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor()) && !gameLogic.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
				if (this.getPosition().getY() + 1 < chessBoard.getBoardHeight() && chessBoard.getBoardPosition(this.getPosition().getX(), this.getPosition().getY() + 1) == null) {
					if (chessBoard.checkState) {
						if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX(), this.getPosition().getY() + 1, this.getTeamColor())) {
							positions.add(new Position(this.getPosition().getX(), this.getPosition().getY() + 1));
						}
					}
					else {
						positions.add(new Position(this.getPosition().getX(), this.getPosition().getY() + 1));
					}
				}
				if (this.isFirstTime() && chessBoard.getBoardPosition(this.getPosition().getX(), this.getPosition().getY() + 2) == null) {
					if (chessBoard.checkState) {
						if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX(), this.getPosition().getY() + 2, this.getTeamColor())) {
							positions.add(new Position(this.getPosition().getX(), this.getPosition().getY() + 2));
						}
					}
					else {
						positions.add(new Position(this.getPosition().getX(), this.getPosition().getY() + 2));
					}
				}
			}
			if (!gameLogic.verticalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
				if (!gameLogic.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
					if (this.getPosition().getY() + 1 < chessBoard.getBoardHeight() && this.getPosition().getX() - 1 >= 0 && chessBoard.getBoardPosition(this.getPosition().getX() - 1, this.getPosition().getY() + 1) != this.getTeamColor() && chessBoard.getBoardPosition(this.getPosition().getX() - 1, this.getPosition().getY() + 1) != null) {
						if (chessBoard.checkState) {
							if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX() - 1, this.getPosition().getY() + 1, this.getTeamColor())) {
								positions.add(new Position(this.getPosition().getX() - 1, this.getPosition().getY() + 1));
							}
						}
						else {
							positions.add(new Position(this.getPosition().getX() - 1, this.getPosition().getY() + 1));
						}
					}
				}
				if (!gameLogic.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
					if (this.getPosition().getY() + 1 < chessBoard.getBoardHeight() && this.getPosition().getX() + 1 < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(this.getPosition().getX() + 1, this.getPosition().getY() + 1) != this.getTeamColor() && chessBoard.getBoardPosition(this.getPosition().getX() + 1, this.getPosition().getY() + 1) != null) {
						if (chessBoard.checkState) {
							if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX() + 1, this.getPosition().getY() + 1, this.getTeamColor())) {
								positions.add(new Position(this.getPosition().getX() + 1, this.getPosition().getY() + 1));
							}
						}
						else {
							positions.add(new Position(this.getPosition().getX() + 1, this.getPosition().getY() + 1));
						}
					}
				}
			}
		}
		return positions;
	}
}
