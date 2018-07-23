package gamepackage;
import java.awt.Canvas;
import javax.swing.JFrame;
public class Window extends Canvas {
	private static final long serialVersionUID = 12345678L;
	public Window (int width, int height, String title, GamePanel g) {
		JFrame frame = new JFrame(title);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(g);
		frame.pack();
		frame.setLocationRelativeTo(null);//puts window in center of screen
		frame.setVisible(true);
		
		g.start();
	}
}
