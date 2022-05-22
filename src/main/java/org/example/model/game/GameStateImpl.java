package org.example.model.game;

import org.example.model.gameresult.GameResult;
import org.example.model.player.Player;
import org.example.model.player.PlayerColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameStateImpl implements GameState {
    private static final long BOARD_SIZE = 3;
    private final Map<Position, Optional<PlayerColor>> positionToOwnerMapping;
    private final Map<PlayerColor, Player> colorToPlayerMapping;
    private int numberOfTurns;
    private PlayerColor currentPlayerColor;
    private Optional<Position> plannedPosition;

    public GameStateImpl(Map<PlayerColor, Player> colorToPlayerMapping) {
        this.colorToPlayerMapping = colorToPlayerMapping;
        currentPlayerColor = PlayerColor.RED;
        positionToOwnerMapping = new HashMap<>();
        plannedPosition = Optional.empty();
        numberOfTurns = 0;

        for (int row = 0; row < BOARD_SIZE; ++row) {
            for (int column = 0; column < BOARD_SIZE; ++column) {
                var newPosition = new Position(row, column);
                positionToOwnerMapping.put(newPosition, Optional.empty());
            }
        }
    }

    @Override
    public boolean isPositionAlreadyOccupied(Position position) {
        var owner = positionToOwnerMapping.get(position);

        return owner.isPresent();
    }

    @Override
    public boolean isPositionAlreadySelectedAsPlanned(Position position) {
        return plannedPosition.isPresent() && plannedPosition.get().equals(position);
    }

    @Override
    public boolean doesPlayerHavePlannedPosition() {
        return this.plannedPosition.isPresent();
    }

    @Override
    public Optional<Position> getPlannedPosition() {
        return plannedPosition;
    }

    private boolean doesCurrentPlayerHasAFullRow() {
        return positionToOwnerMapping.entrySet()
            // vizsgáljuk meg a kulcs-érték párokat
            .stream()
            // ahol van birtokos, és az aktuális játékos az
            .filter(positionToOwnerEntry -> positionToOwnerEntry.getValue().isPresent()
                && positionToOwnerEntry.getValue().get().equals(currentPlayerColor))
            // kulcs érték párból csináljunk pozíciót
            .map(Map.Entry::getKey)
            // pozíciból nyerjük ki, hogy hanyadik sorban van
            .map(Position::getRow)
            // megkapjuk, hogy az egyes sorszámok hányszor szerepelnek
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            // az egyek sorok gyakorisága között van e olyan
            .values().stream()
            // ami a tábla méretével egyenlő (azaz az egészet az aktuális játékos foglalja el)
            .anyMatch(aLong -> aLong.equals(BOARD_SIZE));
    }

    private boolean doesCurrentPlayerHasAFullColumn() {
        return positionToOwnerMapping.entrySet()
            .stream()
            .filter(positionToOwnerEntry -> positionToOwnerEntry.getValue().isPresent()
                && positionToOwnerEntry.getValue().get().equals(currentPlayerColor))
            .map(Map.Entry::getKey)
            .map(Position::getColumn)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .values().stream()
            .anyMatch(aLong -> aLong.equals(BOARD_SIZE));
    }

    private boolean doesCurrentPlayerHasMainDiagonal() {
        return positionToOwnerMapping.entrySet()
            .stream()
            .filter(positionToOwnerEntry -> positionToOwnerEntry.getValue().isPresent()
                && positionToOwnerEntry.getValue().get().equals(currentPlayerColor))
            .filter(positionToOwnerEntry -> positionToOwnerEntry.getKey().getColumn()
                == positionToOwnerEntry.getKey().getRow())
            .count() == BOARD_SIZE;
    }

    private boolean doesCurrentPlayerHasAuxiliaryDiagonal() {
        return positionToOwnerMapping.entrySet()
            .stream()
            .filter(positionToOwnerEntry -> positionToOwnerEntry.getValue().isPresent()
                && positionToOwnerEntry.getValue().get().equals(currentPlayerColor))
            .filter(positionToOwnerEntry ->
                positionToOwnerEntry.getKey().getColumn() + positionToOwnerEntry.getKey().getRow()
                    + 1 == BOARD_SIZE)
            .count() == BOARD_SIZE;
    }

    private boolean doesCurrentPlayerHasAFullDiagonal() {
        return doesCurrentPlayerHasMainDiagonal() || doesCurrentPlayerHasAuxiliaryDiagonal();
    }

    @Override public void setPositionOfNextMove(Position position) {
        plannedPosition = Optional.of(position);
    }

    @Override public void unsetPositionOfNextMove() {
        plannedPosition = Optional.empty();
    }

    @Override public void occupyPositionByCurrentPlayer(Position position) {
        if (isPositionAlreadyOccupied(position)) {
            throw new PositionAlreadyOccupiedException(position);
        }

        this.positionToOwnerMapping.put(position, Optional.of(currentPlayerColor));
        unsetPositionOfNextMove();
    }

    @Override public Optional<PlayerColor> getOwnerOfPosition(Position position) {
        return positionToOwnerMapping.get(position);
    }

    @Override public boolean isCurrentPlayerWinner() {
        return doesCurrentPlayerHasAFullRow()
            || doesCurrentPlayerHasAFullColumn()
            || doesCurrentPlayerHasAFullDiagonal();
    }

    private boolean isAllPositionsOccupied() {
        return positionToOwnerMapping
            .values()
            .stream()
            .noneMatch(Optional::isEmpty);
    }

    @Override public boolean isDraw() {
        return isAllPositionsOccupied() && !isCurrentPlayerWinner();
    }

    @Override public void endTurn() {
        currentPlayerColor = currentPlayerColor.getOpposite();
        ++numberOfTurns;
    }

    @Override public void yield() {
        currentPlayerColor = currentPlayerColor.getOpposite();
    }

    @Override public GameResult getGameResult() {
        return GameResult.builder()
            .redPlayerName(colorToPlayerMapping.get(PlayerColor.RED).getName())
            .bluePlayerName(colorToPlayerMapping.get(PlayerColor.BLUE).getName())
            .colorOfWinner(currentPlayerColor)
            .numberOfTurns(numberOfTurns)
            .build();
    }

    @Override public GameResult getDrawGameResult() {
        return GameResult.builder()
            .redPlayerName(colorToPlayerMapping.get(PlayerColor.RED).getName())
            .bluePlayerName(colorToPlayerMapping.get(PlayerColor.BLUE).getName())
            .colorOfWinner(null)
            .numberOfTurns(numberOfTurns)
            .build();
    }

    @Override public PlayerColor getCurrentPlayerColor() {
        return currentPlayerColor;
    }

    @Override public Player getCurrentPlayer() {
        return colorToPlayerMapping.get(currentPlayerColor);
    }

    @Override public long getBoardSize() {
        return BOARD_SIZE;
    }
}
