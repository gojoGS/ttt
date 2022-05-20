package org.example.model.gameresult;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.model.player.PlayerColor;

import javax.persistence.*;

@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class GameResult {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    String redPlayerName;

    @NotBlank
    String bluePlayerName;

    @NotNull
    int numberOfTurns;

    @Enumerated(EnumType.STRING)
    @NotNull
    PlayerColor colorOfWinner;
}
