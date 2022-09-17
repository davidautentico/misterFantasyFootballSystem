package com.drosa.misterfantasysystem.domain.entities;

import java.util.List;

import com.drosa.misterfantasysystem.domain.enums.PlayerPosition;
import com.drosa.misterfantasysystem.domain.enums.TeamRef;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@Builder(toBuilder=true)
@ToString
@EqualsAndHashCode
public class MisterPlayer {

  private final String id;

  private final String name;

  private final String surname;

  private final PlayerPosition position;

  private final TeamRef teamRef;

  private final int actualAverage;

  private final List<Integer> actualStreak;

  private final long value;

  private final int totalPoints;

  private final int totalPoints2122;

  private final long  clause;

  private final long  saleValue;

  private final String owner;

  private final String href;

  private final boolean isInMarket;

  public String printStats(){
    double performance = this.getTotalPoints() * 1.0 / (this.getClause() * 1.0 / 1000000);
    return  this.getName() + " " + this.getSurname()
        + " | " + this.getPosition()
        + " | "+ this.getValue() / 1000
        + " | "+ this.getClause() / 1000
        + " | "+ this.getTotalPoints()
        + " | "+ this.getTotalPoints2122()
        + " | " + String.format("%,.2f", performance)//.replace(',', '.')
        + " | " + this.getOwner();
  }

}
