package com.ilo.ezh;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final int suitHeight = 36;
    private static final int suitWidth = 33;
    private static final int valueHeight = 26;
    private static final int valueWidth = 31;
    private static final Color GRAYCOLOR = new Color(120, 120, 120);
    private static final Color WHITECOLOR = Color.white;
    private static final int[][] suitsCoordinates = {{168, 632}, {240, 632}, {311, 632}, {383, 632}, {455, 632}};
    private static final int[][] valueCCoordinates = {{147, 590}, {218, 590}, {290, 590}, {361, 590}, {433, 590}};
    private static final Map<Suit, byte[][]> suitsMap = new EnumMap<>(Suit.class);
    private static final Map<Value, byte[][]> valueMap = new EnumMap<>(Value.class);

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("Error! The path to the folder with images is not specified!");
            return;
        }
        String dirWithPictures = args[0];
        init();
        Files.walk(Paths.get(dirWithPictures))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".png"))
                .forEach(Main::printValueAndSuitOfCardFromImage);

    }

    private static void printValueAndSuitOfCardFromImage(Path path) {

        StringBuilder result = new StringBuilder(path.getFileName().toString() + " - ");
        String suitString;
        String valueString;
        BufferedImage subImage;
        int diffToWhiteSpace = 18;
        int score;
        Set<Map.Entry<Suit, byte[][]>> suitMapEntry = suitsMap.entrySet();
        Set<Map.Entry<Value, byte[][]>> valueMapEntry = valueMap.entrySet();

        try {

            BufferedImage BigPicture = ImageIO.read(path.toFile());
            for (int i = 0; i < 5; i++) {
                suitString = "UnkSuit";
                valueString = "UnkValue";
                int maxScore = 0;

                Color color = new Color(BigPicture.getRGB(suitsCoordinates[i][0] - diffToWhiteSpace, suitsCoordinates[i][1]));
                if (!color.equals(GRAYCOLOR) && !color.equals(WHITECOLOR)) {
                    break;
                }
                subImage = BigPicture.getSubimage(valueCCoordinates[i][0], valueCCoordinates[i][1], valueWidth, valueHeight);
                for (Map.Entry<Value, byte[][]> e : valueMapEntry) {

                    score = getScore(e.getValue(), getArrayFromPicture(valueWidth, valueHeight, subImage), valueWidth, valueHeight);

                     if (score > maxScore) {
                        valueString = e.getKey().toString();
                        maxScore = score;
                    }

                }

                maxScore = 0;
                subImage = BigPicture.getSubimage(suitsCoordinates[i][0], suitsCoordinates[i][1], suitWidth, suitHeight);
                for (Map.Entry<Suit, byte[][]> e : suitMapEntry) {

                    score = getScore(e.getValue(), getArrayFromPicture(suitWidth, suitHeight, subImage), suitWidth, suitHeight);
                    if (score > maxScore) {
                        suitString = e.getKey().toString();
                        maxScore = score;
                    }
                }

                result.append(valueString).append(suitString);

            }

        } catch (IOException e) {
            throw new RuntimeException("Can't processing the file " + path, e);
        }

        System.out.println(result);

    }

    private static int getScore(byte[][] value, byte[][] arrayFromPicture, int width, int height) {

        int sum = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (value[x][y] == arrayFromPicture[x][y]) {
                    sum++;
                }
            }
        }
        return sum;
    }

    private static void init() throws IOException {

        String resources = "/res/";
        addValueToMapFromPicture(suitsMap, Suit.SPADE, resources, "s.png", suitWidth, suitHeight);
        addValueToMapFromPicture(suitsMap, Suit.DIAMONDS, resources, "d.png", suitWidth, suitHeight);
        addValueToMapFromPicture(suitsMap, Suit.CLUB, resources, "c.png", suitWidth, suitHeight);
        addValueToMapFromPicture(suitsMap, Suit.HEARTS, resources, "h.png", suitWidth, suitHeight);

        addValueToMapFromPicture(valueMap, Value.TWO, resources, "2.png", valueWidth, valueHeight);
        addValueToMapFromPicture(valueMap, Value.THREE, resources, "3.png", valueWidth, valueHeight);
        addValueToMapFromPicture(valueMap, Value.FOUR, resources, "4.png", valueWidth, valueHeight);
        addValueToMapFromPicture(valueMap, Value.FIVE, resources, "5.png", valueWidth, valueHeight);
        addValueToMapFromPicture(valueMap, Value.SIX, resources, "6.png", valueWidth, valueHeight);
        addValueToMapFromPicture(valueMap, Value.SEVEN, resources, "7.png", valueWidth, valueHeight);
        addValueToMapFromPicture(valueMap, Value.EIGHT, resources, "8.png", valueWidth, valueHeight);
        addValueToMapFromPicture(valueMap, Value.NINE, resources, "9.png", valueWidth, valueHeight);
        addValueToMapFromPicture(valueMap, Value.TEN, resources, "10.png", valueWidth, valueHeight);
        addValueToMapFromPicture(valueMap, Value.JACK, resources, "J.png", valueWidth, valueHeight);
        addValueToMapFromPicture(valueMap, Value.QUEEN, resources, "Q.png", valueWidth, valueHeight);
        addValueToMapFromPicture(valueMap, Value.KING, resources, "K.png", valueWidth, valueHeight);
        addValueToMapFromPicture(valueMap, Value.ACE, resources, "A.png", valueWidth, valueHeight);

    }

    private static void addValueToMapFromPicture(Map map, Object object, String resources, String s, int Width, int Height) throws IOException {

        BufferedImage img = ImageIO.read(Main.class.getResource(resources + s));
        byte[][] newarray = getArrayFromPicture(Width, Height, img);
        map.put(object, newarray);

    }

    private static byte[][] getArrayFromPicture(int suitWidth, int suitHeight, BufferedImage img) {

        byte[][] newarray = new byte[suitWidth][suitHeight];
        for (int x = 0; x < suitWidth; x++) {
            for (int y = 0; y < suitHeight; y++) {
                Color color = new Color(img.getRGB(x, y));
                if (!color.equals(GRAYCOLOR) && !color.equals(WHITECOLOR)) {
                    newarray[x][y] = 1;
                }
            }
        }
        return newarray;
    }

}