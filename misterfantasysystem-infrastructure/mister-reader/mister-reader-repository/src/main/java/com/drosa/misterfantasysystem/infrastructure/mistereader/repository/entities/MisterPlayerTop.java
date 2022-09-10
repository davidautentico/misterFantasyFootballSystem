package com.drosa.misterfantasysystem.infrastructure.mistereader.repository.entities;

import com.drosa.misterfantasysystem.infrastructure.mistereader.repository.enums.PlayerPosition;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class MisterPlayerTop {

  private final int numberOfPlayers;

  private final PlayerPosition position;

  private final double avgPerformance;

  private final double avgPoints;

  @Override
  public String toString() {
    return position
        + " | " + numberOfPlayers
        + " | " + String.format("%,.2f", avgPerformance)
        + " | " + String.format("%,.2f", avgPoints);
  }
}
