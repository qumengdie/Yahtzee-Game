package YahtzeeGame;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private Image img;
	private String[] diceImgName = {"die1.png","die2.png","die3.png","die4.png","die5.png","die6.png"};
	public int curnum = 1;
	
	public ImagePanel(String img) {
		this(new ImageIcon(img).getImage());
	}

	public ImagePanel(Image img) {
		this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        /*setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);*/
        setLayout(null);
	}
	
	public int roll() {
		int num = 0;
			num = (int)(Math.random()*6 + 1);
			img = new ImageIcon(diceImgName[num-1]).getImage();
			repaint();
		this.curnum = num;
		return num;
	}
	
	public int diceNum() {
		return this.curnum;
	}
	
    public void paintComponent(Graphics g) {
        g.drawImage(img, 50, 0, null);
    }
    
    public void setDice(String img) {
    	this.img = new ImageIcon(img).getImage();
    	repaint();
    }
}
