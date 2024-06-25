import java.awt.*;

public class Main {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Bankomat bankomat = new Bankomat();
			}
		});

		/* SwingUtilities.invokeLater(() -> {
			Bankomat bankomat = new Bankomat();
		}); */
	}
}
