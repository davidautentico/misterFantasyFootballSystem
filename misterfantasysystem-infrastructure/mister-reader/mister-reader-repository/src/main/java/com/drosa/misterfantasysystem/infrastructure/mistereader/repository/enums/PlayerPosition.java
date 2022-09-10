package com.drosa.misterfantasysystem.infrastructure.mistereader.repository.enums;

public enum PlayerPosition {
  NONE("NONE"),
  GK("GK"),
  DF("DF"),
  MF("MF"),
  FW("FW");

  private final String value;

  PlayerPosition(final String value) {
    this.value = value;
  }

}
