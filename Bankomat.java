import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Bankomat extends JFrame {
	private CardLayout cardLayout;
	private JPanel mainPanel;
	private ImageIcon bankIcon;
	private File currentFile;
	private Map<String, Account> accountData = new HashMap<>();

	private boolean dataLoaded = false;
	private String currentCardNumber;

	private String fontName = "Arial";
	private int fontSize = 20;

	public Bankomat() {
		setTitle("Bankomat");
		setSize(800, 600);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		bankIcon = new ImageIcon("logo.png");
		setIconImage(bankIcon.getImage());

		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);

		JPanel insertCardPanel = createInsertCardPanel();
		JPanel pinPanel = createPinPanel();
		JPanel homePanel = createHomePanel();
		JPanel depositPanel = createDepositPanel();
		JPanel withdrawPanel = createWithdrawPanel();
		JPanel transferPanel = createTransferPanel();
		JPanel balancePanel = createBalancePanel();
		JPanel currencyCalculatorPanel = createCurrencyCalculatorPanel();

		mainPanel.add(insertCardPanel, "Card");
		mainPanel.add(pinPanel, "Pin");
		mainPanel.add(homePanel, "Home");
		mainPanel.add(depositPanel, "Deposit");
		mainPanel.add(withdrawPanel, "Withdraw");
		mainPanel.add(transferPanel, "Transfer");
		mainPanel.add(balancePanel, "Balance");
		mainPanel.add(currencyCalculatorPanel, "Calculator");

		add(mainPanel);

		cardLayout.show(mainPanel, "Card");

		createMenuBar();

		setVisible(true);
	}

	private JPanel createInsertCardPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel logoLabel = new JLabel(bankIcon);
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(logoLabel, gbc);

		JLabel messageLabel = createLabel("Witaj w bankomacie! Wsuń swoją kartę");
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(messageLabel, gbc);

		JTextField cardNumberField = createTextField(5);
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(cardNumberField, gbc);

		JButton insertCardButton = createButton("Wsuń kartę");
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(insertCardButton, gbc);

		/* insertCardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { */
		insertCardButton.addActionListener(e -> {
			if (dataLoaded) {
				currentCardNumber = cardNumberField.getText();
				if (accountData.containsKey(currentCardNumber)) {
					System.out.println("Wsunięto kartę: " + currentCardNumber);
					cardLayout.show(mainPanel, "Pin");
				} else {
					showError("Nieprawidłowy numer karty");
				}
			} else {
				showError("Nie wczytano danych użytkowników");
			}
		});

		return panel;
	}

	private JPanel createPinPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel logoLabel = new JLabel(bankIcon);
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(logoLabel, gbc);

		JLabel messageLabel = createLabel("Wprowadź 4-cyfrowy PIN");
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(messageLabel, gbc);

		JPasswordField pinField = createPasswordField(4);
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(pinField, gbc);

		pinField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (pinField.getPassword().length == 4) {
					String enteredPin = new String(pinField.getPassword());
					String correctPin = accountData.get(currentCardNumber).getPin();

					if (enteredPin.equals(correctPin)) {
						System.out.println("Wpisano poprawny PIN");
						cardLayout.show(mainPanel, "Home");
					} else {
						System.out.println("Wpisano nieprawidłowy PIN");
						showError("Nieprawidłowy PIN");
						pinField.setText("");
					}
				}
			}
		});

		return panel;
	}

	private JPanel createHomePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel logoLabel = new JLabel(bankIcon);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(logoLabel, gbc);

		JButton depositButton = createButton("Wpłać");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		panel.add(depositButton, gbc);

		JButton withdrawButton = createButton("Wypłać");
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(withdrawButton, gbc);

		JButton transferButton = createButton("Przelej na konto");
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(transferButton, gbc);

		JButton balanceButton = createButton("Sprawdź saldo");
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(balanceButton, gbc);

		JButton currencyCalculatorButton = createButton("Kalkulator walut");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		panel.add(currencyCalculatorButton, gbc);

		depositButton.addActionListener(e -> {
			System.out.println("Wpłata");
			cardLayout.show(mainPanel, "Deposit");
		});

		withdrawButton.addActionListener(e -> {
			System.out.println("Wypłata");
			cardLayout.show(mainPanel, "Withdraw");
		});

		transferButton.addActionListener(e -> {
			System.out.println("Przelew");
			cardLayout.show(mainPanel, "Transfer");
		});

		balanceButton.addActionListener(e -> {
			System.out.println("Saldo");
			cardLayout.show(mainPanel, "Balance");
		});

		currencyCalculatorButton.addActionListener(e -> {
			System.out.println("Kalkulator");
			cardLayout.show(mainPanel, "Calculator");
		});

		return panel;
	}

	private JPanel createDepositPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel logoLabel = new JLabel(bankIcon);
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(logoLabel, gbc);

		JLabel messageLabel = createLabel("Wprowadź kwotę do wpłaty");
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(messageLabel, gbc);

		JTextField amountField = createTextField(10);
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(amountField, gbc);

		JButton depositButton = createButton("Wpłać");
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(depositButton, gbc);

		JButton backButton = createButton("Wróć");
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(backButton, gbc);

		depositButton.addActionListener(e -> {
			try {
				double amount = Double.parseDouble(amountField.getText());
				if (amount > 0) {
					accountData.get(currentCardNumber).deposit(amount);
					showInfo("Wpłacono: " + amount, "Sukces");
					amountField.setText("");
					cardLayout.show(mainPanel, "Home");
				} else {
					showError("Kwota musi być większa od zera");
				}
			} catch (NumberFormatException ex) {
				showError("Nieprawidłowa kwota");
			}
		});

		backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

		return panel;
	}

	private JPanel createWithdrawPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel logoLabel = new JLabel(bankIcon);
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(logoLabel, gbc);

		JLabel messageLabel = createLabel("Wprowadź kwotę do wypłaty");
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(messageLabel, gbc);

		JTextField amountField = createTextField(10);
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(amountField, gbc);

		JButton withdrawButton = createButton("Wypłać");
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(withdrawButton, gbc);

		JButton backButton = createButton("Wróć");
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(backButton, gbc);

		withdrawButton.addActionListener(e -> {
			try {
				double amount = Double.parseDouble(amountField.getText());
				Account account = accountData.get(currentCardNumber);
				if (amount > 0) {
					if (account.getBalance() >= amount) {
						account.withdraw(amount);
						showInfo("Wypłacono: " + amount, "Sukces");
						amountField.setText("");
						cardLayout.show(mainPanel, "Home");
					} else {
						showError("Niewystarczające środki");
					}
				} else {
					showError("Kwota musi być większa od zera");
				}
			} catch (NumberFormatException ex) {
				showError("Nieprawidłowa kwota");
			}
		});

		backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

		return panel;
	}

	private JPanel createTransferPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel logoLabel = new JLabel(bankIcon);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(logoLabel, gbc);

		JLabel accountNumberLabel = createLabel("Numer konta odbiorcy");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		panel.add(accountNumberLabel, gbc);

		JTextField accountNumberField = createTextField(10);
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(accountNumberField, gbc);

		JLabel amountLabel = createLabel("Kwota");
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(amountLabel, gbc);

		JTextField amountField = createTextField(10);
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(amountField, gbc);

		JButton transferButton = createButton("Przelej");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		panel.add(transferButton, gbc);

		JButton backButton = createButton("Wróć");
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(backButton, gbc);

		transferButton.addActionListener(e -> {
			try {
				String recipientAccountNumber = accountNumberField.getText();
				double amount = Double.parseDouble(amountField.getText());
				Account senderAccount = accountData.get(currentCardNumber);

				if (amount <= 0) {
					showError("Kwota musi być większa od zera");
					return;
				}

				if (currentCardNumber.equals(recipientAccountNumber)) {
					showError("Nie można przelać na to samo konto");
					return;
				}

				if (accountData.containsKey(recipientAccountNumber)) {
					Account recipientAccount = accountData.get(recipientAccountNumber);
					if (senderAccount.getBalance() >= amount) {
						senderAccount.withdraw(amount);
						recipientAccount.deposit(amount);
						showInfo("Przelano: " + amount + " do " + recipientAccountNumber, "Sukces");
						accountNumberField.setText("");
						amountField.setText("");
						cardLayout.show(mainPanel, "Home");
					} else {
						showError("Niewystarczające środki");
					}
				} else {
					showError("Nieprawidłowy numer konta odbiorcy");
				}
			} catch (NumberFormatException ex) {
				showError("Nieprawidłowa kwota");
			}
		});

		backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

		return panel;
	}

	private JPanel createBalancePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel logoLabel = new JLabel(bankIcon);
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(logoLabel, gbc);

		JLabel balanceLabel = createLabel("Twoje saldo: ");
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(balanceLabel, gbc);

		JButton backButton = createButton("Wróć");
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(backButton, gbc);

		backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				updateBalancePanel(balanceLabel);
			}
		});

		return panel;
	}

	private void updateBalancePanel(JLabel balanceLabel) {
		if (accountData.containsKey(currentCardNumber)) {
			double balance = accountData.get(currentCardNumber).getBalance();
			balanceLabel.setText("Twoje saldo: " + balance);
		} else {
			balanceLabel.setText("Nie można pobrać salda");
		}
	}

	private JPanel createCurrencyCalculatorPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel logoLabel = new JLabel(bankIcon);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(logoLabel, gbc);

		JLabel messageLabel = createLabel("Kalkulator walut");
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(messageLabel, gbc);

		JLabel plnLabel = createLabel("PLN");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		panel.add(plnLabel, gbc);

		JTextField plnField = createTextField(10);
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(plnField, gbc);

		JLabel eurLabel = createLabel("EUR");
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(eurLabel, gbc);

		JTextField eurField = createTextField(10);
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(eurField, gbc);

		JLabel usdLabel = createLabel("USD");
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(usdLabel, gbc);

		JTextField usdField = createTextField(10);
		gbc.gridx = 1;
		gbc.gridy = 4;
		panel.add(usdField, gbc);

		JButton backButton = createButton("Wróć");
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		panel.add(backButton, gbc);

		plnField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateCurrencyFields(plnField, eurField, usdField, "PLN");
			}
		});

		eurField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateCurrencyFields(plnField, eurField, usdField, "EUR");
			}
		});

		usdField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateCurrencyFields(plnField, eurField, usdField, "USD");
			}
		});

		backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));


		return panel;
	}

	private void updateCurrencyFields(JTextField plnField, JTextField eurField, JTextField usdField, String baseCurrency) {
		double eurToPlnRate = 4.3;
		double usdToPlnRate = 4.0;
		double plnToEurRate = 1 / eurToPlnRate;
		double plnToUsdRate = 1 / usdToPlnRate;
		double eurToUsdRate = plnToUsdRate / plnToEurRate;
		double usdToEurRate = 1 / eurToUsdRate;
		double amount;

		switch (baseCurrency) {
			case "PLN":
				amount = Double.parseDouble(plnField.getText());
				eurField.setText(String.format("%.2f", amount * plnToEurRate));
				usdField.setText(String.format("%.2f", amount * plnToUsdRate));
				break;
			case "EUR":
				amount = Double.parseDouble(eurField.getText());
				plnField.setText(String.format("%.2f", amount * eurToPlnRate));
				usdField.setText(String.format("%.2f", amount * eurToUsdRate));
				break;
			case "USD":
				amount = Double.parseDouble(usdField.getText());
				plnField.setText(String.format("%.2f", amount * usdToPlnRate));
				eurField.setText(String.format("%.2f", amount * usdToEurRate));
				break;
		}
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("Plik");
		fileMenu.setMnemonic('P');

		JMenuItem openMenuItem = new JMenuItem("Otwórz");
		fileMenu.add(openMenuItem);
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		openMenuItem.setMnemonic('O');
		openMenuItem.addActionListener(e -> openFile());

		fileMenu.addSeparator();

		JMenuItem saveMenuItem = new JMenuItem("Zapisz");
		fileMenu.add(saveMenuItem);
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		saveMenuItem.setMnemonic('Z');
		saveMenuItem.addActionListener(e -> saveFile());

		JMenuItem saveAsMenuItem = new JMenuItem("Zapisz jako");
		fileMenu.add(saveAsMenuItem);
		saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift S"));
		saveAsMenuItem.setMnemonic('a');
		saveAsMenuItem.addActionListener(e -> saveFileAs());

		fileMenu.addSeparator();

		JMenuItem exitMenuItem = new JMenuItem("Wyjdź");
		fileMenu.add(exitMenuItem);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
		exitMenuItem.setMnemonic('W');
		exitMenuItem.addActionListener(e -> System.exit(0));

		menuBar.add(fileMenu);

		setJMenuBar(menuBar);
	}

	private void openFile() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			currentFile = fileChooser.getSelectedFile();
			loadDataFromFile(currentFile);
		}
	}

	private void saveFile() {
		if (currentFile != null) {
			saveDataToFile(currentFile);
		} else {
			saveFileAs();
		}
	}

	private void saveFileAs() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			currentFile = fileChooser.getSelectedFile();
			saveDataToFile(currentFile);
		}
	}

	private void loadDataFromFile(File file) {
		// System.out.println(file);
		try {
			accountData.clear(); // Czyści poprzednie dane
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 3) {
					String cardNumber = parts[0];
					String pin = parts[1];
					double balance = Double.parseDouble(parts[2]);
					accountData.put(cardNumber, new Account(cardNumber, pin, balance));
				}
			}
			fr.close();
			dataLoaded = true;
			System.out.println("Wczytano dane z " + file + ":");
			for (Map.Entry<String, Account> entry : accountData.entrySet()) {
				System.out.println("Nr: " + entry.getKey() + ",\tPIN: " + entry.getValue().getPin() + ",\tSaldo: " + entry.getValue().getBalance());
			}
			cardLayout.show(mainPanel, "Card");
		} catch (IOException e) {
			showError("Błąd wczytywania danych z pliku");
		}
	}

	private void saveDataToFile(File file) {
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for (Map.Entry<String, Account> entry : accountData.entrySet()) {
				bw.write(entry.getKey() + "," + entry.getValue().getPin() + "," + entry.getValue().getBalance());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			showError("Błąd zapisywania danych do pliku");
		}
	}

	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(200, 75));
		button.setBackground(Color.GREEN);
		button.setForeground(Color.WHITE);
		button.setFont(new Font(fontName, Font.BOLD, fontSize));

		return button;
	}

	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font(fontName, Font.PLAIN, fontSize));

		return label;
	}

	private JTextField createTextField(int columns) {
		JTextField textField = new JTextField(columns);
		textField.setPreferredSize(new Dimension(200, 50));
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setFont(new Font(fontName, Font.PLAIN, fontSize));

		return textField;
	}

	private JPasswordField createPasswordField(int columns) {
		JPasswordField passwordField = new JPasswordField(columns);
		passwordField.setPreferredSize(new Dimension(200, 50));
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField.setFont(new Font(fontName, Font.PLAIN, fontSize));

		return passwordField;
	}

	private void showError(String text) {
		JOptionPane.showMessageDialog(this, text, "Błąd", JOptionPane.ERROR_MESSAGE);
	}

	private void showInfo(String text, String title) {
		JOptionPane.showMessageDialog(this, text, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
