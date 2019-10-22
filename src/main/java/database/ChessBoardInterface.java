package database;

import entites.PositionInterface;

public interface ChessBoardInterface {

    /**
     * Initialize the chessboard
     */
    void init();

    /**
     * Return an Object from a Position
     * @param position PositionInterface
     * @return Object
     */
    Object get(PositionInterface position);

    /**
     * Return true if the Piece at the position from can go to the position to
     * @param from PositionInterface
     * @param to PositionInterface
     * @return boolean
     */
    boolean canMove(PositionInterface from, PositionInterface to);

}
