package net.sayusimp.islesaddons.util;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class EXPUtils {
    public static HashMap<String, Integer> fishXPMap = new LinkedHashMap<String, Integer>() {{
        put("Raw Sardine", 12);
        put("Raw Flounder", 18);
        put("Raw Stone Clam", 22);
        put("Raw Shrimp", 26);
        put("Raw Stone Crab", 30);
        put("Raw Cod", 36);
        put("Raw Oyster", 42);
        put("Raw Trout", 42);
        put("Raw Picklefish", 56);
        put("Raw Squid", 62);
        put("Raw Salmon", 78);
        put("Raw Clownfish", 85);
        put("Raw Perch", 72);
        put("Raw Catfish", 88);
        put("Raw Urchin", 95);
        put("Raw Pike", 92);
        put("Raw Seahorse", 105);
        put("Raw Pufferfish", 115);
        put("Raw Starfish", 123);
        put("Raw Giant Snail", 135);
        put("Raw Sea Snake", 150);
        put("Raw Tuna", 165);
        put("Raw Lava Eel", 175);
        put("Raw Giant Crab", 185);
        put("Raw Grubler", 192);
        put("Raw Manta Ray", 224);
        // put("Raw Octopus", );
        put("Raw Sea Turtle", 250);
        put("Raw Jellyfish", 250);
        // put("Raw Anglerfish", );
        // put("Raw Goliath Grouper", );
        // put("Raw Shark", );
    }};

    public static HashMap<String, Integer> cookingXPMap = new LinkedHashMap<String, Integer>() {{
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
        // put("Grilled Octopus", );
        put("Jellyfish Stew", 330);
        // put("Grilled Anglerfish", );
        // put("Grilled Goliath Grouper", );
        // put("Goliath Stew", );
        // put("Smoked Shark", );
        put("Volcanic Stew", 200);
        put("Carrot Cake Slice", 85);
        put("Banana Bread Slice", 105);
        put("Chocolate Chip Cookie Dough", 36);
        put("Chocolate Chip Cookie", 105);
        put("Cheese Pizza Slice", 104);
        put("Pancake Batter", 48);
        put("Maple Syrup Pancakes", 70);
        put("Pancakes", 125);
        put("Apple Pie Slice", 195);
        put("Pepperoni Pizza Slice", 175);
        put("Pumpkin Pie Slice", 235);
        put("Cherry Chocolate Cake Slice", 275);
        put("Grilled Rabbit", 14);
        put("Grilled Porkchop", 15);
        put("Grilled Beef", 21);
        put("Grilled Mutton", 36);
        put("Fried Frog Legs", 36);
        put("Bacon and Eggs", 50);
        put("Grilled Boar Steak", 55);
        put("Escargot", 85);
        put("Grilled Bear Steak", 80);
        put("Fried Chicken", 32);
        put("Grilled Duck", 24);
        put("Grilled Seagull", 26);
        // put("Parrot Stew", );
        put("Baked Potato", 18);
        put("Grilled Cheese", 35);
        put("Grilled Brumblebulb", 52);
        put("Tomato Soup", 35);
        put("Roasted Beet", 48);
        put("Corn on the Cob", 50);
        put("Apple Juice", 100);
        put("Carrot Juice", 125);
        put("Orange Juice", 140);
        put("Bamboo Pulp", 75);
        put("Pyre Paste", 60);
        put("Berry Blast Smoothie", 175);
        put("Tropical Smoothie", 200);
        put("Mango Juice", 185);
        put("Mandrake Tea", 215);
        put("Meat Sludge", 275);
        put("Chocolate", 25);
        put("Bread", 12);
        put("Dough", 7);
        put("Flour", 8);
        put("Butter", 18);
        put("Cheese", 24);
        put("Sugar", 32);
        put("Pepperoni", 185);
    }};

    public static HashMap<String, Integer> foragingXPMap = new LinkedHashMap<String, Integer>() {{
        put("Oak Log", 30);
        put("Birch Log", 40);
        put("Spruce Log", 50);
        put("Willow Log", 75);
        put("Acacia Log", 85);
        put("Dreadknot Log", 110);
        put("Mahogany Log", 125);
        put("Baobab Log", 150);
        put("Bedlam Log", 275);
        put("Demon Log", 350);
        put("Oak Bark", 30);
        put("Birch Bark", 40);
        put("Spruce Bark", 50);
        put("Willow Bark", 75);
        put("Acacia Bark", 85);
        put("Dreadknot Bark", 110);
        put("Baobab Bark", 150);
        put("Bedlam Bark", 275);
        put("Demon Bark", 350);
        put("Rough Amber", 0);
        put("Log", 15);
        put("Acorn", 0);
        put("Dark Rune", 0); // dreadknot trees drop dark runes too
    }};

    public static HashMap<String, Integer> carvingXPMap = new LinkedHashMap<String, Integer>() {{
        put("Oak Handle", 35);
        put("Birch Handle", 50);
        put("Spruce Handle", 65);
        put("Willow Handle", 90);
        put("Acacia Handle", 120);
        put("Dreadknot Handle", 150);
        put("Baobab Handle", 215);
        put("Bedlam Handle", 375);
        put("Demon Handle", 625);
    }};

    public static HashMap<String, Integer> farmingXPMap = new LinkedHashMap<String, Integer>() {{
        put("Apple", 8);
        put("Wheat", 12);
        put("Orange", 16);
        put("Lettuce", 16);
        put("Potato", 18);
        put("Flax", 20);
        put("Banana", 24);
        put("Nettle Leaf", 24);
        put("Tomato", 30);
        put("Cherry", 36);
        put("Carrot", 36);
        put("Blackberry", 48);
        put("Sugarcane", 48);
        put("Brumblebulb", 52);
        put("Cocoa Bean", 56);
        put("Coconut", 60);
        put("Viking Hops", 60);
        put("Beet", 65);
        put("Corn", 75);
        put("Buglewart", 81);
        put("Garlic", 85);
        put("Bamboo", 95);
        put("Pyre Pepper", 125);
        put("Pumpkin", 150);
        // put("Mango, );
        // put("Volcanic Hop", );
        // put("Ghost Berry", );
        // put("Nether Wart", );
        // put("Mandrake", );
    }};

    public static HashMap<String, Integer> miningXPMap = new LinkedHashMap<String, Integer>() {{
        put("Tin Ore", 12);
        put("Copper Ore", 12);
        put("Clay", 18);
        put("Iron Ore", 24);
        put("Sand", 26);
        put("Rune Essence", 30);
        put("Coal", 35);
        put("Nickel Ore", 45);
        put("Silver Ore", 65);
        put("Rhodonite Ore", 125);
        put("Salt Chunk", 75);
        put("Mithril Ore", 185);
        put("Ice", 90);
        // put("Granite Slab", );
        // put("Cannonball", );
        put("Adamantium Ore", 215);
        // put("Cobalt Ore", );
        put("Gold Ore", 150);
        put("Crimson Ore", 265);
        put("Obsidian Ore", 325);
        // put("Spectral Chunk", );
        // put("Netherite Ore", );
        // put("Blightstone Chunk", );
        put("Rough Lapis", 0);
        put("Rough Jade", 0);
        put("Rough Amber", 0);
        put("Rough Pearl", 0);
        put("Rough Sapphire", 0);
        put("Rough Diamond", 0);
        put("Rough Emerald", 0);
        put("Rough Amethyst", 0);
        put("Rough Ruby", 0);
        put("Rough Celestial", 0);
    }};

    public static HashMap<String, Integer> smeltingXPMap = new LinkedHashMap<String, Integer>() {{
        put("Bronze Bar", 20);
        put("Iron Bar", 30);
        put("Molten Glass", 20);
        put("Steel Bar", 100);
        put("Silver Bar", 50);
        put("Rhodonite Bar", 110);
        put("Mithril Bar", 150);
        put("Adamantium Bar", 200);
        put("Gold Bar", 125);
        put("Crimson Bar", 250);
        // put("Obsidian Bar", );
        // put("Spectral Bar", );
        // put("Netherite Bar", );
        // put("Blightstone Bar", );
    }};

    public static HashMap<String, Integer> artisanXPMap = new LinkedHashMap<String, Integer>() {{

        put("Fishing Line", 50);
        put("Enchanted Fishing Line", 75);
        put("Radiant Fishing Line", 100);
        put("Seer Fishing Line", 125);
        put("Undead Fishing Line", 175);
        // put("Dire Fishing Line", );
        // put("Phantom Fishing Line", );
        // put("Arcane Fishing Line", );
        // put("Mystic Fishing Line", );

        put("Enchanted Yarn", 40);
        put("Radiant Yarn", 65);
        put("Seer Yarn", 90);
        put("Undead Yarn", 135);
        // put("Dire Yarn", );
        // put("Phantom Yarn", );
        // put("Arcane Yarn", );
        // put("Mystic Yarn", );
        put("Bamboo String", 90);
        put("Yarn", 20);
        put("Bowstring", 25);
        put("Straw", 30);
        put("Rope", 75);

        put("Tanned Rabbit Hide", 20);
        put("Tanned Cow Hide", 40);
        put("Tanned Fox Hide", 60);
        put("Tanned Boar Hide", 80);
        put("Tanned Camel Hide", 110);
        // put("Tanned Bear Hide", );
        // put("Tanned Zoar Hide", );
        // put("Tanned Unicorn Hide", );
        // put("Tanned Manticore Hide", );

        put("Oak Handle", 27);
        put("Birch Handle", 50);
        put("Spruce Handle", 65);
        put("Willow Handle", 90);
        put("Acacia Handle", 120);

        put("Lapis Lazuli Ring", 75);
        put("Jade Ring", 90);
        put("Amber Ring", 120);
        put("Pearl Ring", 165);
        // put("Sapphire Ring", );
        // put("Diamond Ring", );
        // put("Emerald Ring", );
        // put("Ruby Ring", );
        // put("Celestial Ring", );

        put("Lapis Lazuli Necklace", 100);
        put("Jade Necklace", 140);
        put("Amber Necklace", 170);
        put("Pearl Necklace", 215);
        // put("Sapphire Necklace", );
        // put("Diamond Necklace", );
        // put("Emerald Necklace", );
        // put("Ruby Necklace", );
        // put("Celestial Necklace", );

        put("Lapis Lazuli Amulet", 125);
        put("Jade Amulet", 180);
        // put("Amber Amulet", );
        put("Pearl Amulet", 255);
        // put("Sapphire Amulet", );
        // put("Diamond Amulet", );
        // put("Emerald Amulet", );
        // put("Ruby Amulet", );
        // put("Celestial Amulet", );

        put("Lapis Fishing Net", 200);
        put("Jade Fishing Net", 275);
        put("Amber Fishing Net", 350);
        put("Pearl Fishing Net", 425);
        // put("Sapphire Fishing Net", );
        // put("Diamond Fishing Net", );
        // put("Emerald Fishing Net", );
        // put("Ruby Fishing Net", );
        // put("Celestial Fishing Net", );

        put("Lapis Lazuli", 75);
        put("Jade", 100);
        put("Amber", 125);
        put("Pearl", 175);
        put("Sapphire", 225);
        // put("Diamond", );
        // put("Emerald", );
        // put("Amethyst", );
        // put("Ruby", );
        // put("Celestial", );

        put("Ring Mold", 15);
        put("Necklace Mold", 45);
        put("Amulet Mold", 60);
        put("Magic Tablet", 60);
        // put("Fishing Lure Mold", );

        put("Bowl", 15);
        put("Pan", 18);
        put("Brick", 36);
        put("Pot", 36);

        put("Golden Lure", 140); // gives 8
        put("Glass Bottle", 25);
        put("Glass Pane", 75);
        put("Empty Orb", 180);
        put("Lure", 4);


    }};
}
