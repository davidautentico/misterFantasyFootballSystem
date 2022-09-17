package com.drosa.misterfantasysystem.domain.entities;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class MisterTeam {

  private int teamValue;

  private int teamPoints;

  private final MisterPlayer gk;

  private final List<MisterPlayer> dfs;

  private final List<MisterPlayer> mfs;

  private final List<MisterPlayer> fws;

  public String printInfo() {
    StringBuilder dfString = new StringBuilder();
    for (MisterPlayer player : dfs) {
      dfString.append(player.printStats()).append("\n");
    }
    StringBuilder mfString = new StringBuilder();
    for (MisterPlayer player : mfs) {
      mfString.append(player.printStats()).append("\n");
    }
    StringBuilder fwString = new StringBuilder();
    for (MisterPlayer player : fws) {
      fwString.append(player.printStats()).append("\n");
    }

    return "*** Team Value = " + teamPoints + " " + teamValue + "\n"
        + gk.printStats() + "\n"
        + dfString
        + mfString
        + fwString + "\n";
  }
}
