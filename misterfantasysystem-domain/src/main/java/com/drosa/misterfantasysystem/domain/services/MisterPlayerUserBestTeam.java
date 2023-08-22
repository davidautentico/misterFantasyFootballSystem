package com.drosa.misterfantasysystem.domain.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.drosa.misterfantasysystem.domain.entities.MisterPlayer;
import com.drosa.misterfantasysystem.domain.entities.MisterTeam;
import com.drosa.misterfantasysystem.domain.helpers.MisterBestTeamHelper;
import org.springframework.stereotype.Service;

@Service
public class MisterPlayerUserBestTeam {

  private final MisterBestTeamHelper misterBestTeamHelper;

  public MisterPlayerUserBestTeam(MisterBestTeamHelper misterBestTeamHelper) {
    this.misterBestTeamHelper = misterBestTeamHelper;
  }

  public List<MisterTeam> dispatch(final List<MisterPlayer> playerList) {

    Map<String, List<MisterPlayer>> userGroups = playerList.stream().collect(Collectors.groupingBy(MisterPlayer::getOwner));

    return userGroups.keySet().stream().map(key -> misterBestTeamHelper.getBestTeamStrategic(userGroups.get(key)))
        .collect(Collectors.toList());
  }
}
