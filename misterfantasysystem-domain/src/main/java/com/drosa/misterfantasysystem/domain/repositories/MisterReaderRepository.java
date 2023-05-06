package com.drosa.misterfantasysystem.domain.repositories;

import com.drosa.misterfantasysystem.domain.entities.MisterGameWeekInfo;
import com.drosa.misterfantasysystem.domain.entities.MisterPlayerInfo;

public interface MisterReaderRepository {
  MisterPlayerInfo getPlayerInfo();
  MisterGameWeekInfo getMisterGameWeekInfo();
}
