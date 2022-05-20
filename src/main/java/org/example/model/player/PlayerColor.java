package org.example.model.player;

public enum PlayerColor {
    RED,
    BLUE;

    public PlayerColor getOpposite() {
        return switch (this) {
            case RED -> BLUE;
            case BLUE -> RED;
        };
    }
}
