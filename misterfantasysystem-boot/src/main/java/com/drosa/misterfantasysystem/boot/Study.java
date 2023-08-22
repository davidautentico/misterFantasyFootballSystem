package com.drosa.misterfantasysystem.boot;

import com.drosa.misterfantasysystem.boot.parser.HtmlParser;
import com.drosa.misterfantasysystem.domain.entities.MisterPlayer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Study {
    //Parse data folder and extract info
    public static void main(final String[] args) {

        final HtmlParser htmlParser = new HtmlParser();
        final Map<String, MisterPlayer> misterPlayerMap = new HashMap<>();

        for (int actualWeek = 1; actualWeek <= 1; actualWeek++) {
            //get file gmactualWeek
            String fileName = "data/gm" + actualWeek + ".html";
            //load filename
            File file = new File(fileName);
            if (!file.exists()) {
                log.error("No existe el path <{}>", fileName);
            }

            log.info("File gameweek exists <{}>", fileName);

            //parse
            htmlParser.extractPlayerStreakFromFile(file, misterPlayerMap);

            //get stats
        }
    }
}
