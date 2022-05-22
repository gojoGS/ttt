package org.example.model.game;

public class PositionAlreadyOccupiedException extends RuntimeException {
    public PositionAlreadyOccupiedException(Position position) {
        super(String.format("%s is already occupied", position));
    }
}
