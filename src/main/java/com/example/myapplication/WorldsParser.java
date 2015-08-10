package com.example.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @autor Sergey Shustikov
 * (pandarium.shustikov@gmail.com)
 */
public class WorldsParser {

    private String response;
    private final static String WORLDS_TAG = "allAvailableWorlds";
    private final static String NAME_TAG = "name";
    private final static String MAP_URL = "mapURL";
    private final static String WORLD_STATUS_TAG = "worldStatus";
    private final static String COUNTRY_TAG = "country";
    private final static String LANGUAGE_TAG = "language";
    private final static String ID_TAG = "id";
    private final static String URL_TAG = "url";
    private int startIndex;
    private ArrayList<World> worlds;
    private boolean statusBegin;

    public WorldsParser(String response) {
        this.response = response;
    }

    public ArrayList<World> parse() {
        worlds = new ArrayList<>();
        response = response.replaceAll("\n", "");
        response = response.replaceAll("\t", "");
        extractBlocks(response);
        return worlds;
    }

    private boolean beginK, beginV;
    private boolean begK, begV;
    private String statusK = "", statusV = "";
    private boolean temp;

    private void parseBlocks(String string) {
        string = string.substring(1, string.length() - 1);
        Map<String, String> values = new HashMap<>();
        String key = "";
        String value = "";
        World world = new World();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            switch (c) {
                case '{':
                    statusBegin = true;
                    begK = true;
                    break;
                case '}':
                    statusBegin = false;
                    break;
                case '\"': {
                    if (statusBegin) {
                        if (!begK && !begV && !temp) {
                            begK = true;
                            break;
                        }

                        if (!temp) {
                            temp = true;
                            begK = false;
                            break;
                        }

                        if (!begK && !begV)
                            begV = true;
                        else {
                            begV = false;
                            if (statusK.equals("id")) {
                                world.worldStatus.id = statusV;
                            } else if (statusK.equals("description")) {
                                world.worldStatus.description = statusV;
                            }
                            statusK = new String();
                            statusV = new String();
                            key = new String();
                            value = new String();
                            beginK = false;
                            beginV = false;
                            temp = false;
                        }
                        break;
                    }
                    if (!beginK && !beginV && !temp) {
                        beginK = true;
                        break;
                    }

                    if (!temp) {
                        temp = true;
                        beginK = false;
                        break;
                    }

                    if (!beginK && !beginV)
                        beginV = true;
                    else {
                        beginV = false;
                        if (key.startsWith(NAME_TAG)) {
                            world.name = value;
                        } else if (key.startsWith(MAP_URL)) {
                            world.mapURL = value;
                        } else if (key.startsWith(COUNTRY_TAG)) {
                            world.country = value;
                        } else if (key.startsWith(LANGUAGE_TAG)) {
                            world.language = value;
                        } else if (key.startsWith(ID_TAG)) {
                            world.id = value;
                        } else if (key.startsWith(URL_TAG)) {
                            world.url = value;
                        }
                        values.put(key, value);
                        key = new String();
                        value = new String();
                        temp = false;
                    }
                    break;
                }
                default:
                    if (statusBegin) {
                        if (begK) {
                            statusK += string.charAt(i);
                        }
                        if (begV) {
                            statusV += string.charAt(i);
                        }
                        break;
                    }
                    if (beginK) {
                        key += string.charAt(i);
                    }
                    if (beginV) {
                        value += string.charAt(i);
                    }
            }
        }
        worlds.add(world);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }

    private String tmp;

    private void extractBlocks(String string) {
        string = string.substring(1, string.length() - 1);
        ArrayList<String> blocks = new ArrayList<>();
        int length = string.toCharArray().length;
        for (int i = 0; i < length; i++) {
            switch (string.charAt(i)) {
                case '{':
                    if (beginK) break;
                    startIndex = i;
                    beginK = true;
                    break;
                case '}':
                    if (string.charAt(i + 1) != ';' && i + 1 < length)
                        if (tmp.contains(NAME_TAG) && tmp.contains(MAP_URL)) {
                            String block = string.substring(startIndex, i + 1);
                            blocks.add(block);
                            tmp = "";
                            beginK = false;
                        }
                    break;
                default:
                    if (beginK)
                        tmp += string.charAt(i);
            }
        }

        parseBlocks(blocks);
    }

    private void parseBlocks(ArrayList<String> blocks) {
        for (String block : blocks) {
            parseBlocks(block);
        }
    }
}
