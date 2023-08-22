package com.drosa.misterfantasysystem.domain.enums;

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
