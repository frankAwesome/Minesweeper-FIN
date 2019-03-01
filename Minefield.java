/*
FRANCOIS DE KLERK => MINESWEEPER
GUIDE:
1. You can not place more flags, than bombs: flags <= bombs
2. If you click a bomb, you lose
3. If you reveal all fields except for the bombs, you win
4. If you place a flag on top of all the bombs, you win
5. When you lose, or win, the rest of the buttons will be disabled except for the face emoji
6. When you click the face emoji, the game will restart
7. Flags can be removed after being placed
8. The timer starts when the first button is clicked
9. The timer also starts if a flag is placed before any button is clicked
10. The timer will stop when the player wins or loses the game
11. The face emoji will give different expressions for winning and losing

12. Bombs can be shown on startup by enabling the displayField method in line 506
*/


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import java.util.Arrays;
import javax.sound.sampled.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.audio.*;
import java.applet.*;
import java.awt.Color;


//frame is hidden
//hideFrame() method

public class Minefield extends JFrame
{
	int ROWS = 9;
	int COLS = 7;
	private int BOMBS = 7;
	
	public int bombWin = 0;
	public int revealedWin = 0;
	public boolean WonOrLost = false;
	public int flags = 0;
	public boolean playedAtLeastOnce = false;

	//2==medium 1==small 3==large
	public int sizeGame = 2;
	
	//2==moderate 1==easy 3==hard
	public int difficulty = 2;
	
	private JButton smile;
	private JButton[][] grid;
	JPanel gridPanel;
	JPanel pnlButton;
	JPanel pnlBelow;
	public JLabel displayTimeLabel;
	public JLabel lblBombCount;
	JLabel tempLabel;
	private JFrame frame;
	private final GridBagLayout layout;
	private final GridBagConstraints gbc;
	
	public long watchStart, watchEnd;
	public long watchStartFlicker, watchEndFlicker;
	public Timer theChronometer;
	public Timer theChronometerTwo;
	public long pausedTime;
	public long pausedTimeFlicker;
	public boolean paused = false;
	public boolean pausedFlicker = false;
	public boolean started = false;
	public boolean startedFlicker = false;
	
	
	public int times[]  = new int[] {0,0,0,0,0,0,0,0,0,0};
	public int timesModerate[]  = new int[] {0,0,0,0,0,0,0,0,0,0};
	public int timesEasy[]  = new int[] {0,0,0,0,0,0,0,0,0,0};

	
	public int timeSort;
	
	public int joker = 1;
	public int secPrev = 0;
	
	
	//dark theme
	public static final Color VERY_LIGHT_GRAY = new Color(204,204,204);
	public static final Color BACK_BLUE = new Color(36,47,65);
	
	//light theme
	public static final Color LIGHT_BLUE = new Color(216,243,245);
	public static final Color LIGHT_GRAY_BUTTONS = new Color(230,238,239);
	
	public boolean lightThemeBool;

               

	
	private MSCell field[][];

	public Minefield()
	{
		super("Minesweeper");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame = this;
		this.frame.setSize (650,550);
		
		
		//The application icon is changed
		try
		{
			BufferedImage frameIcon = ImageIO.read(new File("Recourses/mineCon.png"));
			frame.setIconImage(frameIcon);
		}
		catch (FileNotFoundException ex)
		{
			
		}
		catch (IOException ex)
		{
			
		}
		
		
		//Places the frame in the centre of any screen by looking at the screen resolution
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		layout = new GridBagLayout();
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		createMenuBar();
		
		//This code was moved so it can be called again later if the smiley button is pressed or size/difficulty is changed
        mainMoved();
		
		playBackSong();
		
		
		//initArray is for the score to be read from a file and put into an array
		initArray();
		

		
		
	}
	

	
	public void playBackSong()
	{
	try {
		File soundFile = new File("Recourses/5.WAV");
		AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
 

		DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
		Clip clip = (Clip) AudioSystem.getLine(info);
		clip.open(sound);
 
   
		clip.addLineListener(new LineListener() {
		public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
          event.getLine().close();
        }
      }
    });
 
    //clip.start();
	clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    catch (IOException e) {
    }
    catch (LineUnavailableException e) {
    }
    catch (UnsupportedAudioFileException e) {
    }

	}
	
	//BUTTONS
	public void playButtonSound()
	{
	try {
		File soundFileTwo = new File("Recourses/button2.WAV");
		AudioInputStream soundTwo = AudioSystem.getAudioInputStream(soundFileTwo);
 

		DataLine.Info infoTwo = new DataLine.Info(Clip.class, soundTwo.getFormat());
		Clip clipTwo = (Clip) AudioSystem.getLine(infoTwo);
		clipTwo.open(soundTwo);
 
   
		clipTwo.addLineListener(new LineListener() {
		public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
          event.getLine().close();
        }
      }
    });
 
    clipTwo.start();
    }
    catch (IOException e) {
    }
    catch (LineUnavailableException e) {
    }
    catch (UnsupportedAudioFileException e) {
    }

	}
	
	
	public void playRightSound()
	{
	try {
		File soundFileThree = new File("Recourses/right.WAV");
		AudioInputStream soundThree = AudioSystem.getAudioInputStream(soundFileThree);
 

		DataLine.Info infoThree = new DataLine.Info(Clip.class, soundThree.getFormat());
		Clip clipThree = (Clip) AudioSystem.getLine(infoThree);
		clipThree.open(soundThree);
 
   
		clipThree.addLineListener(new LineListener() {
		public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
          event.getLine().close();
        }
      }
    });
 
    clipThree.start();
    }
    catch (IOException e) {
    }
    catch (LineUnavailableException e) {
    }
    catch (UnsupportedAudioFileException e) {
    }

	}
	
	
	
	public void playCheerSound()
	{
	try {
		File soundFileFour = new File("Recourses/cheer2.WAV");
		AudioInputStream soundFour = AudioSystem.getAudioInputStream(soundFileFour);
 

		DataLine.Info infoFour = new DataLine.Info(Clip.class, soundFour.getFormat());
		Clip clipFour = (Clip) AudioSystem.getLine(infoFour);
		clipFour.open(soundFour);
 
   
		clipFour.addLineListener(new LineListener() {
		public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
          event.getLine().close();
        }
      }
    });
 
    clipFour.start();
    }
    catch (IOException e) {
    }
    catch (LineUnavailableException e) {
    }
    catch (UnsupportedAudioFileException e) {
    }

	}
	
	
	public void playLoseSound()
	{
	try {
		File soundFileFive = new File("Recourses/boo2.WAV");
		AudioInputStream soundFive = AudioSystem.getAudioInputStream(soundFileFive);
 

		DataLine.Info infoFive = new DataLine.Info(Clip.class, soundFive.getFormat());
		Clip clipFive = (Clip) AudioSystem.getLine(infoFive);
		clipFive.open(soundFive);
 
   
		clipFive.addLineListener(new LineListener() {
		public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
          event.getLine().close();
        }
      }
    });
 
    clipFive.start();
    }
    catch (IOException e) {
    }
    catch (LineUnavailableException e) {
    }
    catch (UnsupportedAudioFileException e) {
    }

	}
	
	//Removes the current components such as buttons
	private void removeGrid()
	{
		paused = true;
		if (playedAtLeastOnce == true)
		{
		   theChronometer.stop();
	       //theChronometerTwo.stop();
		}
		try
		{
		frame.remove(gridPanel);
		frame.remove(pnlButton);
		frame.remove(pnlBelow);
		theChronometerTwo.stop();
		}
		catch (Exception e) 
		{
		}
	
		validate();
		repaint();
		revealedWin = 0;
		bombWin = 0;
		flags = 0;
		watchStart = 0;
		watchStartFlicker = 0;
		started = false;
		startedFlicker = false;
		
		
	}
	
	
	private void mainMoved()
	{
		//initArray();
		
		grid = new JButton[ROWS+2][COLS+2];
		MouseHandler mh = new MouseHandler();
		ButtonHandler handler = new ButtonHandler();
		gridPanel = new JPanel();
		pnlButton = new JPanel();
		pnlBelow = new JPanel();
		
		pnlButton.setBounds(170, 10, frame.getWidth()/2, 60);
		
		
	
		gridPanel.setLayout(layout);
		
		
		
		
		
		try
		{
			BufferedImage buttonIcon = ImageIO.read(new File("Recourses/smiley.png"));
			smile = new JButton(new ImageIcon(buttonIcon));
			smile.setMinimumSize(new Dimension(48, 48));
			smile.setPreferredSize(new Dimension(48, 48));
			smile.setMaximumSize(new Dimension(48, 48));
			pnlButton.add(smile);
			smile.addActionListener(handler);
		}
		catch (FileNotFoundException ex)
		{
		}
		catch (IOException ex)
		{
		}
		
		Font largeFontPLAIN = new Font("Century Gothic", Font.PLAIN,20);
		Font smallFontPLAIN = new Font("Century Gothic", Font.PLAIN,12);
		
		displayTimeLabel = new JLabel();
		lblBombCount = new JLabel();
		
		displayTimeLabel.setHorizontalAlignment(JLabel.RIGHT);
    	displayTimeLabel.setFont(largeFontPLAIN);
		displayTimeLabel.setOpaque(true);
		
		lblBombCount.setHorizontalAlignment(JLabel.CENTER);
    	lblBombCount.setFont(smallFontPLAIN);
		lblBombCount.setOpaque(true);
		
		displayTimeLabel.setText("00:00:00");
		
		smile.setBorder(null);
		
		
		if (sizeGame == 2)
		{
			if (difficulty == 2)
			{
				pnlBelow.setBounds(160, 420, frame.getWidth()/2, 60);
				lblBombCount.setText("Size: Medium   Difficulty: Moderate   Amount of mines: "+BOMBS);
			}
			else if (difficulty == 1)
			{
				pnlBelow.setBounds(160, 420, frame.getWidth()/2, 60);
				lblBombCount.setText("Size: Medium   Difficulty: Easy   Amount of mines: "+BOMBS);
			}
			else
			{
				pnlBelow.setBounds(160, 420, frame.getWidth()/2, 60);
				lblBombCount.setText("Size: Medium   Difficulty: Hard   Amount of mines: "+BOMBS);
			}
		}
		else if (sizeGame == 1)
		{
			if (difficulty == 2)
			{
				pnlBelow.setBounds(80, 320, 400, 60);
				lblBombCount.setText("Size: Small   Difficulty: Moderate   Amount of mines: "+BOMBS);
			}
			else if (difficulty == 1)
			{
				pnlBelow.setBounds(80, 320, 400, 60);
				lblBombCount.setText("Size: Small   Difficulty: Easy   Amount of mines: "+BOMBS);
			}
			else
			{
				pnlBelow.setBounds(80, 320, 400, 60);
				lblBombCount.setText("Size: Small   Difficulty: Hard   Amount of mines: "+BOMBS);
			}
		}
		 else if (sizeGame == 3)
		{
			if (difficulty == 2)
			{
				pnlBelow.setBounds(190, 620, frame.getWidth()/2, 60);
				lblBombCount.setText("Size: Large   Difficulty: Moderate   Amount of mines: "+BOMBS);
			}
			else if (difficulty == 1)
			{
				pnlBelow.setBounds(190, 620, frame.getWidth()/2, 60);
				lblBombCount.setText("Size: Large   Difficulty: Easy   Amount of mines: "+BOMBS);
			}
			else
			{
				pnlBelow.setBounds(190, 620, frame.getWidth()/2, 60);
				lblBombCount.setText("Size: Large   Difficulty: Hard   Amount of mines: "+BOMBS);
			}
		}
		
		pnlButton.add(displayTimeLabel);
		pnlBelow.add(lblBombCount);
		//pnlButton.setBackground(Color.lightGray);
		
		add(pnlButton);
		add(pnlBelow);
		
		for (int i=0; i<ROWS+2; i++)
			for (int j=0; j<COLS+2; j++)
			{
				gbc.gridx = i;
				gbc.gridy = j;
				//grid[i][j] = new JButton(i+","+j);
				grid[i][j] = new JButton(" ");
				grid[i][j].setMinimumSize(new Dimension(48, 48));
			    grid[i][j].setPreferredSize(new Dimension(48, 48));
				grid[i][j].setMaximumSize(new Dimension(48, 48));
				gridPanel.add(grid[i][j],gbc);
				grid[i][j].addMouseListener (mh);	
				grid[i][j].setFocusPainted(false);				
			}
			
		setLayout(new BorderLayout());
		
		add(gridPanel,BorderLayout.CENTER);
			
		field  = new MSCell[ROWS+2][COLS+2];
		
		
		if (!lightThemeBool)
		{
			darkTheme();
		}
		else
		{
			lightTheme();
		}
		
		initialiseField();
		plantBombs();
		calculateValues();
		//displayField();
		hideFrame();
	}
	
	public void initialiseField()
	{		
		for (int i=0; i<ROWS+2; i++)
			for (int j=0; j<COLS+2; j++)
				field[i][j] = new MSCell(false, false, 0, false);
			
	}

    public void plantBombs()
	{
		int bombRow;
		int bombCol;
		int bombCount = 0;
		
		Random randomNumbers = new Random();
		
		while(bombCount < BOMBS)
		{
			bombRow = randomNumbers.nextInt(ROWS)+1;
			bombCol = randomNumbers.nextInt(COLS)+1;
		   
			if (field[bombRow][bombCol].isBomb() == false && bombRow!=0 && bombCol!=0)
			{
				field[bombRow][bombCol].setBomb(true);
				System.out.println(bombRow +" " + bombCol);
				bombCount++;
			}
		}
	}
	
	public void calculateValues()
	{
		for (int i=1; i<ROWS+1; i++)
			for (int j=1; j<COLS+1; j++)
				if (field[i][j].isBomb() == true)
                    for (int k=-1;k<=1;k++)
					    for (int l=-1;l<=1;l++)	     
						    if (field[i+k][j+l].isBomb() == false && i+k!=0 && j+l!=0 && i+k!=ROWS+1 && j+l!=COLS+1)
						    {
								//case?
						        if (field[i+k][j+l].getValue()==0)
							        field[i+k][j+l].setValue(1);
						        else if (field[i+k][j+l].getValue()==1)
							        field[i+k][j+l].setValue(2);
						        else if (field[i+k][j+l].getValue()==2)
							        field[i+k][j+l].setValue(3);
							    else if (field[i+k][j+l].getValue()==3)
							        field[i+k][j+l].setValue(4);
							    else if (field[i+k][j+l].getValue()==4)
							        field[i+k][j+l].setValue(5);
							    else if (field[i+k][j+l].getValue()==5)
							        field[i+k][j+l].setValue(6);
							    else if (field[i+k][j+l].getValue()==6)
							        field[i+k][j+l].setValue(7);
							    else if (field[i+k][j+l].getValue()==7)
							        field[i+k][j+l].setValue(8);
						    }
						
        //Frame is now -1's						
		for (int f=0; f<=ROWS+1; f++)
			for (int g=0; g<=COLS+1; g++)
				if (f==0 || g==0 || f==ROWS+1 || g==COLS+1)
				{
					field[f][g].setValue(-1);
				}
	}
	
	public void hideFrame()
	{
		for (int i=0; i<ROWS+2; i++)
			for (int j=0; j<COLS+2; j++)
				if (i==0 || j==0 || i==ROWS+1 || j==COLS+1)
				{
					grid[i][j].setVisible(false);
				}
	}
	
	public void displayField()
	{	
        for (int i=0; i<ROWS+2; i++)
			for (int j=0; j<COLS+2; j++)
			{
				if (field[i][j].isBomb())
				{
					gbc.gridx = i;
					gbc.gridy = j;
					grid[i][j].setText(field[i][j].toString());
					
				}					
			}
	}
	
	
	//Fields will be revealed here
	//If the field was a 0, recursion will start
	public void revealRec(int r, int c)
	{
		if (field[r][c].getValue()==0)
		{
			for (int k=-1;k<=1;k++)
			{
				for (int l=-1;l<=1;l++)	 
				{				
					if (field[r+k][c+l].isBomb() == false && field[r+k][c+l].getValue()!=-1 && field[r+k][c+l].isRevealed() == false)
					{
						field[r+k][c+l].setRevealed(true);
				
						revealedWin++;
		
						gbc.gridx = r+k;
						gbc.gridy = c+l;
						gridPanel.remove(grid[r+k][c+l]);
						//tempLabel.setText(field[r+k][c+l].toString());
						
						if (field[r+k][c+l].getValue() == 0)
						{
							try
							{
								if (!lightThemeBool)
								{
									BufferedImage nullImage = ImageIO.read(new File("Recourses/null.png"));
									Image newNull = nullImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									tempLabel= new JLabel(new ImageIcon(newNull));
								}
								else
								{
									BufferedImage nullImageLight = ImageIO.read(new File("Recourses/light/null.png"));
									Image newNullLight = nullImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									tempLabel= new JLabel(new ImageIcon(newNullLight));
								}
								
								
								
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r+k][c+l].getValue() == 1)
						{
							try
							{
								
							
								
								
								if (!lightThemeBool)
								{
									BufferedImage oneImage = ImageIO.read(new File("Recourses/1.png"));
									Image newOne = oneImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									tempLabel= new JLabel(new ImageIcon(newOne));
								}
								else
								{
									BufferedImage oneImageLight = ImageIO.read(new File("Recourses/light/1.png"));
									Image newOneLight = oneImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									tempLabel= new JLabel(new ImageIcon(newOneLight));
								}
						
								
								
								
								
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r+k][c+l].getValue() == 2)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage twoImage = ImageIO.read(new File("Recourses/2.png"));
								Image newTwo = twoImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newTwo));
								}
								else
								{
									BufferedImage twoImageLight = ImageIO.read(new File("Recourses/light/2.png"));
									Image newTwoLight = twoImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									tempLabel= new JLabel(new ImageIcon(newTwoLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r+k][c+l].getValue() == 3)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage threeImage = ImageIO.read(new File("Recourses/3.png"));
								Image newThree = threeImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newThree));
								}
								else
								{
								BufferedImage threeImageLight = ImageIO.read(new File("Recourses/light/3.png"));
								Image newThreeLight = threeImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newThreeLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r+k][c+l].getValue() == 4)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage fourImage = ImageIO.read(new File("Recourses/4.png"));
								Image newFour = fourImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newFour));
								}
								else 
								{
								BufferedImage fourImageLight = ImageIO.read(new File("Recourses/light/4.png"));
								Image newFourLight = fourImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newFourLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r+k][c+l].getValue() == 5)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage fiveImage = ImageIO.read(new File("Recourses/5.png"));
								Image newFive = fiveImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newFive));
								}
								else
								{
								BufferedImage fiveImageLight = ImageIO.read(new File("Recourses/light/5.png"));
								Image newFiveLight = fiveImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newFiveLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r+k][c+l].getValue() == 6)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage sixImage = ImageIO.read(new File("Recourses/6.png"));
								Image newSix = sixImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newSix));
								}
								else
								{
								BufferedImage sixImageLight = ImageIO.read(new File("Recourses/light/6.png"));
								Image newSixLight = sixImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newSixLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r+k][c+l].getValue() == 7)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage sevenImage = ImageIO.read(new File("Recourses/7.png"));
								Image newSeven = sevenImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newSeven));
								}
								else
								{
								BufferedImage sevenImageLight = ImageIO.read(new File("Recourses/light/7.png"));
								Image newSevenLight = sevenImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newSevenLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r+k][c+l].getValue() == 8)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage eightImage = ImageIO.read(new File("Recourses/8.png"));
								Image newEight = eightImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newEight));
								}
								else
								{
								BufferedImage eightImageLight = ImageIO.read(new File("Recourses/light/8.png"));
								Image newEightLight = eightImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newEightLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						//gridPanel.add(tempLabel,gbc);
						//revalidate();
						//repaint();	
						
						
					//THIS IS THE RECURSION
						if (field[r+k][c+l].getValue() == 0)
							revealRec(r+k,c+l);
					}
				}
			}				
		}
		else
		{
			field[r][c].setRevealed(true);
					

						revealedWin++;
		
						gbc.gridx = r;
						gbc.gridy = c;
						gridPanel.remove(grid[r][c]);
						
						if (field[r][c].getValue() == 0)
						{
							try
							{
							    if (!lightThemeBool)
								{
									BufferedImage nullImage = ImageIO.read(new File("Recourses/null.png"));
									Image newNull = nullImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									tempLabel= new JLabel(new ImageIcon(newNull));
								}
								else
								{
									BufferedImage nullImageLight = ImageIO.read(new File("Recourses/light/null.png"));
									Image newNullLight = nullImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									tempLabel= new JLabel(new ImageIcon(newNullLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r][c].getValue() == 1)
						{
							try
							{
								if (!lightThemeBool)
								{
									BufferedImage oneImage = ImageIO.read(new File("Recourses/1.png"));
									Image newOne = oneImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									tempLabel= new JLabel(new ImageIcon(newOne));
								}
								else
								{
									BufferedImage oneImageLight = ImageIO.read(new File("Recourses/light/1.png"));
									Image newOneLight = oneImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									tempLabel= new JLabel(new ImageIcon(newOneLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r][c].getValue() == 2)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage twoImage = ImageIO.read(new File("Recourses/2.png"));
								Image newTwo = twoImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newTwo));
								}
								else
								{
									BufferedImage twoImageLight = ImageIO.read(new File("Recourses/light/2.png"));
									Image newTwoLight = twoImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									tempLabel= new JLabel(new ImageIcon(newTwoLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r][c].getValue() == 3)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage threeImage = ImageIO.read(new File("Recourses/3.png"));
								Image newThree = threeImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newThree));
								}
								else
								{
								BufferedImage threeImageLight = ImageIO.read(new File("Recourses/light/3.png"));
								Image newThreeLight = threeImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newThreeLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r][c].getValue() == 4)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage fourImage = ImageIO.read(new File("Recourses/4.png"));
								Image newFour = fourImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newFour));
								}
								else 
								{
								BufferedImage fourImageLight = ImageIO.read(new File("Recourses/light/4.png"));
								Image newFourLight = fourImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newFourLight));
								}
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r][c].getValue() == 5)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage fiveImage = ImageIO.read(new File("Recourses/5.png"));
								Image newFive = fiveImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newFive));
								}
								else
								{
								BufferedImage fiveImageLight = ImageIO.read(new File("Recourses/light/5.png"));
								Image newFiveLight = fiveImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newFiveLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r][c].getValue() == 6)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage sixImage = ImageIO.read(new File("Recourses/6.png"));
								Image newSix = sixImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newSix));
								}
								else
								{
								BufferedImage sixImageLight = ImageIO.read(new File("Recourses/light/6.png"));
								Image newSixLight = sixImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newSixLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r][c].getValue() == 7)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage sevenImage = ImageIO.read(new File("Recourses/7.png"));
								Image newSeven = sevenImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newSeven));
								}
								else
								{
								BufferedImage sevenImageLight = ImageIO.read(new File("Recourses/light/7.png"));
								Image newSevenLight = sevenImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newSevenLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
						
						if (field[r][c].getValue() == 8)
						{
							try
							{
								if (!lightThemeBool)
								{
								BufferedImage eightImage = ImageIO.read(new File("Recourses/8.png"));
								Image newEight = eightImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newEight));
								}
								else
								{
								BufferedImage eightImageLight = ImageIO.read(new File("Recourses/light/8.png"));
								Image newEightLight = eightImageLight.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
								tempLabel= new JLabel(new ImageIcon(newEightLight));
								}
								
								tempLabel.setMinimumSize(new Dimension(48, 48));
								tempLabel.setPreferredSize(new Dimension(48, 48));
								tempLabel.setMaximumSize(new Dimension(48, 48));
								//tempLabel.setText(field[r][c].toString());
								gridPanel.add(tempLabel,gbc);
								revalidate();
								repaint();
							}
		
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
						}
		}
	}
	
	//If a bomb is clicked, all the bombs will be shown here
	private void MineImages()
	{
		try
		{
			for (int r=1; r<ROWS+1; r++)
				for (int c=1; c<COLS+1; c++)
					if (field[r][c].isBomb() == true)
					{
					BufferedImage mineImage = ImageIO.read(new File("Recourses/mine.png"));
					Image newMine = mineImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
			
					tempLabel= new JLabel(new ImageIcon(newMine));
					tempLabel.setMinimumSize(new Dimension(48, 48));
					tempLabel.setPreferredSize(new Dimension(48, 48));
					tempLabel.setMaximumSize(new Dimension(48, 48));
			
					gbc.gridx = r;
					gbc.gridy = c;
					gridPanel.remove(grid[r][c]);
					//tempLabel.setText(field[r][c].toString());
					gridPanel.add(tempLabel,gbc);
					revalidate();
					repaint();
					}
		}
		catch (FileNotFoundException ex)
		{
		}
		catch (IOException ex)
		{
		}
		
		try
		{
			BufferedImage sadFace = ImageIO.read(new File("Recourses/sad.png"));
			Image newSad = sadFace.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
			smile.setIcon(new ImageIcon(newSad));
		}
		catch (FileNotFoundException ex)
		{
		}
		catch (IOException ex)
		{
		}
		
					pausedFlicker = false;
					StopWatchFlicker();
					watchStartFlicker = System.currentTimeMillis();
					theChronometerTwo.start();
					startedFlicker = true;
		
		if (ROWS*COLS-BOMBS == revealedWin)
		{
			paused = true;
			theChronometer.stop();
			Winner();
			//JOptionPane.showMessageDialog(null, "You win!",  "Minesweeper", JOptionPane.INFORMATION_MESSAGE);
			WonOrLost = true;
									
			//System.exit(0);
		}
	}
	
	//Smiley face wil now change to a laughing emoji
	private void Winner()
	{
		try
		{
			BufferedImage win = ImageIO.read(new File("Recourses/win.png"));
			Image newWin = win.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
			smile.setIcon(new ImageIcon(newWin));
			playCheerSound();
			//StopWatchFlickerSmile();
			
					pausedFlicker = false;
					StopWatchFlickerSmile();
					watchStartFlicker = System.currentTimeMillis();
					theChronometerTwo.start();
					startedFlicker = true;
		}
		catch (FileNotFoundException ex)
		{
		}
		catch (IOException ex)
		{
		}
		
		//onthou init array
		
		bestTime();
		bestTimeModerate();
		bestTimeEasy();
	}
	
	public void initArray()
	{
		String line;
		String lineModerate;
		String lineEasy;
		
		try
		{
		
		File file = new File("Recourses/bestHard.txt"); 
		
		BufferedReader abc = new BufferedReader(new FileReader(file));
		
		//line = abc.readLine();


		for (int i = 0; i <10; i++)
		{
			line = abc.readLine();
			times[i] = Integer.parseInt(line);
		}
		abc.close();
		}
		catch (FileNotFoundException ex)
		{
		}
		catch (IOException ex)
		{
		}
		Arrays.sort(times);
		
		
		
		try
		{
		
		File fileModerate = new File("Recourses/bestModerate.txt"); 
		
		BufferedReader abcd = new BufferedReader(new FileReader(fileModerate));
		
		//line = abc.readLine();


		for (int i = 0; i <10; i++)
		{
			lineModerate = abcd.readLine();
			timesModerate[i] = Integer.parseInt(lineModerate);
		}
		abcd.close();
		}
		catch (FileNotFoundException ex)
		{
		}
		catch (IOException ex)
		{
		}
		Arrays.sort(timesModerate);
		
		
		
		try
		{
		
		File fileEasy = new File("Recourses/bestEasy.txt"); 
		
		BufferedReader abcde = new BufferedReader(new FileReader(fileEasy));
		
		//line = abc.readLine();


		for (int i = 0; i <10; i++)
		{
			lineEasy = abcde.readLine();
			timesEasy[i] = Integer.parseInt(lineEasy);
		}
		abcde.close();
		}
		catch (FileNotFoundException ex)
		{
		}
		catch (IOException ex)
		{
		}
		Arrays.sort(timesEasy);
	}
	
	public void bestTime()
	{
		if (sizeGame == 3 && difficulty == 3)
		{
			if (times[0] == 0 || times[9] > timeSort)
			{
				if (times[0] == 0)
					times[0] = timeSort;
				else if(times[0] != 0)
					if (times[9] > timeSort)
						times[9] = timeSort;
				Arrays.sort(times);
			}
			writeToFile();
		}
		
	}
	
	public void bestTimeModerate()
	{
		if (sizeGame == 3 && difficulty == 2)
		{
			if (timesModerate[0] == 0 || timesModerate[9] > timeSort)
			{
				if (timesModerate[0] == 0)
					timesModerate[0] = timeSort;
				else if(timesModerate[0] != 0)
					if (timesModerate[9] > timeSort)
						timesModerate[9] = timeSort;
				Arrays.sort(timesModerate);
			}
			writeToFileModerate();
		}
		
	}
	
	public void bestTimeEasy()
	{
		if (sizeGame == 3 && difficulty == 1)
		{
			if (timesEasy[0] == 0 || timesEasy[9] > timeSort)
			{
				if (timesEasy[0] == 0)
					timesEasy[0] = timeSort;
				else if(timesEasy[0] != 0)
					if (timesEasy[9] > timeSort)
						timesEasy[9] = timeSort;
				Arrays.sort(timesEasy);
			}
			writeToFileEasy();
		}
		
	}
	
	public void writeToFile()
	{
		try
		{
			PrintWriter pr = new PrintWriter("Recourses/bestHard.txt");    

			for (int i=0; i<10 ; i++)
			{
					pr.println(times[i]);
			}
			pr.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("No such file exists.");
		}
	}
	
	public void writeToFileModerate()
	{
		try
		{
			PrintWriter prM = new PrintWriter("Recourses/bestModerate.txt");    

			for (int i=0; i<10 ; i++)
			{
					prM.println(timesModerate[i]);
			}
			prM.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("No such file exists.");
		}
	}
	
	public void writeToFileEasy()
	{
		try
		{
			PrintWriter prE = new PrintWriter("Recourses/bestEasy.txt");    

			for (int i=0; i<10 ; i++)
			{
					prE.println(timesEasy[i]);
			}
			prE.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("No such file exists.");
		}
	}
	
	public void showBest()
	{
		String tempString = "";
		String rForString = "";
		int secCon, minCon, hourCon;
		int count = 0;
		
		for (int i = 0;i < 10;i++)
		{
			if (times[i] > 0)
			{
				secCon = times[i] % 100;
				minCon = (times[i] / 100) % 10000;
				hourCon = times[i] / 10000;
				
				tempString = String.format("%02d",hourCon) +":"+ String.format("%02d",minCon) +":"+ String.format("%02d",secCon);
				
				rForString += tempString +"\n";
				
				count++;
			}
			else if(times[i]==0)
			{
				//rForString += "No time\n";
			}
		}
		
			int toWhere = 10 - count;
			
			if (count < 10)
			{
				for (int j =1; j<=toWhere; j++)
				{
					rForString += "No time\n";
				}
			}
			
			JOptionPane.showMessageDialog(null, rForString,  "Top 10 Scores: Large/Hard", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showBestModerate()
	{
		String tempString = "";
		String rForString = "";
		int secCon, minCon, hourCon;
		int count = 0;
		
		for (int i = 0;i < 10;i++)
		{
			if (timesModerate[i] > 0)
			{
				secCon = timesModerate[i] % 100;
				minCon = (timesModerate[i] / 100) % 10000;
				hourCon = timesModerate[i] / 10000;
				
				tempString = String.format("%02d",hourCon) +":"+ String.format("%02d",minCon) +":"+ String.format("%02d",secCon);
				
				rForString += tempString +"\n";
				
				count++;
			}
			else if(timesModerate[i]==0)
			{
				//rForString += "No time\n";
			}
		}
		
			int toWhere = 10 - count;
			
			if (count < 10)
			{
				for (int j =1; j<=toWhere; j++)
				{
					rForString += "No time\n";
				}
			}
			
			JOptionPane.showMessageDialog(null, rForString,  "Top 10 Scores: Large/Moderate", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showBestEasy()
	{
		String tempString = "";
		String rForString = "";
		int secCon, minCon, hourCon;
		int count = 0;
		
		for (int i = 0;i < 10;i++)
		{
			if (timesEasy[i] > 0)
			{
				secCon = timesEasy[i] % 100;
				minCon = (timesEasy[i] / 100) % 10000;
				hourCon = timesEasy[i] / 10000;
				
				tempString = String.format("%02d",hourCon) +":"+ String.format("%02d",minCon) +":"+ String.format("%02d",secCon);
				
				rForString += tempString +"\n";
				
				count++;
			}
			else if(timesEasy[i]==0)
			{
				//rForString += "No time\n";
			}
		}
		
			int toWhere = 10 - count;
			
			if (count < 10)
			{
				for (int j =1; j<=toWhere; j++)
				{
					rForString += "No time\n";
				}
			}
			
			JOptionPane.showMessageDialog(null, rForString,  "Top 10 Scores: Large/Easy", JOptionPane.INFORMATION_MESSAGE);
	}
	

	
	public void StopWatch()
	{
        theChronometer =
        new Timer(1000,new ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			int seconds = (int)(System.currentTimeMillis()-watchStart)/1000;
        			int days = seconds / 86400;
					int hours = (seconds / 3600) - (days * 24);
					int min = (seconds / 60) - (days * 1440) - (hours * 60);
					int sec = seconds % 60;
        			String s = new String(""+String.format("%02d", hours)+":"+String.format("%02d", min)+":"+String.format("%02d", sec));
					
					String timeString = new String(""+String.format("%02d", hours) + String.format("%02d", min)+String.format("%02d", sec));
					timeSort = Integer.parseInt(timeString);
					
					if (paused == false)
        	           displayTimeLabel.setText(s);
        		}
        });
    }
	
	public void StopWatchFlicker()
	{
        theChronometerTwo =
        new Timer(1000,new ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			int seconds = (int)(System.currentTimeMillis()-watchStartFlicker)/1000;
        			//int days = seconds / 86400;
					//int hours = (seconds / 3600) - (days * 24);
					//int min = (seconds / 60) - (days * 1440) - (hours * 60);
					int sec = seconds % 60;
        			//String s = new String(""+String.format("%02d", hours)+":"+String.format("%02d", min)+":"+String.format("%02d", sec));
					
					//String timeString = new String(""+hours + min + sec);
					//timeSort = Integer.parseInt(timeString);
					
					
					
					
					
					
					
					if (secPrev != sec)
					{
					secPrev = sec;
					if (pausedFlicker == false)
					{
						if (joker == 1)
						{
							try
							{
														
									BufferedImage mineImage = ImageIO.read(new File("Recourses/cry.png"));
									Image newMine = mineImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									//mineImage.getImage().flush();
			
									joker = 2;
									
									//tempLabel.setIcon(newMine);
									smile.setIcon(new ImageIcon(newMine));
									
									
									for (int r=1; r<ROWS+1; r++)
										for (int c=1; c<COLS+1; c++)
										{
											if (field[r][c].isBomb() == false && field[r][c].isRevealed() == false)
											{
												//grid[r][c].setIcon(new ImageIcon(newMine));
												grid[r][c].setText("!");
											}
										}
										
			
									revalidate();
									repaint();
								
							}
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
							
							
						}
						else
						{
							try
							{
								
									BufferedImage mineImageOther = ImageIO.read(new File("Recourses/sad.png"));
									Image newMineOther = mineImageOther.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									//mineImageOther.getImage().flush();
			
									//tempLabel.setIcon(newMineOther);
									
									smile.setIcon(new ImageIcon(newMineOther));
									
									
									for (int r=1; r<ROWS+1; r++)
										for (int c=1; c<COLS+1; c++)
										{
											if (field[r][c].isBomb() == false && field[r][c].isRevealed() == false)
											{
												//grid[r][c].setIcon(null);
												grid[r][c].setText(" ");
											}
										}
									
									
			
									joker = 1;
			
									revalidate();
									repaint();
								
							}
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
							
							
						}
        	           //displayTimeLabel.setText(s);
					   validate();
					   repaint();
					}
					}
				   
				       
        		}
        });
    }
	
	
	
	public void StopWatchFlickerSmile()
	{
        theChronometerTwo =
        new Timer(1000,new ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			int seconds = (int)(System.currentTimeMillis()-watchStartFlicker)/1000;
        			//int days = seconds / 86400;
					//int hours = (seconds / 3600) - (days * 24);
					//int min = (seconds / 60) - (days * 1440) - (hours * 60);
					int sec = seconds % 60;
        			//String s = new String(""+String.format("%02d", hours)+":"+String.format("%02d", min)+":"+String.format("%02d", sec));
					
					//String timeString = new String(""+hours + min + sec);
					//timeSort = Integer.parseInt(timeString);
					
						
					if (secPrev != sec)
					{
					secPrev = sec;
					if (pausedFlicker == false)
					{
						if (joker == 1)
						{
							try
							{
														
									BufferedImage loveImage = ImageIO.read(new File("Recourses/love.png"));
									Image newLove = loveImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									//mineImage.getImage().flush();
			
									joker = 2;
									
									//tempLabel.setIcon(newMine);
									smile.setIcon(new ImageIcon(newLove));
									
									/*
									for (int r=1; r<ROWS+1; r++)
										for (int c=1; c<COLS+1; c++)
										{
											if (field[r][c].isBomb() == false && field[r][c].isRevealed() == false)
											{
												grid[r][c].setIcon(new ImageIcon(newMine));
											}
										}
										*/
			
									revalidate();
									repaint();
								
							}
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
							
							
						}
						else
						{
							try
							{
								
									BufferedImage mineImageOther = ImageIO.read(new File("Recourses/win.png"));
									Image newMineOther = mineImageOther.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									//mineImageOther.getImage().flush();
			
									//tempLabel.setIcon(newMineOther);
									
									smile.setIcon(new ImageIcon(newMineOther));
									
									/*
									for (int r=1; r<ROWS+1; r++)
										for (int c=1; c<COLS+1; c++)
										{
											if (field[r][c].isBomb() == false && field[r][c].isRevealed() == false)
											{
												grid[r][c].setIcon(null);
											}
										}
									*/
									
			
									joker = 1;
			
									revalidate();
									repaint();
								
							}
							catch (FileNotFoundException ex)
							{
							}
							catch (IOException ex)
							{
							}
							
							
						}
        	           //displayTimeLabel.setText(s);
					   validate();
					   repaint();
					}
					}
				   
				       
        		}
        });
    }
	
	
	//Menu bar and events
	private void createMenuBar() 
	{
        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        //file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((ActionEvent eventExit) -> {
			playRightSound();
            System.exit(0);
        });

        file.add(eMenuItem);

        menubar.add(file);

        setJMenuBar(menubar);
		
		JMenu size = new JMenu("Size");
        //size.setMnemonic(KeyEvent.VK_S);
		
		JMenuItem sMenuItem = new JMenuItem("Small");
        //sMenuItem.setMnemonic(KeyEvent.VK_S);
        sMenuItem.setToolTipText("Small Minesweeper");
        sMenuItem.addActionListener((ActionEvent eventSmall) -> 
		{
			playRightSound();
            //System.exit(0);
			ROWS = 5;
			COLS = 4;
			//BOMBS = 4;
			sizeGame = 1;
			
			if (difficulty == 2)
			   BOMBS = 4;
		    else if (difficulty ==1)
				BOMBS = 2;
			else if (difficulty == 3)
				BOMBS = 6;
			
			WonOrLost = false;
			
			this.frame.setSize (550,450);
			
			removeGrid();
			mainMoved();
        });
		
		JMenuItem mMenuItem = new JMenuItem("Medium");
        //mMenuItem.setMnemonic(KeyEvent.VK_M);
        mMenuItem.setToolTipText("Medium Minesweeper");
        mMenuItem.addActionListener((ActionEvent eventMedium) -> 
		{
			playRightSound();
            //System.exit(0);
			ROWS = 9;
			COLS = 7;
			//BOMBS = 7;
			sizeGame = 2;
			
			if (difficulty == 2)
			   BOMBS = 7;
		    else if (difficulty ==1)
				BOMBS = 4;
			else if (difficulty == 3)
				BOMBS = 10;
			
			WonOrLost = false;
			
			this.frame.setSize (650,550);
			
			removeGrid();
			mainMoved();
			
        });
		
		JMenuItem lMenuItem = new JMenuItem("Large");
        //lMenuItem.setMnemonic(KeyEvent.VK_L);
        lMenuItem.setToolTipText("Large Minesweeper");
        lMenuItem.addActionListener((ActionEvent eventLarge) -> 
		{
			playRightSound();
            //System.exit(0);
			ROWS = 13;
			COLS = 11;
			sizeGame = 3;
			
			if (difficulty == 2)
			   BOMBS = 10;
		    else if (difficulty ==1)
				BOMBS = 7;
			else if (difficulty == 3)
				BOMBS = 13;
			
			WonOrLost = false;
			
			this.frame.setSize (850,750);
			
			removeGrid();
			mainMoved();
        });

        size.add(sMenuItem);
		size.add(mMenuItem);
		size.add(lMenuItem);

        menubar.add(size);
		
		JMenu diffi = new JMenu("Difficulty");
        file.setMnemonic(KeyEvent.VK_D);

        JMenuItem easyMenuItem = new JMenuItem("Easy");
        //eMenuItem.setMnemonic(KeyEvent.VK_E);
        easyMenuItem.setToolTipText("Only a few bombs");
        easyMenuItem.addActionListener((ActionEvent eventEasy) -> 
		{
			playRightSound();
            //System.exit(0);
			
			difficulty = 1;
			
			if (sizeGame == 2)
				BOMBS = 4;
			else if(sizeGame == 1)
				BOMBS = 2;
			else if(sizeGame == 3)
				BOMBS = 6;
			
			WonOrLost = false;
			
			removeGrid();
			mainMoved();
        });

        diffi.add(easyMenuItem);
		
		JMenuItem modMenuItem = new JMenuItem("Moderate");
        //eMenuItem.setMnemonic(KeyEvent.VK_E);
        modMenuItem.setToolTipText("Regular amount of bombs");
        modMenuItem.addActionListener((ActionEvent eventMod) -> 
		{
			playRightSound();
            //System.exit(0);
			
			difficulty = 2;
			
			if (sizeGame == 2)
				BOMBS = 7;
			else if(sizeGame == 1)
				BOMBS = 4;
			else if(sizeGame == 3)
				BOMBS = 10;
			
			WonOrLost = false;
			
			removeGrid();
			mainMoved();
        });

        diffi.add(modMenuItem);
		
		JMenuItem hardMenuItem = new JMenuItem("Hard");
        //eMenuItem.setMnemonic(KeyEvent.VK_E);
        hardMenuItem.setToolTipText("Lots of bombs!");
        hardMenuItem.addActionListener((ActionEvent eventHard) -> 
		{
			playRightSound();
            //System.exit(0);
			
			difficulty =3;
			
			if (sizeGame == 2)
				BOMBS = 10;
			else if(sizeGame == 1)
				BOMBS = 7;
			else if(sizeGame == 3)
				BOMBS = 13;
			
			WonOrLost = false;
			
			removeGrid();
			mainMoved();
        });

        diffi.add(hardMenuItem);

        menubar.add(diffi);
		
		
		
		JMenu score = new JMenu("Scores");
        //help.setMnemonic(KeyEvent.VK_H);

        JMenuItem scoreMenuItem = new JMenuItem("Top 10 Large/Hard");
        //hMenuItem.setMnemonic(KeyEvent.VK_E);
        scoreMenuItem.setToolTipText("Display top 10 scores on the large/hard setting");
        scoreMenuItem.addActionListener((ActionEvent eventHelp) -> 
		{
			playRightSound();
            showBest();
		
        });

        score.add(scoreMenuItem);
		
		
		JMenuItem scoreMediumMenuItem = new JMenuItem("Top 10 Large/Moderate");
        //hMenuItem.setMnemonic(KeyEvent.VK_E);
        scoreMediumMenuItem.setToolTipText("Display top 10 scores on the large/medium setting");
        scoreMediumMenuItem.addActionListener((ActionEvent eventHelp) -> 
		{
			playRightSound();
            showBestModerate();
		
        });

        score.add(scoreMediumMenuItem);
		
		
		JMenuItem scoreEasyMenuItem = new JMenuItem("Top 10 Large/Easy");
        //hMenuItem.setMnemonic(KeyEvent.VK_E);
        scoreEasyMenuItem.setToolTipText("Display top 10 scores on the large/easy setting");
        scoreEasyMenuItem.addActionListener((ActionEvent eventHelp) -> 
		{
			playRightSound();
            showBestEasy();
		
        });

        score.add(scoreEasyMenuItem);
		

        menubar.add(score);	
		
		
		JMenu theme = new JMenu("Themes");
        //help.setMnemonic(KeyEvent.VK_H);

        JMenuItem themeMenuItem = new JMenuItem("Light");
        themeMenuItem.setToolTipText("Light themed Minesweeper");
        themeMenuItem.addActionListener((ActionEvent eventHelp) -> 
		{
			playRightSound();
			lightTheme();
        });
		
		JMenuItem darkMenuItem = new JMenuItem("Dark");
        darkMenuItem.setToolTipText("Dark themed Minesweeper");
        darkMenuItem.addActionListener((ActionEvent eventHelp) -> 
		{
			playRightSound();
			darkTheme();
        });


        theme.add(themeMenuItem);
		theme.add(darkMenuItem);

        menubar.add(theme);
		
		
		JMenu help = new JMenu("Help");
        //help.setMnemonic(KeyEvent.VK_H);

        JMenuItem hMenuItem = new JMenuItem("Document");
        //hMenuItem.setMnemonic(KeyEvent.VK_E);
        hMenuItem.setToolTipText("Load PDF");
        hMenuItem.addActionListener((ActionEvent eventHelp) -> 
		{
			playRightSound();
            //System.exit(0);
			if (Desktop.isDesktopSupported()) {
			try 
			{
				File myFile = new File("Recourses/help.pdf");
				Desktop.getDesktop().open(myFile);
			} 
			catch (IOException ex) 
			{
        // no application registered for PDFs
			}
		}
        });

        help.add(hMenuItem);

        menubar.add(help);	
    }
	
	
	public void lightTheme()
	{
		lightThemeBool = true;
		
		if (revealedWin == 0)
		{
		gridPanel.setBackground(LIGHT_BLUE);
		
		pnlBelow.setForeground(LIGHT_BLUE);
		pnlButton.setForeground(LIGHT_BLUE);
		pnlBelow.setBackground(LIGHT_BLUE);
		pnlButton.setBackground(LIGHT_BLUE);
		lblBombCount.setBackground(LIGHT_BLUE);
		displayTimeLabel.setBackground(LIGHT_BLUE);
		smile.setBackground(LIGHT_BLUE);
		displayTimeLabel.setForeground(BACK_BLUE);
		lblBombCount.setForeground(BACK_BLUE);
		
		for (int i=0; i<ROWS+2; i++)
			for (int j=0; j<COLS+2; j++)
			{
				grid[i][j].setBackground(LIGHT_GRAY_BUTTONS);
			}
		}

	}
	
	
	public void darkTheme()
	{
		lightThemeBool = false;
		
		if (revealedWin == 0)
		{
		gridPanel.setBackground(BACK_BLUE);
		
		pnlBelow.setForeground(BACK_BLUE);
		pnlButton.setForeground(BACK_BLUE);
		pnlBelow.setBackground(BACK_BLUE);
		pnlButton.setBackground(BACK_BLUE);
		lblBombCount.setBackground(BACK_BLUE);
		displayTimeLabel.setBackground(BACK_BLUE);
		smile.setBackground(BACK_BLUE);
		displayTimeLabel.setForeground(VERY_LIGHT_GRAY);
		lblBombCount.setForeground(VERY_LIGHT_GRAY);
		
		for (int i=0; i<ROWS+2; i++)
			for (int j=0; j<COLS+2; j++)
			{
				grid[i][j].setBackground(Color.black);
			}
		}
	}
	
	
	//Button handler for smiley emoji button
	private class ButtonHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			playButtonSound();
			if 	(smile == event.getSource() && playedAtLeastOnce == true)
					{
						//JOptionPane.showMessageDialog(null, "Smile!",  "Minesweeper", JOptionPane.INFORMATION_MESSAGE);
						WonOrLost = false;
						removeGrid();
						mainMoved();	
					}
		}
	}		

	private class MouseHandler implements MouseListener
	{
		@Override  
		public void mouseClicked(MouseEvent me) 
		{
			if (me.getButton() == MouseEvent.BUTTON1 && WonOrLost == false)
			{
				
				
				if (started == false)
				{
					paused = false;
					StopWatch();
					watchStart = System.currentTimeMillis();
					theChronometer.start();
					started = true;
				}
				
				playedAtLeastOnce = true;
				
				Object o = me.getSource();
				//tempLabel= new JLabel("@");
				for (int r=1; r<ROWS+1; r++)
					for (int c=1; c<COLS+1; c++)
					{
						if 	(grid[r][c] == (JButton) o)
						{
						if(field[r][c].isFlagged() == false)
						{
							if (field[r][c].isBomb())
							{
								MineImages();
								WonOrLost = true;
								
					            paused = true;
								theChronometer.stop();
								
								playLoseSound();
								
							
								//JOptionPane.showMessageDialog(null, "You lose!", "Minesweeper", JOptionPane.INFORMATION_MESSAGE);
								//System.exit(0);
							}
							else
							{
								playButtonSound();
								revealRec(r, c);
							
						    	if (ROWS*COLS-BOMBS == revealedWin)
							    {
									Winner();
									theChronometer.stop();
									paused = true;
									
									//JOptionPane.showMessageDialog(null, "You win!",  "Minesweeper", JOptionPane.INFORMATION_MESSAGE);
									WonOrLost = true;
									
									//System.exit(0);
								}
							

							}
						}
						}
					}
					
					
			}

			if (me.getButton() == MouseEvent.BUTTON3 && WonOrLost == false)
			{
				Object o= me.getSource();
				playedAtLeastOnce = true;
				tempLabel= new JLabel("@");
				for (int r=1; r<ROWS+1; r++)
					for (int c=1; c<COLS+1; c++)
					{
						if 	(grid[r][c] == (JButton) o)
						{
							playRightSound();
							gbc.gridx = r;
							gbc.gridy = c;
							if (field[r][c].isFlagged() == false && flags < BOMBS)
							{
								field[r][c].setFlagged(true);
								//grid[r][c].setText(field[r][c].toString());
								flags++;
								
								if (started == false)
								{
									paused = false;
									StopWatch();
									watchStart = System.currentTimeMillis();
									theChronometer.start();
									started = true;
								}
								
								try
								{
									BufferedImage flagImage = ImageIO.read(new File("Recourses/flag.png"));
									Image newFlag = flagImage.getScaledInstance( 48, 48,  java.awt.Image.SCALE_SMOOTH );
									grid[r][c].setIcon(new ImageIcon(newFlag));
								}
								catch (FileNotFoundException ex)
								{
								}
								catch (IOException ex)
								{
								}
								
								if (field[r][c].isBomb())
							    {
								   bombWin++;
							    }
							}
							
							else if (field[r][c].isFlagged()==true)
							{
								flags--;
								field[r][c].setFlagged(false);
								//grid[r][c].setText(" ");
								grid[r][c].setIcon(null);
								
								if (field[r][c].isBomb())
							    {
								   bombWin--;
							    }
							}
							
							if (bombWin==BOMBS)
							{
								paused = true;
								theChronometer.stop();
								Winner();
								//JOptionPane.showMessageDialog(null, "You win!", "Minesweeper", JOptionPane.INFORMATION_MESSAGE);
								WonOrLost = true;
								
								
								//System.exit(0);
							}
							
							revalidate();
							repaint();
						}
					}
			}	 
		 }
		 public void mousePressed(MouseEvent me) {}
		 public void mouseReleased(MouseEvent me) {}
		 public void mouseEntered(MouseEvent me) {}
		 public void mouseExited(MouseEvent me) {}
	}		
}