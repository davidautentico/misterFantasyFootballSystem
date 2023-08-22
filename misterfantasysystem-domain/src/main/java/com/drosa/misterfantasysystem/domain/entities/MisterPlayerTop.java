package com.drosa.misterfantasysystem.domain.entities;

import java.util.List;

import com.drosa.misterfantasysystem.domain.enums.PlayerPosition;
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

  private final List<MisterPlayer> playerList;

  @Override
  public String toString() {
    String header = position
        + " | " + numberOfPlayers
        + " | " + String.format("%,.2f", avgPerformance)
        + " | " + String.format("%,.2f", avgPoints);

    StringBuilder playerLines = new StringBuilder();
    for (MisterPlayer misterPlayer : playerList) {
      playerLines.append(misterPlayer.getName()).append(" ")
          .append(misterPlayer.getSurname()).append(" | ")
          .append(misterPlayer.getPosition()).append(" | ")
          .append(String.format("%,.2f", misterPlayer.getStreakPerformance()))
          .append("\n");
    }

    return header + " \n " + playerLines.toString().trim();
  }
}
