package pieces;

import view.ChessBoard;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

	private final DiagonalMovementCalculator diagonalMovementCalculator = new DiagonalMovementCalculator();

	public Bishop(TeamColor teamColor, Position position) {
		super(teamColor, position);
	}
	
	@Override
	public List<Position> getPossibilities(ChessBoard chessBoard) {
		int y = this.getPosition().getY() + 1;
		List<Position> positions = new ArrayList<>();
		if (chessBoard.checkState && !this.isSavior()) {
			return positions;
		}
		if (gameLogic.horizontalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()) || gameLogic.verticalProtection(chessBoard, this.getPosition(), this.getTeamColor())) {
			return positions;
		}
		if (!gameLogic.slashDiagonalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor())) {
			positions.addAll(diagonalMovementCalculator.getPossibilities1(chessBoard, this, gameLogic, y));
		}
		if (!gameLogic.backslashDiagonalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor())) {
			positions.addAll(diagonalMovementCalculator.getPossibilities2(chessBoard, this, gameLogic));
		}
		return positions;
	}
}
