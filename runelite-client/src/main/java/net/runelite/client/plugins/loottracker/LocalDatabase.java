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

package net.runelite.client.plugins.loottracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

@Slf4j
public class LocalDatabase
{

	private Connection connection = null;
	private ExecutorService executorService = Executors.newSingleThreadExecutor();


	public LocalDatabase()
	{
		try
		{
			DriverManager.registerDriver(new org.sqlite.JDBC());
			String url = RuneLite.RUNELITE_DIR + "\\localloot.db";
			connection = DriverManager.getConnection("jdbc:sqlite:" + url);
			createTables();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}

	public Collection<LootTrackerRecord> getAllRecords()
	{
		Collection<LootTrackerRecord> lootTrackerRecords = new ArrayList<>();
		executorService.submit(() ->
		{
			try
			{
				String sql = "SELECT id, title, subtitle, timestamp FROM loot_tracker_record";
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql);
				while (resultSet.next())
				{
					int id = resultSet.getInt("id");
					String title = resultSet.getString("title");
					String subtitle = resultSet.getString("subtitle");
					long timeStamp = resultSet.getLong("timestamp");
					ArrayList<LootTrackerItem> lootTrackerItems = new ArrayList<>();
					String items = "SELECT name, quantity, price, ignored, item_id "
						+ "FROM loot_tracker_item WHERE loot_tracker_record_id = ?";
					PreparedStatement preparedStatement = connection.prepareStatement(items);
					preparedStatement.setInt(1, id);
					ResultSet itemResults = preparedStatement.executeQuery();
					while (itemResults.next())
					{
						lootTrackerItems.add(new LootTrackerItem(itemResults.getInt("item_id"),
							itemResults.getString("name"),
							itemResults.getInt("quantity"), itemResults.getLong("price"),
							itemResults.getInt("ignored") != 0));
					}
					LootTrackerRecord lootTrackerRecord = new LootTrackerRecord(title, subtitle,
						lootTrackerItems.toArray(LootTrackerItem[]::new), timeStamp);
					lootTrackerRecords.add(lootTrackerRecord);
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			log.info("Loaded " + lootTrackerRecords.size() + " Loot Tracker Records from local DB");
		});

		return lootTrackerRecords;
	}

	public void insertLootRecord(LootTrackerRecord lootTrackerRecord)
	{
		executorService.submit(() ->
		{
			try
			{
				PreparedStatement preparedStatement = connection.prepareStatement("INSERT into" +
						" loot_tracker_record(title, subtitle, timestamp) VALUES (?,?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, lootTrackerRecord.getTitle());
				preparedStatement.setString(2, lootTrackerRecord.getSubTitle());
				preparedStatement.setLong(3, lootTrackerRecord.getTimestamp());
				preparedStatement.executeUpdate();
				ResultSet rs = preparedStatement.getGeneratedKeys();

				if (rs.next())
				{
					int recordID = rs.getInt(1);
					for (LootTrackerItem lootTrackerItem : lootTrackerRecord.getItems())
					{
						preparedStatement = connection.prepareStatement("INSERT into" +
							"  loot_tracker_item(loot_tracker_record_id, name, quantity, price, ignored, item_id)" +
							" VALUES(?,?,?,?,?,?)");
						preparedStatement.setInt(1, recordID);
						preparedStatement.setString(2, lootTrackerItem.getName());
						preparedStatement.setInt(3, lootTrackerItem.getQuantity());
						preparedStatement.setLong(4, lootTrackerItem.getPrice());
						preparedStatement.setBoolean(5, lootTrackerItem.isIgnored());
						preparedStatement.setInt(6, lootTrackerItem.getId());
						preparedStatement.executeUpdate();
					}
					log.info("Saved Loot Record" + recordID + " to local DB");
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		});
	}


	private void createTables() throws SQLException
	{
		String createRecordTable = "CREATE TABLE IF NOT EXISTS loot_tracker_record\n" +
			"(\n" +
			"\tid integer\n" +
			"\t\tconstraint loot_tracker_record_pk\n" +
			"\t\t\tprimary key autoincrement,\n" +
			"\ttitle text not null,\n" +
			"\tsubtitle text,\n" +
			"\ttimestamp integer\n" +
			");\n" +
			"\n";

		String createItemsTable = "CREATE TABLE IF NOT EXISTS loot_tracker_item " +
			"(loot_tracker_record_id integer not null references loot_tracker_record," +
			"name text not null, " +
			"int quantity, " +
			"long price, " +
			"boolean ignored);";


		executorService.submit(() ->
		{
			try
			{
				Statement statement = this.connection.createStatement();
				statement.execute(createRecordTable);
				log.info("Created Loot Tracker Records table. . .");
				statement.execute(createItemsTable);
				log.info("Created Loot Tracker Items table. . .");
				statement.closeOnCompletion();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		});
	}


}
