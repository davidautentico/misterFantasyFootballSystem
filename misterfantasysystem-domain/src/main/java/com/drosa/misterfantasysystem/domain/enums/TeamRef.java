package com.drosa.misterfantasysystem.domain.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

public enum TeamRef {
  BILBAO("teams/1/athletic-club"),
  RAYO("teams/14/rayo-vallecano"),
  ATLETICO("teams/2/atletico"),
  BARCELONA("teams/3/barcelona"),
  MADRID("teams/15/real-madrid"),
  SEVILLA("teams/17/sevilla"),
  BETIS("teams/4/betis"),
  VALLADOLID("teams/317/valladolid"),
  ESPANYOL("teams/8/espanyol"),
  GETAFE("teams/9/getafe"),
  ALMERIA("teams/21/almeria"),
  CELTA("teams/5/celta"),
  MALLORCA("teams/408/mallorca"),
  CADIZ("teams/499/cadiz"),
  REALSOCIEDAD("teams/16/real-sociedad"),
  OSASUNA("teams/50/osasuna"),
  VALENCIA("teams/19/valencia"),
  ELCHE("teams/23/elche"),
  VILLAREAL("teams/20/villarreal"),
  GIRONA("teams/222/girona"),
  ;

  private static final Map<String, TeamRef> LOOKUP =
      Arrays.stream(TeamRef.values()).collect(Collectors.toMap(key -> key.value.toLowerCase(), Function.identity()));

  private final String value;

  /**
   * Class constructor.
   *
   * @param value itemLocation value
   */
  TeamRef(final String value) {
    this.value = value;
  }

  /**
   * Returns a {@link ItemLocation} by value.
   *
   * @param value string to be matched to value.
   * @return {@link ItemLocation} or null if no value matches.
   */
  public static TeamRef getByValue(final String value) {
    if (!StringUtils.hasLength(value)) {
      return null;
    }

    return LOOKUP.getOrDefault(value.toLowerCase(), null);
  }

  /**
   * Get associated itemLocation value.
   *
   * @return associated value
   */
  public String getValue() {
    return value;
  }

  public static Stream<TeamRef> stream() {
    return Stream.of(TeamRef.values());
  }
}
