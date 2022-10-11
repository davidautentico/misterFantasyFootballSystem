package com.drosa.misterfantasysystem.domain.helpers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.drosa.misterfantasysystem.domain.entities.MisterPlayer;
import com.drosa.misterfantasysystem.domain.entities.MisterTeam;
import com.drosa.misterfantasysystem.domain.enums.PlayerPosition;
import org.springframework.stereotype.Component;

@Component
public class MisterBestTeamHelper {

  private MisterTeam getBestTeamTactic(List<MisterPlayer> playerList, int dfNumber, int mfNumber, int fwNumber) {
    List<MisterPlayer> gkPlayers = playerList.stream()
        .filter(misterPlayer -> misterPlayer.getPosition() == PlayerPosition.GK)
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(1)
        .collect(Collectors.toList());
    List<MisterPlayer> dfPlayers = playerList.stream().filter(misterPlayer -> misterPlayer.getPosition() == PlayerPosition.DF)
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(dfNumber)
        .collect(Collectors.toList());
    List<MisterPlayer> mfPlayers = playerList.stream().filter(misterPlayer -> misterPlayer.getPosition() == PlayerPosition.MF)
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(mfNumber)
        .collect(Collectors.toList());
    List<MisterPlayer> fwPlayers = playerList.stream().filter(misterPlayer -> misterPlayer.getPosition() == PlayerPosition.FW)
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(fwNumber)
        .collect(Collectors.toList());

    int gkPoints = gkPlayers.stream().map(MisterPlayer::getTotalPoints).reduce(0, Integer::sum);
    int dfPoints = dfPlayers.stream().map(MisterPlayer::getTotalPoints).reduce(0, Integer::sum);
    int mfPoints = mfPlayers.stream().map(MisterPlayer::getTotalPoints).reduce(0, Integer::sum);
    int fwPoints = fwPlayers.stream().map(MisterPlayer::getTotalPoints).reduce(0, Integer::sum);

    return MisterTeam.builder()
        .owner(dfPlayers.get(0).getOwner())
        .teamPoints(gkPoints + dfPoints + mfPoints + fwPoints)
        .teamValue(0)
        .mfs(mfPlayers)
        .gk(gkPlayers.size() > 0 ? gkPlayers.get(0) : null
        )
        .dfs(dfPlayers)
        .fws(fwPlayers)
        .build();

  }

  public MisterTeam getBestTeamStrategic(List<MisterPlayer> playerList) {

    // 1-3-4-3
    MisterTeam misterTeam = getBestTeamTactic(playerList, 3, 4, 3);

    int size343 = misterTeam.getDfs().size() + misterTeam.getMfs().size() + misterTeam.getFws().size() + 1;
    int size442;
    int size541;
    if (size343 != 11) {
      MisterTeam misterTeam442 = getBestTeamTactic(playerList, 4, 4, 2);
      size442 = misterTeam442.getDfs().size() + misterTeam442.getMfs().size() + misterTeam442.getFws().size() + 1;
      if (size442 != 11) {
        MisterTeam misterTeam541 = getBestTeamTactic(playerList, 5, 4, 1);
        size541 = misterTeam541.getDfs().size() + misterTeam541.getMfs().size() + misterTeam541.getFws().size() + 1;
        if (size541 == 11) {
          return misterTeam541;
        } else {
          if (size343 >= size442 && size343 >= size541) {
            return misterTeam;
          } else if (size442 >= size343 && size442 >= size541) {
            return misterTeam442;
          }
          return misterTeam;
        }
      } else {
        return misterTeam442;
      }
    }

    return misterTeam;
  }
}
