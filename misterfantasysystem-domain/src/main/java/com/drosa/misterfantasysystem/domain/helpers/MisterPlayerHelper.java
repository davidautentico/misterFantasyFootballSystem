package com.drosa.misterfantasysystem.domain.helpers;

import com.drosa.misterfantasysystem.domain.entities.MisterPlayer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MisterPlayerHelper {

  public int getPlayerValue(MisterPlayer misterPlayer, String owner) {

    if (misterPlayer.getOwner().equalsIgnoreCase(owner)) {
      return (int) misterPlayer.getValue();
    }

    return (int) misterPlayer.getClause();
  }

  public boolean isOwner(MisterPlayer misterPlayer, String owner) {

    return StringUtils.hasLength(misterPlayer.getOwner()) && misterPlayer.getOwner().equalsIgnoreCase(owner);
  }

  public boolean isPlayer(MisterPlayer misterPlayer) {

    return StringUtils.hasLength(misterPlayer.getOwner());
  }

  public boolean playerNotOwner(MisterPlayer misterPlayer, String owner) {

    return StringUtils.hasLength(misterPlayer.getOwner()) && !misterPlayer.getOwner().equalsIgnoreCase(owner);
  }

  public double getPerformance(MisterPlayer misterPlayer, String owner) {
    return misterPlayer.getTotalPoints() * 1.0 / (getPlayerValue(misterPlayer, owner) * 1.0 / 1000000);
  }
}