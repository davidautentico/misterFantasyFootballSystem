package com.drosa.misterfantasysystem.domain.enums;

public enum MarketRecommendation {
  NONE("NONE"),
  NORMAL_BUY("NORMAL_BY"),
  IMPORTANT_BUY("IMPORTANT_BUY");

  private final String value;

  MarketRecommendation(final String value) {
    this.value = value;
  }
}
