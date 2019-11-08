package common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class Logger {

    @Before("execution(public void pieces.Piece.move(game.ChessBoard, int, int))")
    public void logPieceMovement(JoinPoint joinPoint) {
        System.out.println("Trigger");
    }

}
