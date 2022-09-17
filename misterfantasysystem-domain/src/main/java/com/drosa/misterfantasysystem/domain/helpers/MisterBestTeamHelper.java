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

  public MisterTeam getBestTeam(List<MisterPlayer> playerList) {
    List<MisterPlayer> gkPlayers = playerList.stream()
        .filter(misterPlayer -> misterPlayer.getPosition() == PlayerPosition.GK)
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(1)
        .collect(Collectors.toList());
    List<MisterPlayer> dfPlayers = playerList.stream().filter(misterPlayer -> misterPlayer.getPosition() == PlayerPosition.DF)
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(3)
        .collect(Collectors.toList());
    List<MisterPlayer> mfPlayers = playerList.stream().filter(misterPlayer -> misterPlayer.getPosition() == PlayerPosition.MF)
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(4)
        .collect(Collectors.toList());
    List<MisterPlayer> fwPlayers = playerList.stream().filter(misterPlayer -> misterPlayer.getPosition() == PlayerPosition.FW)
        .sorted(Comparator.comparing(MisterPlayer::getTotalPoints).reversed())
        .limit(3)
        .collect(Collectors.toList());

    int gkPoints = gkPlayers.stream().map(MisterPlayer::getTotalPoints).reduce(0, Integer::sum);
    int dfPoints = dfPlayers.stream().map(MisterPlayer::getTotalPoints).reduce(0, Integer::sum);
    int mfPoints = mfPlayers.stream().map(MisterPlayer::getTotalPoints).reduce(0, Integer::sum);
    int fwPoints = fwPlayers.stream().map(MisterPlayer::getTotalPoints).reduce(0, Integer::sum);

    return MisterTeam.builder()
        .teamPoints(gkPoints + dfPoints + mfPoints + fwPoints)
        .teamValue(0)
        .mfs(mfPlayers)
        .gk(gkPlayers.get(0))
        .dfs(dfPlayers)
        .fws(fwPlayers)
        .build();

  }
}
