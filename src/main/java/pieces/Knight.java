package pieces;

import view.ChessBoard;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
	
	public Knight(TeamColor teamColor, Position position) {
		super(teamColor, position);
	}

	@Override
	public List<Position> getPossibilities(ChessBoard chessBoard) {
		int x;
		List<Position> positions = new ArrayList<>();
		if (chessBoard.checkState && !this.isSavior()) {
			return positions;
		}
		if (gameLogic.verticalProtection(chessBoard, this.getPosition(), this.getTeamColor()) || gameLogic.horizontalProtection(chessBoard, this.getPosition(), this.getTeamColor()) || gameLogic.slashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor()) || gameLogic.backslashDiagonalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
			return positions;
		}
		for (int y = -2; y <= 2; y++) {
			if (y != 0) {
				x = y % 2 == 0 ? 1 : 2;
				if (this.getPosition().getY() + y >= 0 && this.getPosition().getY() + y < chessBoard.getBoardHeight() && this.getPosition().getX() - x >= 0 && this.getPosition().getX() - x < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(this.getPosition().getX() - x, this.getPosition().getY() + y) != this.getTeamColor()) {
					if (chessBoard.checkState) {
						if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX() - x, this.getPosition().getY() + y, this.getTeamColor())) {
							positions.add(new Position(this.getPosition().getX() - x, this.getPosition().getY() + y));
						}
					}
					else {
						positions.add(new Position(this.getPosition().getX() - x, this.getPosition().getY() + y));
					}
				}
				if (this.getPosition().getY() + y >= 0 && this.getPosition().getY() + y < chessBoard.getBoardHeight() && this.getPosition().getX() + x >= 0 && this.getPosition().getX() + x < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(this.getPosition().getX() + x, this.getPosition().getY() + y) != this.getTeamColor()) {
					if (chessBoard.checkState) {
						if (gameLogic.isThisProtecting(chessBoard, this.getPosition().getX() + x, this.getPosition().getY() + y, this.getTeamColor())) {
							positions.add(new Position(this.getPosition().getX() + x, this.getPosition().getY() + y));
						}
					}
					else {
						positions.add(new Position(this.getPosition().getX() + x, this.getPosition().getY() + y));
					}
				}
			}
		}
		return positions;
	}	
}
