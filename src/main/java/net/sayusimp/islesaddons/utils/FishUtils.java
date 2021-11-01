package net.sayusimp.islesaddons.utils;

import java.util.HashMap;

public class FishUtils {

    public static HashMap<String, Integer> fishXPMap = new HashMap<String, Integer>() {{
        put("Raw Sardine", 12);
        put("Raw Flounder", 18);
        put("Raw Stone Clam", 22);
        put("Raw Shrimp", 26);
        put("Raw Stone Crab", 30);
        put("Raw Cod", 36);
        put("Raw Oyster", 42);
        put("Raw Trout", 48);
        put("Raw Picklefish", 56);
        put("Raw Squid", 62);
        put("Raw Salmon", 66);
        put("Raw Clownfish", 69);
        put("Raw Perch", 72);
        put("Raw Catfish", 82);
        put("Raw Urchin", 84);
        put("Raw Pike", 92);
        put("Raw Seahorse", 96);
        put("Raw Pufferfish", 105);
        put("Raw Starfish", 111);
        put("Raw Giant Snail", 135);
        put("Raw Sea Snake", 150);
        put("Raw Tuna", 165);
        put("Raw Lava Eel", 175);
        put("Raw Giant Crab", 185);
        put("Raw Grubler", 192);
        put("Raw Sea Turtle", 200);
        put("Raw Manta Ray", 224);
    }};

    public static HashMap<String, Integer> seafoodXPMap = new HashMap<String, Integer>() {{
        put("Grilled Sardine", 12);
        put("Stone Chum", 16);
        put("Grilled Flounder", 18);
        put("Grilled Stone Clam", 22);
        put("Fried Shrimp", 26);
        put("Boiled Stone Crab", 30);
        put("Inedible Fugu", 35);
        put("Grilled Cod", 36);
        put("Oyster", 42);
        put("Grilled Trout", 48);
        put("Cod Fish Sandwich", 50);
        put("Calamari", 62);
        put("Smoked Salmon", 66);
        put("Grilled Catfish", 71);
        put("Fried Perch", 72); // 1.3
        put("Picklefish Stew", 80);
        put("Smoked Pike", 92); // 1.3
        put("Fugu", 125);
        put("Seahorse Stew", 128);
        put("Fried Sea Snake", 135);
        put("Grilled Tuna", 150);
        put("Uni", 154);
        put("Charred Lava Eel", 170);
        put("Boiled Giant Crab", 185);
        put("Grilled Grubler", 195); // 1.3
        put("Smoked Sea Turtle", 200);
        put("Giant Snail Stew", 205);
        put("Starfish Pie Slice", 225);
        put("Smoked Manta Ray", 225);
    }};

    public static String getColorFromAmount(int amount)
    {
        return null;
    }



}
