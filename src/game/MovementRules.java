package game;

import common.Position;
import pieces.*;

//TODO Optimize all this methods
public class MovementRules {

	/**
	 * Method to check if a piece is protecting the king from a check in vertical alignment
	 * @param chessBoard ChessBoard
	 * @param position Position
	 * @param teamColor TeamColor
	 * @return boolean
	 */
	public static boolean verticalProtection(ChessBoardGameInterface chessBoard, Position position, TeamColor teamColor) {
		return verticalProtection(chessBoard, position.getY(), position.getY(), teamColor);
	}

	/**
	 * Method to check if a piece is protecting the king from a check in vertical alignment
	 * @param chessBoard ChessBoard
	 * @param xPos int
	 * @param yPos int
	 * @param teamColor TeamColor
	 * @return boolean
	 */
	public static boolean verticalProtection(ChessBoardGameInterface chessBoard, int xPos, int yPos, TeamColor teamColor) {
		int y = 0;
		TeamColor enemyType;
		if (teamColor == TeamColor.White)
			enemyType = TeamColor.Black;
		else
			enemyType = TeamColor.White;

		// King on the Vertical Up
		for (y = yPos - 1; y >= 0; y--) {
			if (chessBoard.getBoardPosition(xPos, y) == teamColor && chessBoard.getPiece(xPos, y) instanceof King) {
				for (y = yPos + 1; y < ChessBoard.boardSize; y++) {
					if (chessBoard.getBoardPosition(xPos, y) == teamColor)
						break;
					else if (chessBoard.getBoardPosition(xPos, y) == enemyType) {
						if (chessBoard.getPiece(xPos,  y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook)
							return (true);
						else
							break;
					}
				}
				break;
			}
			else if (chessBoard.getBoardPosition(xPos, y) != null)
				break;
		}		
		// King on the Vertical Down
		for (y = yPos + 1; y < ChessBoard.boardSize; y++) {
			if (chessBoard.getBoardPosition(xPos, y) == teamColor && chessBoard.getPiece(xPos, y) instanceof King) {
				for (y = yPos - 1; y >= 0; y--) {
					if (chessBoard.getBoardPosition(xPos, y) == teamColor)
						break;
					else if (chessBoard.getBoardPosition(xPos,  y) == enemyType) {
						if (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook)
							return (true);
						else
							break;
					}
				}
				break;				
			}
			else if (chessBoard.getBoardPosition(xPos, y) != null)
				break;
		}
		return (false);
	}

	/**
	 * Method to check if a piece is protecting the king from a check in horizontal alignment
	 * @param chessBoard ChessBoard
	 * @param position Position
	 * @param teamColor TeamColor
	 * @return boolean
	 */
	public static boolean horizontalProtection(ChessBoardGameInterface chessBoard, Position position, TeamColor teamColor) {
		return horizontalProtection(chessBoard, position.getX(), position.getY(), teamColor);
	}

	/**
	 * Method to check if a piece is protecting the king from a check in horizontal alignment
	 * @param chessBoard ChessBoard
	 * @param xPos int
	 * @param yPos int
	 * @param teamColor TeamColor
	 * @return boolean
	 */
	public static boolean horizontalProtection(ChessBoardGameInterface chessBoard, int xPos, int yPos, TeamColor teamColor) {
		int x = 0;
		TeamColor enemyType = getEnemyTeamColor(teamColor);
		// King on the Horizontal Left
		for (x = xPos - 1; x >= 0; x--) {
			if (chessBoard.getBoardPosition(x, yPos) == teamColor && chessBoard.getPiece(x, yPos) instanceof King) {
				for (x = xPos + 1; x < ChessBoard.boardSize; x++) {
					if (chessBoard.getBoardPosition(x, yPos) == teamColor)
						break;
					else if (chessBoard.getBoardPosition(x, yPos) == enemyType) {
						if (chessBoard.getPiece(x,  yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook)
							return (true);
						else
							break;
					}
				}
				break;
			}
			else if (chessBoard.getBoardPosition(x, yPos) != null)
				break;
		}
		// King on the Horizontal Right
		for (x = xPos + 1; x < ChessBoard.boardSize; x++) {
			if (chessBoard.getBoardPosition(x, yPos) == teamColor && chessBoard.getPiece(x, yPos) instanceof King) {
				for (x = xPos - 1; x >= 0; x--) {
					if (chessBoard.getBoardPosition(x, yPos) == teamColor)
						break;
					else if (chessBoard.getBoardPosition(x,  yPos) == enemyType) {
						if (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook)
							return (true);
						else
							break;
					}
				}
				break;				
			}
			else if (chessBoard.getBoardPosition(x, yPos) != null)
				break;
		}
		return (false);
	}

	/**
	 * Method to check if a piece is protecting the king from a check in diagonal alignment
	 * @param chessBoard ChessBoard
	 * @param position Position
	 * @param teamColor TeamColor
	 * @return boolean
	 */
	public static boolean slashDiagonalProtection(ChessBoardGameInterface chessBoard, Position position, TeamColor teamColor) {
		return slashDiagonalProtection(chessBoard, position.getX(), position.getY(), teamColor);
	}

	/**
	 * Method to check if a piece is protecting the king from a check in diagonal alignment
	 * @param chessBoard ChessBoard
	 * @param xPos int
	 * @param yPos int
	 * @param teamColor TeamColor
	 * @return boolean
	 */
	public static boolean slashDiagonalProtection(ChessBoardGameInterface chessBoard, int xPos, int yPos, TeamColor teamColor) {
		TeamColor enemyType = getEnemyTeamColor(teamColor);

		// King on the Diagonal / Up
		int y = yPos - 1;
		for (int x = xPos + 1; x < ChessBoard.boardSize && y >= 0; x++, y--) {
			if (chessBoard.getBoardPosition(x, y) == teamColor && chessBoard.getPiece(x, y) instanceof King) {
				y = yPos + 1;
				for (x = xPos - 1; x >= 0 && y < ChessBoard.boardSize; x--, y++) {
					if (chessBoard.getBoardPosition(x, y) == teamColor)
						break;
					else if (chessBoard.getBoardPosition(x, y) == enemyType) {
						if (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop)
							return (true);
						else
							break;
					}
				}
				break;
			}
			else if (chessBoard.getBoardPosition(x, y) != null)
				break;
		}
		// King on the Diagonal / Down
		y = yPos + 1;
		for (int x = xPos - 1; x >= 0 && y < ChessBoard.boardSize; x--, y++) {
			if (chessBoard.getBoardPosition(x, y) == teamColor && chessBoard.getPiece(x, y) instanceof King)
			{
				y = yPos - 1;
				for (x = xPos + 1; x < ChessBoard.boardSize && y >= 0; x++, y--) {
					if (chessBoard.getBoardPosition(x, y) == teamColor)
						break;
					else if (chessBoard.getBoardPosition(x, y) == enemyType) {
						if (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop)
							return (true);
						else
							break;
					}
				}
				break;
			}
			else if (chessBoard.getBoardPosition(x, y) != null)
				break;			
		}
		return (false);
	}

	/**
	 * Method to check if a piece is protecting the king from a check in back diagonal alignment
	 * @param chessBoard ChessBoard
	 * @param position Position
	 * @param teamColor TeamColor
	 * @return boolean
	 */
	public static boolean backslashDiagonalProtection(ChessBoardGameInterface chessBoard, Position position, TeamColor teamColor) {
		return backslashDiagonalProtection(chessBoard, position.getX(), position.getY(), teamColor);
	}

	/**
	 * Method to check if a piece is protecting the king from a check in back diagonal alignment
	 * @param chessBoard ChessBoard
	 * @param xPos int
	 * @param yPos int
	 * @param teamColor TeamColor
	 * @return boolean
	 */
	public static boolean backslashDiagonalProtection(ChessBoardGameInterface chessBoard, int xPos, int yPos, TeamColor teamColor) {
		TeamColor enemyType = getEnemyTeamColor(teamColor);

		// King on the Diagonal \ Up
		int y = yPos - 1;
		for(int x = xPos - 1; x >= 0 && y >= 0; x--, y--)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor && chessBoard.getPiece(x, y) instanceof King)
			{
				y = yPos + 1;
				for(x = xPos + 1; x < ChessBoard.boardSize && y < ChessBoard.boardSize; x++, y++)
				{
					if (chessBoard.getBoardPosition(x, y) == teamColor)
						break;
					else if (chessBoard.getBoardPosition(x, y) == enemyType)
					{
						if (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop)
							return (true);
						else
							break;
					}
				}
				break;
			}
			else if (chessBoard.getBoardPosition(x, y) != null)
				break;
		}
		// King on the Diagonal \ Down
		y = yPos + 1;
		for(int x = xPos + 1; x < ChessBoard.boardSize && y < ChessBoard.boardSize; x++, y++)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor && chessBoard.getPiece(x, y) instanceof King)
			{
				y = yPos - 1;
				for(x = xPos - 1; x >= 0 && y >= 0; x--, y--)
				{
					if (chessBoard.getBoardPosition(x, y) == teamColor)
						break;
					else if (chessBoard.getBoardPosition(x, y) == enemyType)
					{
						if (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop)
							return (true);
						else
							break;
					}
				}
				break;
			}
			else if (chessBoard.getBoardPosition(x, y) != null)
				break;
		}		
		return (false);
	}

	/**
	 * Method to check a Check situation
	 * @param chessBoard ChessBoard
	 * @param xPos int
	 * @param yPos int
	 * @param teamColor TeamColor
	 * @param kingCanCapture boolean, true if the King can capture the Piece that create a Check situation
	 * @return boolean
	 */
	public static boolean isCheck(ChessBoardGameInterface chessBoard, int xPos, int yPos, TeamColor teamColor, boolean kingCanCapture) {
		int y = 0;
		int x = 0;
		TeamColor enemyType = getEnemyTeamColor(teamColor);
		
		// Horizontal Left
		for (x = xPos - 1; x >= 0; x--)
		{
			if (chessBoard.getBoardPosition(x, yPos) == teamColor && chessBoard.getPiece(x, yPos) instanceof King)
				break;
			else if (chessBoard.getBoardPosition(x, yPos) == enemyType)
			{
				if (x == xPos - 1 && chessBoard.getPiece(x, yPos) != null && kingCanCapture && chessBoard.getPiece(x, yPos) instanceof King)
					return (true);
				else if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook))
					return (true);
				else
					break;
			}
		}
		// Horizontal Right
		for (x = xPos + 1; x < ChessBoard.boardSize; x++)
		{
			if (chessBoard.getBoardPosition(x, yPos) == teamColor && chessBoard.getPiece(x, yPos) instanceof King)
				break;
			else if (chessBoard.getBoardPosition(x, yPos) == enemyType)
			{
				if (x == xPos + 1 && chessBoard.getPiece(x, yPos) != null && kingCanCapture && chessBoard.getPiece(x, yPos) instanceof King)
					return (true);
				else if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook))
					return (true);
				else
					break;
			}
		}
		// Vertical Up
		for (y = yPos - 1; y >= 0; y--)
		{
			if (chessBoard.getBoardPosition(xPos, y) == teamColor && chessBoard.getPiece(xPos, y) instanceof King)
				break;
			else if (chessBoard.getBoardPosition(xPos, y) == enemyType)
			{
				if (y == yPos - 1 && chessBoard.getPiece(xPos, y) != null && kingCanCapture && chessBoard.getPiece(xPos, y) instanceof King)
					return (true);
				else if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook))
					return (true);
				else
					break;
			}
		}
		// Vertical Down
		for (y = yPos + 1; y < ChessBoard.boardSize; y++)
		{
			if (chessBoard.getBoardPosition(xPos, y) == teamColor && chessBoard.getPiece(xPos, y) instanceof King)
				break;
			else if (chessBoard.getBoardPosition(xPos, y) == enemyType)
			{
				if (y == yPos + 1 && chessBoard.getPiece(xPos, y) != null && kingCanCapture && chessBoard.getPiece(xPos, y) instanceof King)
					return (true);
				else if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook))
					return (true);
				else
					break;
			}
		}
		// Diagonal 1 \ Up
		for (y = yPos - 1, x = xPos - 1; y >= 0 && x >= 0; y--, x--)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor && chessBoard.getPiece(x, y) instanceof King)
				break;
			else if (chessBoard.getBoardPosition(x, y) == enemyType)
			{
				if (y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && ((kingCanCapture && chessBoard.getPiece(x, y) instanceof King) || (teamColor == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn)))
					return (true);
				else if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
					return (true);
				else
					break;
			}
		}
		// Diagonal 1 \ Down
		for (y = yPos + 1, x = xPos + 1; y < ChessBoard.boardSize && x < ChessBoard.boardSize; y++, x++)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor && chessBoard.getPiece(x, y) instanceof King)
				break;
			else if (chessBoard.getBoardPosition(x, y) == enemyType)
			{
				if (y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && ((kingCanCapture && chessBoard.getPiece(x, y) instanceof King) || (teamColor == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn)))
					return (true);
				else if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
					return (true);
				else
					break;
			}
		}
		// Diagonal 2 / Up
		for (y = yPos - 1, x = xPos + 1; y >= 0 && x < ChessBoard.boardSize; y--, x++)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor && chessBoard.getPiece(x, y) instanceof King)
				break;
			else if (chessBoard.getBoardPosition(x, y) == enemyType)
			{
				if (y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && ((kingCanCapture && chessBoard.getPiece(x, y) instanceof King) || (teamColor == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn)))
					return (true);
				else if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
					return (true);
				else
					break;
			}
		}
		// Diagonal 2 / Down
		for (y = yPos + 1, x = xPos - 1; y < ChessBoard.boardSize && x >= 0; y++, x--)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor && chessBoard.getPiece(x, y) instanceof King)
				break;
			else if (chessBoard.getBoardPosition(x, y) == enemyType)
			{
				if (y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && ((kingCanCapture && chessBoard.getPiece(x, y) instanceof King) || (teamColor == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn)))
					return (true);
				else if (chessBoard.getBoardPosition(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
					return (true);
				else
					break;
			}
		}		
		// Knight
		for (y = -2; y <= 2; y++)
		{
			if (y != 0)
			{
				x = y % 2 == 0 ? 1 : 2;
				if (yPos + y >= 0 && yPos + y < ChessBoard.boardSize && xPos - x >= 0 && xPos - x < ChessBoard.boardSize && chessBoard.getBoardPosition(xPos - x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos - x, yPos + y) != null)
				{
					if (chessBoard.getPiece(xPos - x, yPos + y) != null && chessBoard.getPiece(xPos - x, yPos + y) instanceof Knight)
						return (true);
				}
				if (yPos + y >= 0 && yPos + y < ChessBoard.boardSize && xPos + x >= 0 && xPos + x < ChessBoard.boardSize && chessBoard.getBoardPosition(xPos + x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos + x, yPos + y) != null)
				{
					if (chessBoard.getPiece(xPos + x, yPos + y) != null && chessBoard.getPiece(xPos + x, yPos + y) instanceof Knight)
						return (true);
				}
			}
		}
		return (false);
	}

	/**
	 * Method to check a Checkmate situation
	 * @param chessboard ChessBoard
	 * @param xPos int
	 * @param yPos int
	 * @param teamColor TeamColor
	 * @return boolean
	 */
	public static boolean isThisProtecting(ChessBoardGameInterface chessboard, int xPos, int yPos, TeamColor teamColor) {
		PieceInterface checkPiece = chessboard.getGameManagement().getCheckPieces().get(0);
		// Vertical up threat
		if (chessboard.getKing(teamColor).getPosition().getX() == checkPiece.getPosition().getX() && chessboard.getKing(teamColor).getPosition().getY() > checkPiece.getPosition().getY())
			if (xPos == chessboard.getKing(teamColor).getPosition().getX() && yPos < chessboard.getKing(teamColor).getPosition().getY() && yPos > checkPiece.getPosition().getY())
				return (true);
		// Vertical down threat
		if (chessboard.getKing(teamColor).getPosition().getX() == checkPiece.getPosition().getX() && chessboard.getKing(teamColor).getPosition().getY() < checkPiece.getPosition().getY())
			if (xPos == chessboard.getKing(teamColor).getPosition().getX() && yPos > chessboard.getKing(teamColor).getPosition().getY() && yPos < checkPiece.getPosition().getY())
				return (true);
		// Horizontal left threat
		if (chessboard.getKing(teamColor).getPosition().getX() > checkPiece.getPosition().getX() && chessboard.getKing(teamColor).getPosition().getY() == checkPiece.getPosition().getY())
			if (yPos == chessboard.getKing(teamColor).getPosition().getY() && xPos < chessboard.getKing(teamColor).getPosition().getX() && xPos > checkPiece.getPosition().getX())
				return (true);
		// Horizontal right threat
		if (chessboard.getKing(teamColor).getPosition().getX() < checkPiece.getPosition().getX() && chessboard.getKing(teamColor).getPosition().getY() == checkPiece.getPosition().getY())
			if (yPos == chessboard.getKing(teamColor).getPosition().getY() && xPos > chessboard.getKing(teamColor).getPosition().getX() && xPos < checkPiece.getPosition().getX())
				return (true);
		// Diagonal 1 \ up threat
		int y = checkPiece.getPosition().getY();
		if (chessboard.getKing(teamColor).getPosition().getX() > checkPiece.getPosition().getX() && chessboard.getKing(teamColor).getPosition().getY() > checkPiece.getPosition().getY())
			for (int x = checkPiece.getPosition().getX(); x < chessboard.getKing(teamColor).getPosition().getX() && y < chessboard.getKing(teamColor).getPosition().getY(); x++, y++)
				if (xPos == x && yPos == y)
					return (true);
		// Diagonal 1 \ down threat
		y = checkPiece.getPosition().getY();
		if (chessboard.getKing(teamColor).getPosition().getX() < checkPiece.getPosition().getX() && chessboard.getKing(teamColor).getPosition().getY() < checkPiece.getPosition().getY())
			for (int x = checkPiece.getPosition().getX(); x > chessboard.getKing(teamColor).getPosition().getX() && y > chessboard.getKing(teamColor).getPosition().getY(); x--, y--)
				if (xPos == x && yPos == y)
					return (true);
		// Diagonal 2 / up threat
		y = checkPiece.getPosition().getY();
		if (chessboard.getKing(teamColor).getPosition().getX() < checkPiece.getPosition().getX() && chessboard.getKing(teamColor).getPosition().getY() > checkPiece.getPosition().getY())
			for (int x = checkPiece.getPosition().getX(); x > chessboard.getKing(teamColor).getPosition().getX() && y < chessboard.getKing(teamColor).getPosition().getY(); x--, y++)
				if (xPos == x && yPos == y)
					return (true);
		// Diagonal 2 / down threat
		y = checkPiece.getPosition().getY();
		if (chessboard.getKing(teamColor).getPosition().getX() > checkPiece.getPosition().getX() && chessboard.getKing(teamColor).getPosition().getY() < checkPiece.getPosition().getY())
			for (int x = checkPiece.getPosition().getX(); x < chessboard.getKing(teamColor).getPosition().getX() && y > chessboard.getKing(teamColor).getPosition().getY(); x++, y--)
				if (xPos == x && yPos == y)
					return (true);
		return (false);
	}

	/**
	 * Return the opposite TeamColor
	 * @param teamColor TeamColor
	 * @return TeamColor
	 */
	private static TeamColor getEnemyTeamColor(TeamColor teamColor) {
		if (teamColor == TeamColor.White) {
			return TeamColor.Black;
		}
		else {
			return TeamColor.White;
		}
	}


}
