package game.core.engine;
import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static game.api.LogConfig.LOGGER;

public class KarmaManager {
    private static final File PLAYERS_FILE = new File("players.txt");
    private static final DecimalFormat KARMA_FORMAT =
            new DecimalFormat("0.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    public static void updatePlayerKarma(String name, double deltaKarma) {
        List<PlayerEntry> entries = loadEntries();
        boolean updated = updateEntry(entries, name, deltaKarma);
        if (!updated) {
            entries.add(new PlayerEntry(name, deltaKarma, 0));
        }
        saveEntries(entries);
        LOGGER.info(String.format("Обновлена карма игрока: %s → +%s",
                name, KARMA_FORMAT.format(deltaKarma)));
    }

    public static double loadPlayerKarma(String name) {
        for (PlayerEntry e : loadEntries()) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e.getKarma();
            }
        }
        return 0.0;
    }

    private static List<PlayerEntry> loadEntries() {
        List<PlayerEntry> list = new ArrayList<>();
        if (!PLAYERS_FILE.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(PLAYERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                PlayerEntry entry = PlayerEntry.parse(line);
                if (entry != null) {
                    list.add(entry);
                }
            }
        } catch (IOException | NumberFormatException e) {
            LOGGER.warning("Ошибка при чтении players.txt: " + e.getMessage());
        }
        return list;
    }

    private static boolean updateEntry(List<PlayerEntry> entries, String name, double deltaKarma) {
        for (PlayerEntry e : entries) {
            if (e.getName().equalsIgnoreCase(name)) {
                e.addKarma(deltaKarma);
                return true;
            }
        }
        return false;
    }

    private static void saveEntries(List<PlayerEntry> entries) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PLAYERS_FILE))) {
            for (PlayerEntry e : entries) {
                writer.write(e.toLine());
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.severe("Ошибка записи в players.txt: " + e.getMessage());
        }
    }

    private static class PlayerEntry {
        private final String name;
        private double karma;
        private final int wins;

        PlayerEntry(String name, double karma, int wins) {
            this.name = name;
            this.karma = karma;
            this.wins = wins;
        }

        static PlayerEntry parse(String line) {
            String[] parts = line.split(";");
            if (parts.length < 2) return null;
            String n = parts[0];
            double k = Double.parseDouble(parts[1]);
            int w = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
            return new PlayerEntry(n, k, w);
        }

        String getName() {
            return name;
        }

        double getKarma() {
            return karma;
        }

        void addKarma(double delta) {
            karma += delta;
            if (karma > 5) {
                karma = 5;
            } else if (karma < -5) {
                karma = -5;
            }
        }

        String toLine() {
            return String.join(";",
                    name,
                    KARMA_FORMAT.format(karma),
                    String.valueOf(wins)
            );
        }
    }
}