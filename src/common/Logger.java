package common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import pieces.Piece;

@Aspect
public class Logger {

    //TODO Comment
    @Before("execution(public void pieces.Piece.move(game.ChessBoard, int, int))")
    public void logPieceMovement(JoinPoint joinPoint) {
        Piece piece = (Piece) joinPoint.getTarget();
        System.out.println(piece.toString()+" moving to {"+joinPoint.getArgs()[1]+","+joinPoint.getArgs()[2]+"}");
    }

}
