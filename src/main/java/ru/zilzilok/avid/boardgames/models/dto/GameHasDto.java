package ru.zilzilok.avid.boardgames.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameHasDto {
    BoardGame boardGame;
    boolean has;
}
