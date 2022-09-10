package com.drosa.misterfantasysystem.infrastructure.mistereader.repository.entities;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@Builder
@ToString
public class MisterPlayerReport {

  private final MisterPlayerTop gkTop;

  private final MisterPlayerTop dfTop;

  private final MisterPlayerTop mfTop;

  private final MisterPlayerTop fwTop;

  private final List<MisterPlayer> marketPlayers;

  private final List<MisterPlayer> ownerPlayers;

  public String printReport() {
    String gkTopLine = "*** GK TOP ***" + "\n" + gkTop;
    String dfTopLine = "*** DF TOP ***" + "\n" + dfTop;
    String mfTopLine = "*** MF TOP ***" + "\n" + mfTop;
    String fwTopLine = "*** FW TOP ***" + "\n" + fwTop;

    String marketPlayersLine = "*** Free Market Players ***";
    StringBuilder playersStr = new StringBuilder();
    String ownerPlayersLine = "*** Owner Players ***";
    StringBuilder ownerStr = new StringBuilder();

    for (MisterPlayer marketPlayer : marketPlayers) {
      playersStr.append(marketPlayer.printStats()).append(" \n ");
    }

    for (MisterPlayer ownerPlayer : ownerPlayers) {
      ownerStr.append(ownerPlayer.printStats()).append(" \n ");
    }

    return gkTopLine + "\n" + dfTopLine + "\n" + mfTopLine + "\n" + fwTopLine + "\n"
        + marketPlayersLine + "\n" + playersStr + "\n"
        + ownerPlayersLine + "\n" + ownerStr + "\n"
        ;
  }

  @Override
  public int hashCode(){
    return marketPlayers.hashCode();
  }
}
