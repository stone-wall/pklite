package net.runelite.client.plugins.hideprayers;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("hideprayers")
public interface HidePrayersConfig extends Config
{
    @ConfigItem(
            position = 0,
            keyName = "pk prayers",
            name = "Hides none pk prayers",
            description = "Hides widget icons."
    )
    default boolean showPrayers() { return false; }
    
    @ConfigItem(
    	position = 1,
    	keyName = "eagle/mystic",
    	name = "Shows eagle/mystic prayers not rigour/augury",
    	description = "Hides widget icons."
    )
    default boolean showEagleMystic() { return false; }

    @ConfigItem(
            position = 2,
            keyName = "Ultimate Strength",
            name = "Shows Ultimate Strength prayers not Piety",
            description = "Hides widget icons."
    )
    default boolean showUltStrength() { return false; }
}

