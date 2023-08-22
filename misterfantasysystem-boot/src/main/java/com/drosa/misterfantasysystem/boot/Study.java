package com.drosa.misterfantasysystem.boot;

import com.drosa.misterfantasysystem.boot.parser.HtmlParser;
import com.drosa.misterfantasysystem.domain.entities.MisterPlayer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Study {
    //Parse data folder and extract info
    public static void main(final String[] args) {

        final HtmlParser htmlParser = new HtmlParser();
        final Map<String, MisterPlayer> misterPlayerMap = new HashMap<>();

        for (int actualWeek = 1; actualWeek <= 38; actualWeek++) {
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
            analysis(misterPlayerMap, 5);
        }
    }

    private static void analysis(Map<String, MisterPlayer> misterPlayerMap, int targetPoint) {
        AtomicInteger total0 = new AtomicInteger();
        AtomicInteger count0 = new AtomicInteger();
        AtomicInteger total1 = new AtomicInteger();
        AtomicInteger count1 = new AtomicInteger();
        AtomicInteger total2 = new AtomicInteger();
        AtomicInteger count2 = new AtomicInteger();
        AtomicInteger total3 = new AtomicInteger();
        AtomicInteger count3 = new AtomicInteger();
        AtomicInteger total4 = new AtomicInteger();
        AtomicInteger count4 = new AtomicInteger();
        AtomicInteger total5 = new AtomicInteger();
        AtomicInteger count5 = new AtomicInteger();
        AtomicInteger total6 = new AtomicInteger();
        AtomicInteger count6 = new AtomicInteger();
        AtomicInteger total7 = new AtomicInteger();
        AtomicInteger count7 = new AtomicInteger();
        AtomicInteger total8 = new AtomicInteger();
        AtomicInteger count8 = new AtomicInteger();
        AtomicInteger total9 = new AtomicInteger();
        AtomicInteger count9 = new AtomicInteger();
        AtomicInteger total10 = new AtomicInteger();
        AtomicInteger count10 = new AtomicInteger();
        AtomicInteger total11 = new AtomicInteger();
        AtomicInteger count11 = new AtomicInteger();
        misterPlayerMap.forEach((s, misterPlayer) -> {
            List<Integer> pointList = misterPlayer.getActualStreak();
            if (pointList.size() > 1) {
                for (int i = 0; i < pointList.size() - 1; i++) {
                    int points = pointList.get(i);
                    int points1 = pointList.get(i + 1);
                    if (points <= 0) {
                        total0.getAndIncrement();
                        if (points1 >= targetPoint) {
                            count0.getAndIncrement();
                        }
                    }
                    if (points >= 1) {
                        total1.getAndIncrement();
                        if (points1 >= targetPoint) {
                            count1.getAndIncrement();
                        }
                    }
                    if (points >= 2) {
                        total2.getAndIncrement();
                        if (points1 >= targetPoint) {
                            count2.getAndIncrement();
                        }
                    }
                    if (points >= 3) {
                        total3.getAndIncrement();
                        if (points1 >= targetPoint) {
                            count3.getAndIncrement();
                        }
                    }
                    if (points >= 4) {
                        total4.getAndIncrement();
                        if (points1 >= targetPoint) {
                            count4.getAndIncrement();
                        }
                    }
                    if (points >= 5) {
                        total5.getAndIncrement();
                        if (points1 >= targetPoint) {
                            count5.getAndIncrement();
                        }
                    }
                    if (points >= 6) {
                        total6.getAndIncrement();
                        if (points1 >= targetPoint) {
                            count6.getAndIncrement();
                        }
                    }
                    if (points >= 7) {
                        total7.getAndIncrement();
                        if (points1 >= targetPoint) {
                            count7.getAndIncrement();
                        }
                    }
                    if (points >= 8) {
                        total8.getAndIncrement();
                        if (points1 >= targetPoint) {
                            count8.getAndIncrement();
                        }
                    }
                    if (points >= 9) {
                        total9.getAndIncrement();
                        if (points1 >= targetPoint) {
                            count9.getAndIncrement();
                        }
                    }
                    if (points >= 10) {
                        total10.getAndIncrement();
                        if (points1 >= targetPoint) {
                            count10.getAndIncrement();
                        }
                    }
                    if (points >= 11) {
                        total11.getAndIncrement();
                        if (points1 >= targetPoint) {
                            count11.getAndIncrement();
                        }
                    }
                }
            }
        });

        // results
        log.info("Prob >=5 count<=0 sample = <{}> res = <{}>", total0.get(), count0.get() * 100.0 / total0.get());
        log.info("Prob >=5 count>=1 sample = <{}> res = <{}>", total1.get(), count1.get() * 100.0 / total1.get());
        log.info("Prob >=5 count>=2 sample = <{}> res = <{}>", total2.get(), count2.get() * 100.0 / total2.get());
        log.info("Prob >=5 count>=3 sample = <{}> res = <{}>", total3.get(), count3.get() * 100.0 / total3.get());
        log.info("Prob >=5 count>=4 sample = <{}> res = <{}>", total4.get(), count4.get() * 100.0 / total4.get());
        log.info("Prob >=5 count>=5 sample = <{}> res = <{}>", total5.get(), count5.get() * 100.0 / total5.get());
        log.info("Prob >=5 count>=6 sample = <{}> res = <{}>", total6.get(), count6.get() * 100.0 / total6.get());
        log.info("Prob >=5 count>=7 sample = <{}> res = <{}>", total7.get(), count7.get() * 100.0 / total7.get());
        log.info("Prob >=5 count>=8 sample = <{}> res = <{}>", total8.get(), count8.get() * 100.0 / total8.get());
        log.info("Prob >=5 count>=9 sample = <{}> res = <{}>", total9.get(), count9.get() * 100.0 / total9.get());
        log.info("Prob >=5 count>=10 sample = <{}> res = <{}>", total10.get(), count10.get() * 100.0 / total10.get());
        log.info("Prob >=5 count>=11 sample = <{}> res = <{}>", total11.get(), count11.get() * 100.0 / total11.get());
    }
}
