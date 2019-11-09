package pieces;

import common.Position;
import game.ChessBoardGameInterface;
import game.MovementRules;
import game.ChessBoard;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class Queen extends Piece {
	
	public Queen(TeamColor teamColor, Position position) {
		super(teamColor, position);
	}
	
	@Override
	public List<Position> getPossibilities(ChessBoardGameInterface chessBoard) {
		List<Position> positions = new ArrayList<>();
		int y = this.getPosition().getY() + 1;
		if (!MovementRules.slashDiagonalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()) && !MovementRules.verticalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()) && !MovementRules.horizontalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()))
		{
			for(int x = this.getPosition().getX() + 1; x < ChessBoard.boardSize && y < ChessBoard.boardSize; x++, y++)
			{
				if (chessBoard.getBoardPosition(x, y) == null)
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor()))
							positions.add(new Position(x, y));
					}
					else
						positions.add(new Position(x, y));
				}
				else if (chessBoard.getBoardPosition(x, y) == this.getTeamColor())
					break;
				else
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor()))
							positions.add(new Position(x, y));
					}
					else
						positions.add(new Position(x, y));
					break;
				}
			}
			y = this.getPosition().getY() - 1;
			for(int x = this.getPosition().getX() - 1; x >= 0 && y >= 0; x--, y--)
			{
				if (chessBoard.getBoardPosition(x, y) == null)
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor()))
							positions.add(new Position(x, y));
					}
					else
						positions.add(new Position(x, y));
				}
				else if (chessBoard.getBoardPosition(x, y) == this.getTeamColor())
					break;
				else
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor()))
							positions.add(new Position(x, y));
					}
					else
						positions.add(new Position(x, y));
					break;
				}
			}
		}
		if (!MovementRules.backslashDiagonalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()) && !MovementRules.verticalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()) && !MovementRules.horizontalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()))
		{
			y = this.getPosition().getY() + 1;
			for (int x = this.getPosition().getX() - 1; x >= 0 && y < ChessBoard.boardSize; x--, y++)
			{
				if (chessBoard.getBoardPosition(x, y) == null)
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor()))
							positions.add(new Position(x, y));
					}
					else
						positions.add(new Position(x, y));
				}
				else if (chessBoard.getBoardPosition(x, y) == this.getTeamColor())
					break;
				else
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor()))
							positions.add(new Position(x, y));
					}
					else
						positions.add(new Position(x, y));
					break;
				}
			}
			y = this.getPosition().getY() - 1;
			for (int x = this.getPosition().getX() + 1; x < ChessBoard.boardSize && y >= 0; x++, y--)
			{
				if (chessBoard.getBoardPosition(x, y) == null)
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor()))
							positions.add(new Position(x, y));
					}
					else
						positions.add(new Position(x, y));
				}
				else if (chessBoard.getBoardPosition(x, y) == this.getTeamColor())
					break;
				else
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, x, y, this.getTeamColor()))
							positions.add(new Position(x, y));
					}
					else
						positions.add(new Position(x, y));
					break;
				}
			}
		}
		// Rook ability
		if (!MovementRules.horizontalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()) && !MovementRules.slashDiagonalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()) && !MovementRules.backslashDiagonalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()))
		{
			for (y = this.getPosition().getY() - 1; y >= 0; y--)
			{
				if (chessBoard.getBoardPosition(this.getPosition().getX(), y) == null)
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, this.getPosition().getX(), y, this.getTeamColor()))
							positions.add(new Position(this.getPosition().getX(), y));
					}
					else
						positions.add(new Position(this.getPosition().getX(), y));
				}
				else if (chessBoard.getBoardPosition(this.getPosition().getX(), y) == this.getTeamColor())
					break;
				else
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, this.getPosition().getX(), y, this.getTeamColor()))
							positions.add(new Position(this.getPosition().getX(), y));
					}
					else
						positions.add(new Position(this.getPosition().getX(), y));
					break;
				}
			}
			for (y = this.getPosition().getY() + 1; y < ChessBoard.boardSize; y++)
			{
				if (chessBoard.getBoardPosition(this.getPosition().getX(), y) == null)
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, this.getPosition().getX(), y, this.getTeamColor()))
							positions.add(new Position(this.getPosition().getX(), y));
					}
					else
						positions.add(new Position(this.getPosition().getX(), y));
				}
				else if (chessBoard.getBoardPosition(this.getPosition().getX(), y) == this.getTeamColor())
					break;
				else
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, this.getPosition().getX(), y, this.getTeamColor()))
							positions.add(new Position(this.getPosition().getX(), y));
					}
					else
						positions.add(new Position(this.getPosition().getX(), y));
					break;
				}
			}
		}
		if (!MovementRules.verticalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()) && !MovementRules.slashDiagonalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()) && !MovementRules.backslashDiagonalProtection(chessBoard, this.getPosition().getX(), this.getPosition().getY(), this.getTeamColor()))
		{
			for (int x = this.getPosition().getX() - 1; x >= 0; x--)
			{
				if (chessBoard.getBoardPosition(x, this.getPosition().getY()) == null)
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, x, this.getPosition().getY(), this.getTeamColor()))
							positions.add(new Position(x, this.getPosition().getY()));
					}
					else
						positions.add(new Position(x, this.getPosition().getY()));
				}
				else if (chessBoard.getBoardPosition(x, this.getPosition().getY()) == this.getTeamColor())
					break;
				else
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, x, this.getPosition().getY(), this.getTeamColor()))
							positions.add(new Position(x, this.getPosition().getY()));
					}
					else
						positions.add(new Position(x, this.getPosition().getY()));
					break;
				}
			}
			for (int x = this.getPosition().getX() + 1; x < ChessBoard.boardSize; x++)
			{
				if (chessBoard.getBoardPosition(x, this.getPosition().getY()) == null)
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, x, this.getPosition().getY(), this.getTeamColor()))
							positions.add(new Position(x, this.getPosition().getY()));
					}
					else
						positions.add(new Position(x, this.getPosition().getY()));
				}
				else if (chessBoard.getBoardPosition(x, this.getPosition().getY()) == this.getTeamColor())
					break;
				else
				{
					if (chessBoard.getGameManagement().isCheckState())
					{
						if (MovementRules.isThisProtecting(chessBoard, x, this.getPosition().getY(), this.getTeamColor()))
							positions.add(new Position(x, this.getPosition().getY()));
					}
					else
						positions.add(new Position(x, this.getPosition().getY()));
					break;
				}
			}
		}
		return positions;
	}
}
