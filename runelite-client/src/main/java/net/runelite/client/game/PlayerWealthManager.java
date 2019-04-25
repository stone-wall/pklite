/*
 *
 *  Copyright (c) 2019. PKLite
 *   Redistributions and modifications of this software are permitted as long as this notice remains in its original unmodified state at the top of this file. If there are any questions, comments, or feedback about this software, please direct all inquiries directly to the following authors.
 *   PKLite discord: https://discord.gg/Dp3HuFM
 *  Written by PKLite(ST0NEWALL, others) <stonewall@stonewall@pklite.xyz>, 2019
 *
 */

package net.runelite.client.game;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.Player;
import net.runelite.api.PlayerComposition;
import net.runelite.api.kit.KitType;


@Singleton
@Slf4j
public class PlayerWealthManager
{
	public ConcurrentHashMap<String, HashMap<Integer, Integer>>
		wealthMap = new ConcurrentHashMap<>();

	private final ItemManager itemManager;
	private final Client client;

	@Inject
	public PlayerWealthManager(ItemManager itemManager, Client client)
	{
		this.itemManager = itemManager;
		this.client = client;
	}

	public void addPlayerToWealthMap(Player player)
	{
		if (wealthMap.containsKey(player.getName()))
		{
			updatePlayerWealth(player, client);
			return;
		}
		PlayerComposition playerComposition = player.getPlayerComposition();
		HashMap<Integer, Integer> itemMap = new HashMap<>();
		for (KitType kitType : KitType.values())
		{
			ItemComposition itemComposition = itemManager.getItemComposition(playerComposition.getEquipmentId(kitType));
			itemMap.put(itemComposition.getId(), itemManager.getItemPrice(itemComposition.getId()));
		}
		wealthMap.put(player.getName(), itemMap);
	}

	public boolean updatePlayerWealth(Player player, Client client)
	{
		if (!wealthMap.containsKey(player.getName()))
		{
			addPlayerToWealthMap(player);
			return false;
		}
		if (wealthMap.containsKey(player.getName()))
		{
			HashMap<Integer, Integer> oldData = wealthMap.remove(player.getName());
			PlayerComposition playerComposition = player.getPlayerComposition();
			for (KitType kitType : KitType.values())
			{
				ItemComposition itemComposition = itemManager.getItemComposition(playerComposition.getEquipmentId(kitType));
				if (!oldData.containsKey(itemComposition.getId()))
				{
					oldData.put(itemComposition.getId(), itemManager.getItemPrice(itemComposition.getId()));
				}
			}
			wealthMap.put(player.getName(), oldData);
			return true;
		}
		return false;
	}

	public int getPlayerWealth(Player player)
	{
		if (!wealthMap.containsKey(player.getName()))
		{
			return 0;
		}
		HashMap<Integer, Integer> money = wealthMap.get(player.getName());
		money.entrySet().forEach(integerIntegerEntry ->
		{
			log.info(itemManager.getItemComposition(integerIntegerEntry.getKey()).getName());
			//log.info(player.getName() + ": " + integerIntegerEntry.getKey() + " : " + integerIntegerEntry.getValue());

		});
		return money.values().stream().mapToInt(Integer::intValue).sum();
	}


}
