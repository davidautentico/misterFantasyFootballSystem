package com.drosa.misterfantasysystem.domain.repositories;

import com.drosa.misterfantasysystem.domain.entities.MisterPlayer;
import com.drosa.misterfantasysystem.domain.entities.MisterPlayerInfo;

public interface MisterReaderRepository {
  MisterPlayerInfo getPlayerInfo();
}
