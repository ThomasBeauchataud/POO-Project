package pieces;

import common.Position;
import game.ChessBoardGameInterface;
import game.ChessBoardPieceInterface;
import game.MovementRules;
import game.ChessBoard;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class King extends Piece {
	
	public King(TeamColor teamColor, Position position) {
		super(teamColor, position);
	}

	@Override
	public List<Position> getPossibilities(ChessBoardGameInterface chessBoard) {
		int x;
		int y;
		List<Position> positions = new ArrayList<>();
		for (y = this.getPosition().getY() - 1; y <= this.getPosition().getY() + 1; y++) {
			for (x = this.getPosition().getX() - 1; x <= this.getPosition().getX() + 1; x++) {
				if(y >= 0 && y < ChessBoard.boardSize && x >= 0 && x < ChessBoard.boardSize && chessBoard.getBoardPosition(x, y) != this.getTeamColor()) {
					if (!chessBoard.getGameManagement().isCheckState()) {
						this.canCastle(chessBoard);
					}
					if (!MovementRules.isCheck(chessBoard, x, y, this.getTeamColor(), true)) {
						positions.add(new Position(x, y));
					}
				}
			}
		}
		return positions;
	}

	public Castle canCastle(ChessBoardPieceInterface chessBoard){
		Castle canCastle = null;
		if(this.getTeamColor() == TeamColor.Black && this.isFirstTime() && chessBoard.getBoardPosition(5, 0) == null && chessBoard.getBoardPosition(6, 0) == null && chessBoard.getPiece(7, 0) != null && chessBoard.getPiece(7, 0).isFirstTime()){
			canCastle = Castle.ShortBlack;
			chessBoard.colorSquare(7, 0, false);
		}
		if(this.getTeamColor() == TeamColor.Black && this.isFirstTime() && chessBoard.getBoardPosition(1, 0) == null && chessBoard.getBoardPosition(2, 0) == null && chessBoard.getBoardPosition(3, 0) == null && chessBoard.getPiece(0, 0) != null && chessBoard.getPiece(0, 0).isFirstTime()){
			canCastle = Castle.LongBlack;
			chessBoard.colorSquare(0, 0, false);
		}
		if(this.getTeamColor() == TeamColor.White && this.isFirstTime() && chessBoard.getBoardPosition(5, 7) == null && chessBoard.getBoardPosition(6, 7) == null && chessBoard.getPiece(7, 7) != null && chessBoard.getPiece(7, 7).isFirstTime()){
			canCastle = Castle.ShortWhite;
			chessBoard.colorSquare(7, 7, false);
		}
		if(this.getTeamColor() == TeamColor.White && this.isFirstTime() && chessBoard.getBoardPosition(1, 7) == null && chessBoard.getBoardPosition(2, 7) == null && chessBoard.getBoardPosition(3, 7) == null && chessBoard.getPiece(0, 7) != null && chessBoard.getPiece(0, 7).isFirstTime()){
			canCastle = Castle.LongWhite;
			chessBoard.colorSquare(0, 7, false);
		}
		return canCastle; 
	}
}
