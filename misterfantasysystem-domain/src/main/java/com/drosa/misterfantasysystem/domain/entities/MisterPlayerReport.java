package com.drosa.misterfantasysystem.domain.entities;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@Builder(toBuilder = true)
@ToString
public class MisterPlayerReport {

  private final List<MisterTeam> allUserBestTeam;

  private final int totalOwnerValue;

  private final int totalOwnerPoints;

  private final List<MisterTeam> misterTeamBestTeams;

  private final MisterPlayerTop gkTop;

  private final MisterPlayerTop dfTop;

  private final MisterPlayerTop mfTop;

  private final MisterPlayerTop fwTop;

  private final MisterPlayerInfo misterPlayerInfo;

  private final List<MisterPlayer> marketPlayers;

  private final List<MisterPlayer> ownerPlayers;

  public String printReport() {
    String ownerStats = "**** Points and Value: " + totalOwnerPoints + " " + totalOwnerValue;

    String gkTopLine = "*** GK TOP ***" + "\n" + gkTop;
    String dfTopLine = "*** DF TOP ***" + "\n" + dfTop;
    String mfTopLine = "*** MF TOP ***" + "\n" + mfTop;
    String fwTopLine = "*** FW TOP ***" + "\n" + fwTop;

    String bestUsersTeamsLine = "*** TOP User teams ***";
    StringBuilder userStr = new StringBuilder();
    for (MisterTeam misterTeam : allUserBestTeam) {
      userStr.append(misterTeam.getOwner() + " points = " + misterTeam.getTeamPoints()).append(" \n ");
    }

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

    String ownerMisterTeamsLine = "*** Mister Teams ***";
    StringBuilder misterTeams = new StringBuilder();
    for (MisterTeam misterTeam : misterTeamBestTeams) {
      misterTeams.append(misterTeam.printInfo()).append(" \n ");
    }

    return ownerStats + "\n"
        + bestUsersTeamsLine + "\n" + userStr + "\n"
        + gkTopLine + "\n" + dfTopLine + "\n" + mfTopLine + "\n" + fwTopLine + "\n"
        + marketPlayersLine + "\n" + playersStr + "\n"
        + ownerPlayersLine + "\n" + ownerStr + "\n"
        + ownerMisterTeamsLine + "\n" + misterTeams + "\n"
        ;
  }

  @Override
  public int hashCode() {
    return marketPlayers.hashCode();
  }
}
