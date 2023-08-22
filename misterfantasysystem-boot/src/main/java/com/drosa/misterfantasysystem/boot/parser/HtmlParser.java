package com.drosa.misterfantasysystem.boot.parser;

import com.drosa.misterfantasysystem.domain.entities.MisterPlayer;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class HtmlParser {

    public List<MisterPlayer> extractPlayerStreakFromFile(final File file, final Map<String, MisterPlayer> misterPlayerMap) {

        // read gameweek-matches-summary

        // read "btn btn-player-gw" data-title = "<name>" y cuelga "points pt-1/2/3/4/"
        Document doc = null;
        try {
            doc = Jsoup.parse(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements panelAll = doc.getElementsByClass("panel panel-all players");
        panelAll.forEach(allPlayers -> {
            Elements players = allPlayers.getElementsByClass("btn btn-player-gw");
            players.forEach(player -> {
                String playerTitle = player.attributes().get("data-title");
                int points = getPoints(player);

                if (!misterPlayerMap.containsKey(playerTitle)) {
                    MisterPlayer misterPlayer = MisterPlayer.builder()
                            .name(playerTitle)
                            .actualStreak(new ArrayList<>())
                            .build();
                    misterPlayerMap.put(playerTitle, misterPlayer);
                }

                MisterPlayer misterPlayer = misterPlayerMap.get(playerTitle);
                misterPlayer.getActualStreak().add(points);

                log.info("Gameweek <{}> PlayerTitle <{}> points <{}> size <{}> streak <{}>", file.getName(), playerTitle, points,
                        misterPlayer.getActualStreak().size(),
                        misterPlayer.getActualStreak());
            });
        });


        return Collections.emptyList();
    }

    private int getPoints(Element player) {

        Elements points = player.getElementsByClass("points pt-1");
        if (points.size() == 0) {
            points = player.getElementsByClass("points pt-2");
            if (points.size() == 0) {
                points = player.getElementsByClass("points pt-3");
                if (points.size() == 0) {
                    points = player.getElementsByClass("points pt-4");
                }
            }
        }

        if (points.size() > 0) {
            TextNode textNode = (TextNode) points.get(0).childNodes().get(0);
            return Integer.parseInt(textNode.text().trim());
        }

        return 0;
    }
}
