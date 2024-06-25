public class Account {
	private String cardNumber;
	private String pin;
	private double balance;

	public Account(String cardNumber, String pin, double balance) {
		this.cardNumber = cardNumber;
		this.pin = pin;
		this.balance = balance;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getPin() {
		return pin;
	}

	public double getBalance() {
		return balance;
	}

	public void deposit(double amount) {
		balance += amount;
	}

	public void withdraw(double amount) {
		balance -= amount;
	}
}
