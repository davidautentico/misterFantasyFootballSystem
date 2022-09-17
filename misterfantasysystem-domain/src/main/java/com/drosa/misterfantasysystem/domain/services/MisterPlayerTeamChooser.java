package com.drosa.misterfantasysystem.domain.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.drosa.misterfantasysystem.domain.entities.MisterPlayer;
import com.drosa.misterfantasysystem.domain.entities.MisterTeam;
import com.drosa.misterfantasysystem.domain.enums.PlayerPosition;
import com.drosa.misterfantasysystem.domain.helpers.MisterPlayerHelper;
import com.drosa.misterfantasysystem.domain.repositories.ConfigurationRepository;
import org.springframework.stereotype.Service;

@Service
public class MisterPlayerTeamChooser {

  private final ConfigurationRepository configurationRepository;

  private final MisterPlayerHelper misterPlayerHelper;

  public MisterPlayerTeamChooser(ConfigurationRepository configurationRepository,
      MisterPlayerHelper misterPlayerHelper) {
    this.configurationRepository = configurationRepository;
    this.misterPlayerHelper = misterPlayerHelper;
  }

  /**
   * Choose best team from playerList
   */
  public List<MisterTeam> dispatch(final List<MisterPlayer> playerList, final int valueLimit, final String ownerUser) {

    List<MisterPlayer> gkPlayers = playerList.stream().filter(misterPlayer -> misterPlayer.getPosition() == PlayerPosition.GK).collect(
        Collectors.toList());
    List<MisterPlayer> dfPlayers = playerList.stream().filter(misterPlayer -> misterPlayer.getPosition() == PlayerPosition.DF).collect(
        Collectors.toList());
    List<MisterPlayer> mfPlayers = playerList.stream().filter(misterPlayer -> misterPlayer.getPosition() == PlayerPosition.MF).collect(
        Collectors.toList());
    List<MisterPlayer> fwPlayers = playerList.stream().filter(misterPlayer -> misterPlayer.getPosition() == PlayerPosition.FW).collect(
        Collectors.toList());

    List<MisterTeam> misterTeams = new ArrayList<>();

    for (int i = 0; i < configurationRepository.getNumberOfIters(); i++) {
      int teamValue = 0;
      int teamPoints = 0;

      // choose GK
      int totalGks = gkPlayers.size();
      int gkIndex = ThreadLocalRandom.current().nextInt(0, totalGks);
      MisterPlayer chosenGk = gkPlayers.get(gkIndex).toBuilder().build();
      teamValue += misterPlayerHelper.getPlayerValue(chosenGk, ownerUser);
      teamPoints += chosenGk.getTotalPoints();

      // choose DFs
      List<MisterPlayer> chosenDfs = new ArrayList<>();
      int totalDfs = dfPlayers.size();
      HashSet<Integer> indexSet = new HashSet<>();
      int count = 0;
      while (count < configurationRepository.getNumberOfDfs()) {
        int idx = ThreadLocalRandom.current().nextInt(0, totalDfs);
        if (!indexSet.contains(idx)) {
          MisterPlayer player = dfPlayers.get(idx).toBuilder().build();
          chosenDfs.add(player);
          teamPoints += player.getTotalPoints();
          teamValue += misterPlayerHelper.getPlayerValue(player, ownerUser);
          indexSet.add(idx);
          count++;
        }
      }

      // choose MFs
      List<MisterPlayer> chosenMfs = new ArrayList<>();
      int total = mfPlayers.size();
      indexSet.clear();
      count = 0;
      while (count < configurationRepository.getNumberOfMfs()) {
        int idx = ThreadLocalRandom.current().nextInt(0, total);
        if (!indexSet.contains(idx)) {
          MisterPlayer player = mfPlayers.get(idx).toBuilder().build();
          chosenMfs.add(player);
          teamPoints += player.getTotalPoints();
          teamValue += misterPlayerHelper.getPlayerValue(player, ownerUser);
          indexSet.add(idx);
          count++;
        }
      }

      // choose FWs
      List<MisterPlayer> chosenFws = new ArrayList<>();
      total = fwPlayers.size();
      indexSet.clear();
      count = 0;
      while (count < configurationRepository.getNumberOfFws()) {
        int idx = ThreadLocalRandom.current().nextInt(0, total);
        if (!indexSet.contains(idx)) {
          MisterPlayer player = fwPlayers.get(idx).toBuilder().build();
          chosenFws.add(player);
          teamPoints += player.getTotalPoints();
          teamValue += misterPlayerHelper.getPlayerValue(player, ownerUser);
          indexSet.add(idx);
          count++;
        }
      }

      if (teamValue <= valueLimit) {
        misterTeams.add(MisterTeam.builder()
            .gk(chosenGk)
            .dfs(chosenDfs)
            .mfs(chosenMfs)
            .fws(chosenFws)
            .teamValue(teamValue)
            .teamPoints(teamPoints)
            .build()
        );
      }
    }

    return misterTeams.stream()
        .sorted(Comparator.comparing(MisterTeam::getTeamPoints).reversed())
        .limit(configurationRepository.getNumberOfTeams())
        .collect(Collectors.toList());
  }
}
