package com.drosa.misterfantasysystem.domain.helpers;

import com.drosa.misterfantasysystem.domain.entities.MisterPlayer;
import org.springframework.stereotype.Component;

@Component
public class MisterPlayerHelper {

  public int getPlayerValue(MisterPlayer misterPlayer, String owner){

    if (misterPlayer.getOwner().equalsIgnoreCase(owner)){
      return (int) misterPlayer.getValue();
    }

    return (int) misterPlayer.getClause();
  }

}
