package org.example.model.game;

import org.example.model.gameresult.GameResult;
import org.example.model.player.Player;
import org.example.model.player.PlayerColor;

import java.util.Optional;

public interface GameState {
    /**
     * Mark a position as the possible next move, without really changing the ownership of that position
     * @param position the position that is planned to be occupied
     * @throws PositionAlreadyOccupiedException if the chosen position is already occupied
     */
    void setPositionOfNextMove(Position position);

    /**
     * Unset the planned position of the next move
     */
    void unsetPositionOfNextMove();

    /**
     * Occupy the position by the current player; it also unsets the mark on the planned position
     * @param position the position to be occupied
     */
    void occupyPositionByCurrentPlayer(Position position);

    /**
     * Returns whether the current player is a winner by the rules of classic tic-tac-toe.
     * That means, that given an N by N sized board, the current player is the winner if:
     * <ul>
     *     <li>They has N positions occupied in the same row</li>
     *     <li>They has N positions occupied in the same column</li>
     *     <li>They has N positions occupied in the main diagonal</li>
     *     <li>They has N positions occupied in the anti-diagonal</li>
     * </ul>
     *
     * @return whether the current player is winner
     */
    boolean isCurrentPlayerWinner();

    /**
     * Returns whether the state of the game is in a draw state.
     * If
     * <ul>
     *     <li>all position is occupied and</li>
     *     <li>neither of the players can be declared winner,</li>
     * </ul>
     * then the game is a draw.
     * @return Returns whether the state of the game is in a draw state.
     */
    boolean isDraw();

    /**
     * Ends a turn in the game. When this happens,
     * the current player is switched to the not current one
     */
    void endTurn();

    /**
     * If the current player yields, the opposite player wind the game.
     */
    void yield();

    /**
     * Returns a POJO containing data about the finished game
     * @return a {@link GameResult} instance describing the game
     */
    GameResult getGameResult();

    /**
     * Return the owner of a position, if it has any
     * @param position said position
     * @return the owner of said position wrapped in an Optional, or an empty optional if no owner found;
     */
    Optional<PlayerColor> getOwnerOfPosition(Position position);

    /**
     * Returns the color of the current player
     * @return the color of the current player
     */
    PlayerColor getCurrentPlayerColor();

    /**
     * Returns the current player
     * @return the current player
     */
    Player getCurrentPlayer();
}
