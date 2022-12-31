package jinkyframe;

import jinkyframe.Tides.TidalHeightOccurrence;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static jinkyframe.Colours.*;
import static jinkyframe.ImageUtils.*;

public final class TidePanel {
    private TidePanel() {
        // static methods
    }

    private static final Font font = font("Minecraftia-Regular.ttf", 8.0f);

    public static BufferedImage generate(Info info, Margins margins) {

        var currentTime = info.systemInfo().currentTime();
        var zoneId = info.systemInfo().zoneId();

        var tides = info.tideInfo().tides().tidalHeightOccurrenceList();
        var todaysTides = tides.stream()
                .filter(tide -> {
                    var currentDate = currentTime.toLocalDate();
                    var tideDateTime = tide.localDateTime(zoneId);
                    return tideDateTime.toLocalDate().isEqual(currentDate) ||
                            tideDateTime.minusMinutes(1).toLocalDate().isEqual(currentDate);
                })
                .toList();

        var graph = generateGraph(todaysTides, currentTime.toLocalTime(), zoneId);

        int width = 264;

        return createImage(width, 112, g -> {
            int graphX = width - graph.getWidth() - margins.right() * 2 - 1;
            int graphY = 18;
            g.drawImage(graph, graphX, graphY, null);
            g.setColor(black);
            g.setFont(font);
            int x = graphX - 5;
            for (int hour = 0; hour <= 24; hour += 2) {
                var hourString = String.format("%02d", hour % 24);
                g.drawString(hourString, x, graph.getHeight() + graphY + 14);
                x += 20;
            }
            int y = graph.getHeight() + graphY - 1;
            for (int m = 1; m <= 7; m += 2) {
                g.drawString(String.valueOf(m), graphX - 7, y);
                y -= 20;
            }

            var title = "Thames Tide";
            int titleWidth = g.getFontMetrics().stringWidth(title);
            g.drawString(title, graphX + diff(graph.getWidth(), titleWidth), graphY);
        });
    }

    private static BufferedImage generateGraph(List<TidalHeightOccurrence> tides, LocalTime currentTime,
                                               ZoneId zoneId) {
        int w = 240;
        int h = 80;
        return createImage(w, h, g -> {
            g.setPaint(Textures.dotFill(black));
            for (int y = 10; y < h; y += 10) {
                g.drawLine(1, y, w, y);
            }
            for (int x = 10; x < w; x += 10) {
                g.drawLine(x, 0, x, h);
            }

            int x1 = 0;
            int x2 = 5;

            var y1 = (int) Math.round(h - tides.get(0).height() * 10);

            g.setColor(blue);
            var stroke = g.getStroke();
            g.setStroke(new BasicStroke(3.0f));

            int timeX = 0;
            int timeY = 0;

            for (int i = 1; i < tides.size(); i++) {
                var point = tides.get(i);
                var y2 = (int) Math.round(h - point.height() * 10);

                var pointTime = point.localDateTime(zoneId).toLocalTime();
                if (currentTime.plusMinutes(15).truncatedTo(ChronoUnit.HOURS).equals(pointTime)) {
                    timeX = x2;
                    timeY = y2;
                }

                g.drawLine(x1, y1, x2, y2);
                x1 += 5;
                x2 += 5;
                y1 = y2;
            }
            g.setStroke(stroke);

            g.setColor(red);
            g.fillOval(timeX - 5, timeY - 5, 10, 10);

            g.setColor(black);
            g.drawLine(0, 0, 0, h);
            g.drawLine(0, h - 1, w, h - 1);
        });
    }
}
