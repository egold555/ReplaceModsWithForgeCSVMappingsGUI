package org.golde.java.rmwfcsvmg.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.golde.java.rmwfcsvmg.EnumForgeVersion;
import org.golde.java.rmwfcsvmg.Main;
import org.golde.java.rmwfcsvmg.ThreadReplaceCSV;
import org.golde.java.rmwfcsvmg.utils.StringUtils;
import javax.swing.JProgressBar;

@SuppressWarnings({"serial", "rawtypes", "unchecked"}) //Ugg java D:
public class PanelMain extends JPanel{

	private EnumForgeVersion selectedForgeVersion = EnumForgeVersion.v1_7_10;
	public JButton btnGo;
	public JComboBox comboBoxForgeVersion;
	private JTextField textFieldCustomMCPSnapshotVersion;
	private JTextField textFieldCustomMCPMinecraftVersion;
	private JLabel lblMCPVersion;
	private JLabel lblMinecraftVersion;
	private JLabel lblModToApply;
	public JButton btnSelectMod;
	private File selectedFile = null;
	public JProgressBar progressBar;

	public PanelMain() {

		textFieldCustomMCPSnapshotVersion = new JTextField();
		textFieldCustomMCPSnapshotVersion.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				updateGoButton();
			}
		});

		textFieldCustomMCPSnapshotVersion.setBounds(196, 113, 96, 22);
		textFieldCustomMCPSnapshotVersion.setVisible(false);
		add(textFieldCustomMCPSnapshotVersion);
		textFieldCustomMCPSnapshotVersion.setColumns(10);

		textFieldCustomMCPMinecraftVersion = new JTextField();
		textFieldCustomMCPMinecraftVersion.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				updateGoButton();
			}
		});

		textFieldCustomMCPMinecraftVersion.setBounds(196, 81, 96, 22);
		textFieldCustomMCPMinecraftVersion.setVisible(false);
		add(textFieldCustomMCPMinecraftVersion);
		textFieldCustomMCPMinecraftVersion.setColumns(10);

		lblMCPVersion = new JLabel("MCP Version");
		lblMCPVersion.setBounds(80, 84, 77, 16);
		lblMCPVersion.setVisible(false);
		add(lblMCPVersion);

		lblMinecraftVersion = new JLabel("Minecraft Version");
		lblMinecraftVersion.setBounds(80, 116, 104, 16);
		lblMinecraftVersion.setVisible(false);
		add(lblMinecraftVersion);

		btnGo = new JButton("GO!");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent action) {

				pressedGoButton();

			}

		});
		btnGo.setEnabled(false);
		btnGo.setBounds(149, 178, 97, 25);
		add(btnGo);


		comboBoxForgeVersion = new JComboBox();
		comboBoxForgeVersion.setBounds(196, 13, 96, 22);
		comboBoxForgeVersion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent action) {

				if(action.getID() == ActionEvent.ACTION_PERFORMED) {
					selectedForgeVersion = EnumForgeVersion.get((String)comboBoxForgeVersion.getSelectedItem());

					updateGoButton();
				}

			}
		});
		setLayout(null);

		comboBoxForgeVersion.setModel(new DefaultComboBoxModel(EnumForgeVersion.valuesNice()));
		add(comboBoxForgeVersion);

		JLabel lblForgeVersion = new JLabel("Forge Version");
		lblForgeVersion.setBounds(80, 16, 96, 16);
		add(lblForgeVersion);

		lblModToApply = new JLabel("Mod to apply to");
		lblModToApply.setBounds(79, 52, 104, 16);
		add(lblModToApply);

		btnSelectMod = new JButton("Select Mod");
		final PanelMain stupidWorkaroundCauseJava = this;
		btnSelectMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser(Main.FILE_MAIN);
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.jar", "jar"));
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.zip", "zip"));
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.class", "class"));
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setMultiSelectionEnabled(false);

				//
				fileChooser.showOpenDialog(stupidWorkaroundCauseJava);
				selectedFile = fileChooser.getSelectedFile();
				updateGoButton();
			}


		});
		btnSelectMod.setBounds(195, 48, 97, 25);
		add(btnSelectMod);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(12, 216, 376, 14);
		add(progressBar);

	}

	private void updateGoButton() {
		boolean shouldGoButtonBeEnabled = true;
		if(selectedForgeVersion == EnumForgeVersion.Snapshot) {
			if(!StringUtils.isStringEmpty(textFieldCustomMCPSnapshotVersion.getText()) && !StringUtils.isStringEmpty(textFieldCustomMCPMinecraftVersion.getText())) {
				EnumForgeVersion.Snapshot.setCustom(textFieldCustomMCPSnapshotVersion.getText(), textFieldCustomMCPMinecraftVersion.getText());
			}else {
				shouldGoButtonBeEnabled = false;
			}
			lblMCPVersion.setVisible(true);
			lblMinecraftVersion.setVisible(true);
			textFieldCustomMCPSnapshotVersion.setVisible(true);
			textFieldCustomMCPMinecraftVersion.setVisible(true);
		}else {
			lblMCPVersion.setVisible(false);
			lblMinecraftVersion.setVisible(false);
			textFieldCustomMCPSnapshotVersion.setVisible(false);
			textFieldCustomMCPMinecraftVersion.setVisible(false);
		}

		if(selectedFile == null || !selectedFile.exists() || selectedFile.isDirectory()) {
			shouldGoButtonBeEnabled = false;
		}

		btnGo.setEnabled(shouldGoButtonBeEnabled);
	}

	private void pressedGoButton() {
		new ThreadReplaceCSV(selectedForgeVersion, selectedFile, this);
	}
}
