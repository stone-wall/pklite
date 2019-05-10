

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

package net.runelite.client.plugins.pvptools;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.runelite.api.Client;
import net.runelite.client.ui.FontManager;

public class MissingPlayersJFrame extends JFrame
{

	private final JList missingPlayersJList;

	MissingPlayersJFrame(Client client, PvpToolsPlugin pvpToolsPlugin, List<String> list)
	{
		super();
		int x = client.getCanvas().getLocationOnScreen().x + client.getCanvas().getWidth();
		int y = client.getCanvas().getLocationOnScreen().y;
		JPanel scrollContainer = new JPanel(new BorderLayout());

		JScrollPane jScrollPane = new JScrollPane(scrollContainer);
		JButton refreshJButton = new JButton("Refresh");
		refreshJButton.addActionListener(pvpToolsPlugin.playersButtonActionListener);
		JButton copyJButton = new JButton("Copy List");
		missingPlayersJList = new JList(list.toArray());
		ActionListener copyButtonActionListener = e ->
		{
			StringSelection stringSelection;
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringBuilder stringBuilder = new StringBuilder();
			list.forEach(s ->
			{
				stringBuilder.append(s);
				stringBuilder.append(System.getProperty("line.separator"));
			});
			stringSelection = new StringSelection(stringBuilder.toString());
			clipboard.setContents(stringSelection, stringSelection);
		};
		copyJButton.addActionListener(copyButtonActionListener);
		this.setTitle("Missing CC Members");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JLabel titleLabel = new JLabel("Missing CC Members");
		titleLabel.setFont(FontManager.getRunescapeFont().deriveFont(Font.BOLD, 18));
		missingPlayersJList.setFont(new Font("Arial", Font.PLAIN, 14));
		scrollContainer.add(refreshJButton, BorderLayout.NORTH);
		scrollContainer.add(titleLabel, BorderLayout.CENTER);
		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.add(missingPlayersJList, BorderLayout.NORTH);
		footerPanel.add(copyJButton, BorderLayout.CENTER);
		scrollContainer.add(footerPanel, BorderLayout.SOUTH);
		this.add(jScrollPane);
		this.setLocation(x, y);
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.pack();
		this.setVisible(true);
	}
}
