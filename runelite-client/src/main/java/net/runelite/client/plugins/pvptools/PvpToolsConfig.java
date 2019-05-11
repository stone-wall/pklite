

/*******************************************************************************
 * Copyright (c) 2019. PKLite
 * @see <a href="https://pklite.xyz>pklite</a>
 *  Redistributions and modifications of this software are permitted as long as this notice remains in its
 *  original unmodified state at the top of this file.  If there are any questions comments, or feedback
 *  about this software, please direct all inquiries directly to the following authors:
 *
 *   PKLite discord: https://discord.gg/Dp3HuFM
 *   Written by PKLite(ST0NEWALL, others) <stonewall@pklite.xyz>, 2019
 *
 ******************************************************************************/

package net.runelite.client.plugins.pvptools;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup("pvptools")
public interface PvpToolsConfig extends Config
{
	@ConfigItem(
		keyName = "countPlayers",
		name = "Count Players",
		description = "When in PvP zones, counts the attackable players in and not in player's CC",
		position = 3
	)
	default boolean countPlayers()
	{
		return true;
	}

	@ConfigItem(
		keyName = "countOverHeads",
		name = "Count Enemy Overheads",
		description = "Counts the number of each protection prayer attackable targets not in your CC are currently" +
			" using",
		position = 4
	)
	default boolean countOverHeads()
	{
		return true;
	}

	@ConfigItem(
		keyName = "renderSelfHotkey",
		name = "Render Self Hotkey",
		description = "Toggles renderself when you press the hotkey",
		position = 7
	)
	default Keybind renderSelf()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "attackOptionsClan",
		name = "Move CC Attack Option",
		description = "Moves the attack option for people in the same CC",
		position = 8,
		group = "Right-Click Attack Options"
	)
	default boolean attackOptionsClan()
	{
		return false;
	}

	@ConfigItem(
		keyName = "attackOptionsFriend",
		name = "Move Friend Attack Options",
		description = "Moves the attack option for people on your friends list",
		position = 9,
		group = "Right-Click Attack Options"
	)
	default boolean attackOptionsFriend()
	{
		return false;
	}

	@ConfigItem(
			keyName = "levelRangeAttackOptions",
			name = "Moves Other Attack Options",
			description = "Moves the attack option for people that are outside your level range",
			position = 10,
			group = "Right-Click Attack Options"
	)
	default boolean levelRangeAttackOptions()
	{
		return false;
	}

	@ConfigItem(
		keyName = "riskCalculator",
		name = "Risk Calculator",
		description = "Enables a panel in the PvP Tools Panel that shows the players current risk",
		position = 14
	)
	default boolean riskCalculatorEnabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "missingPlayers",
		name = "Missing CC Players",
		description = "Adds a button to the PvP Tools panel that opens a window showing which CC members are not at" +
				" the current players location",
		position = 15
	)
	default boolean missingPlayersEnabled() { return true; }

	@ConfigItem(
			keyName = "currentPlayers",
			name = "Current CC Players",
			description = "Adds a button to the PvP Tools panel that opens a window showing which CC members currently at" +
					" the players location",
			position = 16
	)
	default boolean currentPlayersEnabled()
	{
		return true;
	}

}
