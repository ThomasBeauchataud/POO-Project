package game;

import view.ChessBoard;
import pieces.*;

import java.util.Iterator;

public class GameLogic {
	
	//method to detect stalemate
	private boolean isOneKingStalemate(ChessBoard chessBoard, Piece king, TeamColor teamColor)
	{
		int nbPiece = 0;
		boolean stalemate = true;

		// A Player has only 1 king left, which is not in check position and can't move
		for (int y = 0; y < chessBoard.getBoardHeight(); y++)
		{
			for (int x = 0; x < chessBoard.getBoardWidth(); x++)
			{
				if (chessBoard.getBoardPosition(x, y) == teamColor)
					nbPiece++;
			}
		}
		if (nbPiece == 1)
		{
			for (int y = king.getPosition().getY() - 1; y <= king.getPosition().getY() + 1; y++)
			{
				for (int x = king.getPosition().getX() - 1; x <= king.getPosition().getX() + 1; x++)
				{
					if(y >= 0 && y < chessBoard.getBoardHeight() && x >= 0 && x < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(x, y) != teamColor)
					{
						if (!isCheck(chessBoard, x, y, teamColor, true))
						{
							stalemate = false;
							break;
						}
					}
				}
				if (!stalemate)
					break;
			}
		}
		else
			stalemate = false;
		return (stalemate);
	}
	
	public boolean isLimitPieceStalemate(ChessBoard chessBoard)
	{
		// Both Player have less then:
		// A king and a Queen
		// A king and a Rook
		// A king and two knight
		// A king, a bishop and a knight
		// A king and two bishop (one light square, one dark square)
		// A king and at least a pawn
		if (chessBoard.playerOneQueen != 0 || chessBoard.playerTwoQueen != 0)
			return (false);
		else if (chessBoard.playerOneRook != 0 || chessBoard.playerTwoRook != 0)
			return (false);
		else if (chessBoard.playerOneKnight > 1 || chessBoard.playerTwoKnight > 1)
			return (false);
		else if (((chessBoard.playerOneBishopDarkSquare != 0 || chessBoard.playerOneBishopLightSquare != 0) && chessBoard.playerOneKnight != 0) ||
				((chessBoard.playerTwoBishopDarkSquare != 0 || chessBoard.playerTwoBishopLightSquare != 0) && chessBoard.playerTwoKnight != 0))
			return (false);
		else if ((chessBoard.playerOneBishopDarkSquare != 0 && chessBoard.playerOneBishopLightSquare != 0) || (chessBoard.playerTwoBishopDarkSquare != 0 && chessBoard.playerTwoBishopLightSquare != 0))
			return (false);
		else if (chessBoard.playerOnePawn > 1 || chessBoard.playerTwoPawn > 1)
			return (false);
		return (true);
	}
	
	public boolean isStalemate(ChessBoard chessBoard, Piece king, TeamColor teamColor)
	{
		if (isOneKingStalemate(chessBoard, king, teamColor) || isLimitPieceStalemate(chessBoard))
		{
			chessBoard.stalemate = true;
			return (true);
		}
		return (false);
	}

	// Method to check if a piece is protecting the king from a check
	public boolean verticalProtection(ChessBoard chessBoard, Position position, TeamColor teamColor) {
		return verticalProtection(chessBoard, position.getY(), position.getY(), teamColor);
	}
	
	// Method to check if a piece is protecting the king from a check
	public boolean verticalProtection(ChessBoard chessBoard, int xPos, int yPos, TeamColor teamColor)
	{
		int y = 0;
		TeamColor enemyType;
		if (teamColor == TeamColor.White)
			enemyType = TeamColor.Black;
		else
			enemyType = TeamColor.White;

		// King on the Vertical Up
		for (y = yPos - 1; y >= 0; y--)
		{
			if (chessBoard.getBoardPosition(xPos, y) == teamColor && chessBoard.getPiece(xPos, y) instanceof King)
			{
				for (y = yPos + 1; y < chessBoard.getBoardHeight(); y++)
				{
					if (chessBoard.getBoardPosition(xPos, y) == teamColor)
						break;
					else if (chessBoard.getBoardPosition(xPos, y) == enemyType)
					{
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
		for (y = yPos + 1; y < chessBoard.getBoardHeight(); y++)
		{
			if (chessBoard.getBoardPosition(xPos, y) == teamColor && chessBoard.getPiece(xPos, y) instanceof King)
			{
				for (y = yPos - 1; y >= 0; y--)
				{
					if (chessBoard.getBoardPosition(xPos, y) == teamColor)
						break;
					else if (chessBoard.getBoardPosition(xPos,  y) == enemyType)
					{
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

	public boolean horizontalProtection(ChessBoard chessBoard, Position position, TeamColor teamColor) {
		return horizontalProtection(chessBoard, position.getX(), position.getY(), teamColor);
	}
	
	public boolean horizontalProtection(ChessBoard chessBoard, int xPos, int yPos, TeamColor teamColor)
	{
		int x = 0;
		TeamColor enemyType = getEnemyTeamColor(teamColor);

		// King on the Horizontal Left
		for (x = xPos - 1; x >= 0; x--)
		{
			if (chessBoard.getBoardPosition(x, yPos) == teamColor && chessBoard.getPiece(x, yPos) instanceof King)
			{
				for (x = xPos + 1; x < chessBoard.getBoardWidth(); x++)
				{
					if (chessBoard.getBoardPosition(x, yPos) == teamColor)
						break;
					else if (chessBoard.getBoardPosition(x, yPos) == enemyType)
					{
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
		for (x = xPos + 1; x < chessBoard.getBoardWidth(); x++)
		{
			if (chessBoard.getBoardPosition(x, yPos) == teamColor && chessBoard.getPiece(x, yPos) instanceof King)
			{
				for (x = xPos - 1; x >= 0; x--)
				{
					if (chessBoard.getBoardPosition(x, yPos) == teamColor)
						break;
					else if (chessBoard.getBoardPosition(x,  yPos) == enemyType)
					{
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

	public boolean slashDiagonalProtection(ChessBoard chessBoard, Position position, TeamColor teamColor) {
		return slashDiagonalProtection(chessBoard, position.getX(), position.getY(), teamColor);
	}
	
	public boolean slashDiagonalProtection(ChessBoard chessBoard, int xPos, int yPos, TeamColor teamColor)
	{
		TeamColor enemyType = getEnemyTeamColor(teamColor);

		// King on the Diagonal / Up
		int y = yPos - 1;
		for (int x = xPos + 1; x < chessBoard.getBoardWidth() && y >= 0; x++, y--)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor && chessBoard.getPiece(x, y) instanceof King)
			{
				y = yPos + 1;
				for (x = xPos - 1; x >= 0 && y < chessBoard.getBoardHeight(); x--, y++)
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
		// King on the Diagonal / Down
		y = yPos + 1;
		for (int x = xPos - 1; x >= 0 && y < chessBoard.getBoardHeight(); x--, y++)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor && chessBoard.getPiece(x, y) instanceof King)
			{
				y = yPos - 1;
				for (x = xPos + 1; x < chessBoard.getBoardWidth() && y >= 0; x++, y--)
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

	public boolean backslashDiagonalProtection(ChessBoard chessBoard, Position position, TeamColor teamColor) {
		return backslashDiagonalProtection(chessBoard, position.getX(), position.getY(), teamColor);
	}
	
	public boolean backslashDiagonalProtection(ChessBoard chessBoard, int xPos, int yPos, TeamColor teamColor)
	{
		TeamColor enemyType = getEnemyTeamColor(teamColor);

		// King on the Diagonal \ Up
		int y = yPos - 1;
		for(int x = xPos - 1; x >= 0 && y >= 0; x--, y--)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor && chessBoard.getPiece(x, y) instanceof King)
			{
				y = yPos + 1;
				for(x = xPos + 1; x < chessBoard.getBoardWidth() && y < chessBoard.getBoardHeight(); x++, y++)
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
		for(int x = xPos + 1; x < chessBoard.getBoardWidth() && y < chessBoard.getBoardHeight(); x++, y++)
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
		
	// Method to check check
	public boolean isCheck(ChessBoard chessBoard, int xPos, int yPos, TeamColor teamColor, boolean kingCanCapture) {
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
		for (x = xPos + 1; x < chessBoard.getBoardWidth(); x++)
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
		for (y = yPos + 1; y < chessBoard.getBoardHeight(); y++)
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
		for (y = yPos + 1, x = xPos + 1; y < chessBoard.getBoardHeight() && x < chessBoard.getBoardWidth(); y++, x++)
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
		for (y = yPos - 1, x = xPos + 1; y >= 0 && x < chessBoard.getBoardWidth(); y--, x++)
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
		for (y = yPos + 1, x = xPos - 1; y < chessBoard.getBoardHeight() && x >= 0; y++, x--)
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
				if (yPos + y >= 0 && yPos + y < chessBoard.getBoardHeight() && xPos - x >= 0 && xPos - x < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(xPos - x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos - x, yPos + y) != null)
				{
					if (chessBoard.getPiece(xPos - x, yPos + y) != null && chessBoard.getPiece(xPos - x, yPos + y) instanceof Knight)
						return (true);
				}
				if (yPos + y >= 0 && yPos + y < chessBoard.getBoardHeight() && xPos + x >= 0 && xPos + x < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(xPos + x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos + x, yPos + y) != null)
				{
					if (chessBoard.getPiece(xPos + x, yPos + y) != null && chessBoard.getPiece(xPos + x, yPos + y) instanceof Knight)
						return (true);
				}
			}
		}
		return (false);
	}
	
	// Method to find all the piece that create a check
	public void findAllCheckPieces(ChessBoard chessBoard, int xPos, int yPos, TeamColor teamColor) {
		int y = 0;
		int x = 0;
		TeamColor enemyType = getEnemyTeamColor(teamColor);
		
		// Horizontal Left
		for (x = xPos - 1; x >= 0; x--)
		{
			if (chessBoard.getBoardPosition(x, yPos) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(x, yPos) == enemyType)
			{
				if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook))
					chessBoard.checkPieces.add(chessBoard.getPiece(x, yPos));
				else
					break;
			}
		}
		// Horizontal Right
		for (x = xPos + 1; x < chessBoard.getBoardWidth(); x++)
		{
			if (chessBoard.getBoardPosition(x, yPos) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(x, yPos) == enemyType)
			{
				if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook))
					chessBoard.checkPieces.add(chessBoard.getPiece(x, yPos));
				else
					break;
			}
		}
		// Vertical Up
		for (y = yPos - 1; y >= 0; y--)
		{
			if (chessBoard.getBoardPosition(xPos, y) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(xPos, y) == enemyType)
			{
				if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook))
					chessBoard.checkPieces.add(chessBoard.getPiece(xPos, y));
				else
					break;
			}
		}
		// Vertical Down
		for (y = yPos + 1; y < chessBoard.getBoardHeight(); y++)
		{
			if (chessBoard.getBoardPosition(xPos, y) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(xPos, y) == enemyType)
			{
				if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook))
					chessBoard.checkPieces.add(chessBoard.getPiece(xPos, y));
				else
					break;
			}
		}
		// Diagonal 1 \ Up
		for (y = yPos - 1, x = xPos - 1; y >= 0 && x >= 0; y--, x--)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(x, y) == enemyType)
			{
				if (y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn))
					chessBoard.checkPieces.add(chessBoard.getPiece(x, y));
				else if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
					chessBoard.checkPieces.add(chessBoard.getPiece(x, y));
				else
					break;
			}
		}
		// Diagonal 1 \ Down
		for (y = yPos + 1, x = xPos + 1; y < chessBoard.getBoardHeight() && x < chessBoard.getBoardWidth(); y++, x++)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(x, y) == enemyType)
			{
				if (y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn))
					chessBoard.checkPieces.add(chessBoard.getPiece(x, y));
				else if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
					chessBoard.checkPieces.add(chessBoard.getPiece(x, y));
				else
					break;
			}
		}
		// Diagonal 2 / Up
		for (y = yPos - 1, x = xPos + 1; y >= 0 && x < chessBoard.getBoardWidth(); y--, x++)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(x, y) == enemyType)
			{
				if (y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn))
					chessBoard.checkPieces.add(chessBoard.getPiece(x, y));
				else if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
					chessBoard.checkPieces.add(chessBoard.getPiece(x, y));
				else
					break;
			}
		}
		// Diagonal 2 / Down
		for (y = yPos + 1, x = xPos - 1; y < chessBoard.getBoardHeight() && x >= 0; y++, x--)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(x, y) == enemyType)
			{
				if (y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn))
					chessBoard.checkPieces.add(chessBoard.getPiece(x, y));
				if (chessBoard.getBoardPosition(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
					chessBoard.checkPieces.add(chessBoard.getPiece(x, y));
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
				if (yPos + y >= 0 && yPos + y < chessBoard.getBoardHeight() && xPos - x >= 0 && xPos - x < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(xPos - x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos - x, yPos + y) != null)
				{
					if (chessBoard.getPiece(xPos - x, yPos + y) != null && chessBoard.getPiece(xPos - x, yPos + y) instanceof Knight)
						chessBoard.checkPieces.add(chessBoard.getPiece(xPos - x, yPos + y));
				}
				if (yPos + y >= 0 && yPos + y < chessBoard.getBoardHeight() && xPos + x >= 0 && xPos + x < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(xPos + x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos + x, yPos + y) != null)
				{
					if (chessBoard.getPiece(xPos + x, yPos + y) != null && chessBoard.getPiece(xPos + x, yPos + y) instanceof Knight)
						chessBoard.checkPieces.add(chessBoard.getPiece(xPos + x, yPos + y));
				}
			}
		}
	}
	
	// Method to find all the piece that can save the king from a checkmate
	public void findAllSaviorPieces(ChessBoard chessBoard, int xPos, int yPos, TeamColor teamColor, boolean protect) {
		int y = 0;
		int x = 0;
		TeamColor enemyType = getEnemyTeamColor(teamColor);
		
		// Horizontal Left
		for (x = xPos - 1; x >= 0; x--)
		{
			if (chessBoard.getBoardPosition(x, yPos) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(x, yPos) == enemyType)
			{
				if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook))
					chessBoard.saviorPieces.add(chessBoard.getPiece(x, yPos));
				else
					break;
			}
		}
		// Horizontal Right
		for (x = xPos + 1; x < chessBoard.getBoardWidth(); x++)
		{
			if (chessBoard.getBoardPosition(x, yPos) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(x, yPos) == enemyType)
			{
				if (chessBoard.getPiece(x, yPos) != null && (chessBoard.getPiece(x, yPos) instanceof Queen || chessBoard.getPiece(x, yPos) instanceof Rook))
					chessBoard.saviorPieces.add(chessBoard.getPiece(x, yPos));
				else
					break;
			}
		}
		// Vertical Up
		for (y = yPos - 1; y >= 0; y--)
		{
			if (chessBoard.getBoardPosition(xPos, y) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(xPos, y) == enemyType)
			{
				if (enemyType == TeamColor.Black && protect && y == yPos - 1 && chessBoard.getPiece(xPos, y) != null && chessBoard.getPiece(xPos, y) instanceof Pawn)
					chessBoard.saviorPieces.add(chessBoard.getPiece(xPos, y));
				if (enemyType == TeamColor.Black && protect && y == yPos - 2 && chessBoard.getPiece(xPos, y) != null && chessBoard.getPiece(xPos, y) instanceof Pawn && chessBoard.getPiece(xPos, y).isFirstTime())
					chessBoard.saviorPieces.add(chessBoard.getPiece(xPos, y));
				if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook))
					chessBoard.saviorPieces.add(chessBoard.getPiece(xPos, y));
				else
					break;
			}
		}
		// Vertical Down
		for (y = yPos + 1; y < chessBoard.getBoardHeight(); y++)
		{
			if (chessBoard.getBoardPosition(xPos, y) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(xPos, y) == enemyType)
			{
				if (enemyType == TeamColor.White && protect && y == yPos + 1 && chessBoard.getPiece(xPos, y) != null && chessBoard.getPiece(xPos, y) instanceof Pawn)
					chessBoard.saviorPieces.add(chessBoard.getPiece(xPos, y));
				if (enemyType == TeamColor.White && protect && y == yPos + 2 && chessBoard.getPiece(xPos, y) != null && chessBoard.getPiece(xPos, y) instanceof Pawn && chessBoard.getPiece(xPos, y).isFirstTime())
					chessBoard.saviorPieces.add(chessBoard.getPiece(xPos, y));
				if (chessBoard.getPiece(xPos, y) != null && (chessBoard.getPiece(xPos, y) instanceof Queen || chessBoard.getPiece(xPos, y) instanceof Rook))
					chessBoard.saviorPieces.add(chessBoard.getPiece(xPos, y));
				else
					break;
			}
		}
		// Diagonal 1 \ Up
		for (y = yPos - 1, x = xPos - 1; y >= 0 && x >= 0; y--, x--)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(x, y) == enemyType)
			{
				if (!protect && y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn))
					chessBoard.saviorPieces.add(chessBoard.getPiece(x, y));
				if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
					chessBoard.saviorPieces.add(chessBoard.getPiece(x, y));
				else
					break;
			}
		}
		// Diagonal 1 \ Down
		for (y = yPos + 1, x = xPos + 1; y < chessBoard.getBoardHeight() && x < chessBoard.getBoardWidth(); y++, x++)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(x, y) == enemyType)
			{
				if (!protect && y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn))
					chessBoard.saviorPieces.add(chessBoard.getPiece(x, y));
				if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
					chessBoard.saviorPieces.add(chessBoard.getPiece(x, y));
				else
					break;
			}
		}
		// Diagonal 2 / Up
		for (y = yPos - 1, x = xPos + 1; y >= 0 && x < chessBoard.getBoardWidth(); y--, x++)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(x, y) == enemyType)
			{
				if (protect == false && y == yPos - 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.White && chessBoard.getPiece(x, y) instanceof Pawn))
					chessBoard.saviorPieces.add(chessBoard.getPiece(x, y));
				if (chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
					chessBoard.saviorPieces.add(chessBoard.getPiece(x, y));
				else
					break;
			}
		}
		// Diagonal 2 / Down
		for (y = yPos + 1, x = xPos - 1; y < chessBoard.getBoardHeight() && x >= 0; y++, x--)
		{
			if (chessBoard.getBoardPosition(x, y) == teamColor)
				break;
			else if (chessBoard.getBoardPosition(x, y) == enemyType)
			{
				if (protect == false && y == yPos + 1 && chessBoard.getBoardPosition(x, y) != null && chessBoard.getPiece(x, y) != null && (teamColor == TeamColor.Black && chessBoard.getPiece(x, y) instanceof Pawn))
					chessBoard.saviorPieces.add(chessBoard.getPiece(x, y));
				if (chessBoard.getBoardPosition(x, y) != null && (chessBoard.getPiece(x, y) instanceof Queen || chessBoard.getPiece(x, y) instanceof Bishop))
					chessBoard.saviorPieces.add(chessBoard.getPiece(x, y));
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
				if (yPos + y >= 0 && yPos + y < chessBoard.getBoardHeight() && xPos - x >= 0 && xPos - x < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(xPos - x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos - x, yPos + y) != null)
				{
					if (chessBoard.getPiece(xPos - x, yPos + y) != null && chessBoard.getPiece(xPos - x, yPos + y) instanceof Knight)
						chessBoard.saviorPieces.add(chessBoard.getPiece(xPos - x, yPos + y));
				}
				if (yPos + y >= 0 && yPos + y < chessBoard.getBoardHeight() && xPos + x >= 0 && xPos + x < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(xPos + x, yPos + y) != teamColor && chessBoard.getBoardPosition(xPos + x, yPos + y) != null)
				{
					if (chessBoard.getPiece(xPos + x, yPos + y) != null && chessBoard.getPiece(xPos + x, yPos + y) instanceof Knight)
						chessBoard.saviorPieces.add(chessBoard.getPiece(xPos + x, yPos + y));
				}
			}
		}
	}

	
	// Method to check checkmate
	public boolean isCheckmate(ChessBoard chessboard, int xPos, int yPos, TeamColor teamColor)
	{
		boolean checkmate = true;
		int x = xPos;
		int y = yPos;
		for (y = yPos - 1; y <= yPos + 1; y++)
		{
			for (x = xPos - 1; x <= xPos + 1; x++)
			{
				if(y >= 0 && y < chessboard.getBoardHeight() && x >= 0 && x < chessboard.getBoardWidth() && chessboard.getBoardPosition(x, y) != teamColor)
				{
					if (!isCheck(chessboard, x, y, teamColor, true))
					{
						checkmate = false;
						break;
					}
				}
			}
			if (!checkmate)
				break;
		}
		/* NO castle when you are in check
		// Check is the king can castle
		if (chessboard.getPiece(xPos, yPos).canCastle(chessboard) == 1 && !isCheck(chessboard, xPos - 1, yPos, teamColor, true))
			return (false);
		if (chessboard.getPiece(xPos, yPos).canCastle(chessboard) == 2 && !isCheck(chessboard, xPos + 2, yPos, teamColor, true))
			return (false);
		if (chessboard.getPiece(xPos, yPos).canCastle(chessboard) == 3 && !isCheck(chessboard, xPos - 1, yPos, teamColor, true))
			return (false);
		if (chessboard.getPiece(xPos, yPos).canCastle(chessboard) == 4 && !isCheck(chessboard, xPos + 2, yPos, teamColor, true))
			return (false);
				*/
		if (chessboard.checkPieces.size() < 2)
		{
			Piece checkPiece = chessboard.checkPieces.get(0);
			canCapture(chessboard, checkPiece);
			canProtect(chessboard, xPos, yPos, teamColor, checkPiece);
			if (!chessboard.saviorPieces.isEmpty())
			{
				for(Iterator<Piece> piece = chessboard.saviorPieces.iterator(); piece.hasNext(); )
				{
					Piece item = piece.next();
					item.setSavior(true);
					if (verticalProtection(chessboard, item.getPosition().getX(), item.getPosition().getY(), item.getTeamColor()) || horizontalProtection(chessboard, item.getPosition().getX(), item.getPosition().getY(), item.getTeamColor()) ||
				    	slashDiagonalProtection(chessboard, item.getPosition().getX(), item.getPosition().getY(), item.getTeamColor()) || backslashDiagonalProtection(chessboard, item.getPosition().getX(), item.getPosition().getY(), item.getTeamColor()))
				    {
				    	item.setSavior(false);
				    	piece.remove();
				    }
				}
			}
			if (!chessboard.saviorPieces.isEmpty())
				checkmate = false;
		}
		return (checkmate);
	}
	
	// Method to check is someone can capture the piece that threat the king
	public void canCapture(ChessBoard chessboard, Piece checkPiece)
	{
		findAllSaviorPieces(chessboard, checkPiece.getPosition().getX(), checkPiece.getPosition().getY(), checkPiece.getTeamColor(), false);
	}
	
	// Method to check is someone can capture the threatening piece or protect the king from the piece that threat him
	public void canProtect(ChessBoard chessboard, int xPos, int yPos, TeamColor teamColor, Piece checkPiece)
	{
		if (checkPiece instanceof Knight || checkPiece instanceof Pawn)
			return;
		// Vertical up threat
		if (xPos == checkPiece.getPosition().getX() && yPos > checkPiece.getPosition().getY())
			for (int y = checkPiece.getPosition().getY() + 1; y < yPos; y++)
				findAllSaviorPieces(chessboard, checkPiece.getPosition().getX(), y, checkPiece.getTeamColor(), true);
		// Vertical down threat
		if (xPos == checkPiece.getPosition().getX() && yPos < checkPiece.getPosition().getY())
			for (int y = checkPiece.getPosition().getY() - 1; y > yPos; y--)
				findAllSaviorPieces(chessboard, checkPiece.getPosition().getX(), y, checkPiece.getTeamColor(), true);
		// Horizontal left threat
		if (xPos > checkPiece.getPosition().getX() && yPos == checkPiece.getPosition().getY())
			for (int x = checkPiece.getPosition().getX() + 1; x < xPos; x++)
				findAllSaviorPieces(chessboard, x, checkPiece.getPosition().getY(), checkPiece.getTeamColor(), true);
		// Horizontal right threat
		if (xPos < checkPiece.getPosition().getX() && yPos == checkPiece.getPosition().getY())
			for (int x = checkPiece.getPosition().getX() - 1; x > xPos; x--)
				findAllSaviorPieces(chessboard, x, checkPiece.getPosition().getY(), checkPiece.getTeamColor(), true);
		// Diagonal 1 \ up threat
		int y = checkPiece.getPosition().getY() + 1;
		if (xPos > checkPiece.getPosition().getX() && yPos > checkPiece.getPosition().getY())
			for (int x = checkPiece.getPosition().getX() + 1; x < xPos && y < yPos; x++, y++)
				findAllSaviorPieces(chessboard, x, y, checkPiece.getTeamColor(), true);
		// Diagonal 1 \ down threat
		y = checkPiece.getPosition().getY() - 1;
		if (xPos < checkPiece.getPosition().getX() && yPos < checkPiece.getPosition().getY())
			for (int x = checkPiece.getPosition().getX() - 1; x > xPos && y > yPos; x--, y--)
				findAllSaviorPieces(chessboard, x, y, checkPiece.getTeamColor(), true);				
		// Diagonal 2 / up threat
		y = checkPiece.getPosition().getY() + 1;
		if (xPos < checkPiece.getPosition().getX() && yPos > checkPiece.getPosition().getY())
			for (int x = checkPiece.getPosition().getX() - 1; x > xPos && y < yPos; x--, y++)
				findAllSaviorPieces(chessboard, x, y, checkPiece.getTeamColor(), true);
		// Diagonal 2 / down threat
		y = checkPiece.getPosition().getY() - 1;
		if (xPos > checkPiece.getPosition().getX() && yPos < checkPiece.getPosition().getY())
			for (int x = checkPiece.getPosition().getX() + 1; x < xPos && y > yPos; x++, y--)
				findAllSaviorPieces(chessboard, x, y, checkPiece.getTeamColor(), true);
	}
	
	public boolean isThisProtecting(ChessBoard chessboard, int xPos, int yPos, TeamColor teamColor)
	{
		Piece checkPiece = chessboard.checkPieces.get(0);
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

	private TeamColor getEnemyTeamColor(TeamColor teamColor) {
		if (teamColor == TeamColor.White) {
			return TeamColor.Black;
		}
		else {
			return TeamColor.White;
		}
	}
}
