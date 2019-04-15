package topoMem;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.lang.*;

public class TopoMem {
	
	private static int currentPattern;
	private static Integer score = new Integer(0);
	private static int good = 0;
	private static volatile int buttonPressed = 0;
	private static volatile boolean buttonClickable = false;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JLabel label = new JLabel();
		JLabel labelScore = new JLabel("0");
		ImageIcon red = new ImageIcon("red.png");
		ImageIcon white = new ImageIcon("white.png");
		JButton[] button = new JButton[17];
		JButton OK = new JButton("Ready");
		int[] filledPattern = new int[17];
		int usedPattern[] = new int[10];
		int gameOver = 0;
		long startTime = 0;
		int pattern[][]  = {{0,1,0,0,0,0,1,0,1,1,1,0,0,0,0,1,0},
							{0,0,1,0,0,0,0,1,0,0,1,0,0,1,0,1,1},
							{0,0,1,0,0,1,0,1,0,1,0,0,1,0,0,1,0},
							{0,0,0,0,1,0,1,0,0,0,0,1,1,1,0,0,1},
							{0,0,0,1,1,0,1,0,1,1,0,1,0,0,0,0,0},
							{0,0,0,1,1,1,0,0,0,0,0,1,0,1,0,0,1},
							{0,0,1,0,0,0,1,0,1,0,1,0,0,1,0,1,0},
							{0,0,0,0,1,0,1,0,0,0,0,1,1,0,1,0,1},
							{0,0,0,0,1,0,1,1,0,0,0,0,1,1,0,1,0},
							{0,1,1,0,1,0,0,0,0,0,0,0,1,0,1,0,1} };
		
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0; c.gridy = 0; c.fill = 1;
		//frame.setSize(700,700);
		frame.setVisible(true);
		frame.setTitle("Topographic Memory");
		frame.setLocation(500,80);
		frame.setResizable(false);
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		for(int i = 1; i <= 4; ++i) {
			for(int j = 1; j <= 4; ++j) {
				c.gridx += 100;
				int current = (i-1)*4 + j;
				button[(i-1)*4 + j] = new JButton(new ImageIcon("white.png"));
				button[(i-1)*4 + j].setPreferredSize(new Dimension(100,100));
				button[(i-1)*4 + j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					 if(buttonClickable == true) {
						if(button[current].getIcon() == red) {
							button[current].setIcon(white);
							filledPattern[current] = 0;
						}
						else {
							button[current].setIcon(red);
							filledPattern[current] = 1;
						}
					 }
					}
				} );
				frame.add(button[(i-1)*4 + j],c);
			}
			
			c.gridx = 0;
			c.gridy += 100;
		}
		c.gridx = 100; c.fill = 0; c.gridy += 100;
		OK.setPreferredSize(new Dimension(100,100));
		
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				score += checkPattern(filledPattern, pattern[currentPattern]);
				
				if(checkPattern(filledPattern, pattern[currentPattern]) == 6)
					good++;
				
				for(int i = 1; i <= 16; ++i) {
					filledPattern[i] = 0;
					button[i].setIcon(white);
				}
				
				buttonPressed = 1;
				labelScore.setText(score.toString());
			}
		});
		c.gridx = 0;
		frame.add(label,c);
		c.gridx = 200;
		frame.add(OK,c);
		c.gridx = 300;
		frame.add(labelScore,c);
		frame.pack();
		while(gameOver == 0) {
			Random rand = new Random();
			currentPattern = rand.nextInt(10);
			
			startTime = System.currentTimeMillis();
			
			OK.setEnabled(false);
			while(usedPattern[currentPattern] == 1)
				currentPattern = rand.nextInt(10);
			
			usedPattern[currentPattern] = 1;
			
			for(int i = 1; i <= 16; ++i)
				if(pattern[currentPattern][i] == 1)
					button[i].setIcon(red);
			buttonClickable = false;
			try{Thread.sleep(2500);} catch(InterruptedException e) {System.out.println(e);}
			
			buttonClickable = true;
			OK.setEnabled(true);
			
			for(int i = 1; i <= 16; ++i)
				button[i].setIcon(white);
			
			while(buttonPressed == 0);
				
			buttonPressed = 0;
			
			
			gameOver = 1;
			
		   for(int i = 0; i < 10; ++i) {
				if(usedPattern[i] == 0) {
					gameOver = 0;
					break;
				}
			}
		}
		
		long stopTime = System.currentTimeMillis();
		long elapsedTime = (stopTime - startTime)/100 - 25;
		frame.getContentPane().removeAll();
		frame.repaint();
		c.gridy = 0;
		JLabel scoreLbl = new JLabel("Congratulations, you scored: " + score);
		JLabel goodLbl = new JLabel("Patterns completed correctly: " + good);
		JLabel badLbl = new JLabel("Pattern completed wrong: " + (10 - good));
		JLabel tmeLbl = new JLabel("Patterns completed in: " + elapsedTime + " seconds");
		frame.add(scoreLbl,c);
		c.gridy = 20;
		frame.add(goodLbl,c);
		c.gridy = 30;
		frame.add(badLbl,c);
		c.gridy = 40;
		frame.add(tmeLbl, c);
		frame.validate();
		try{Thread.sleep(5000);} catch(InterruptedException e) {System.out.println(e);}
		
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		
		
	}
	
	public static Integer checkPattern(int[] filledPattern, int[] correctPattern) {
		Integer returnValue = new Integer(0);
		for(int i = 1; i <= 16; ++i)
		  if(correctPattern[i] == 1) {
			if(filledPattern[i] != correctPattern[i])
				returnValue -= 1;
			else
				returnValue += 1;
		  }
		return returnValue;
	}

	
	
}
