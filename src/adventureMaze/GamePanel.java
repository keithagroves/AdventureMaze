package adventureMaze;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
	
	private Timer timer;
	public GameObject game;
	private LevelManager level;
	private Font titleFont, startFont, instructionsFont, gameOverFont, scoreFont;
	private Player player;
	public ObjectManager manager;
	private final int MENU_STATE = 0, GAME_STATE = 1, END_STATE = 2, WIN_STATE = 3;
	private int currentState = MENU_STATE, currentLevel = 1;
	public static BufferedImage playerImg, enemyImg, bulletImg, menuBkgndImg, endBkgndImg, gameBkgndImg, wallImg,
			winBkgndImg;
	public static int playerX, playerY, enemySpeed;
	
	public GamePanel() {
		
		timer = new Timer(1000 / 60, this);
		game = new GameObject();
		titleFont = new Font("Arial", Font.BOLD, 64);
		startFont = new Font("Arial", Font.PLAIN, 30);
		instructionsFont = new Font("Arial", Font.PLAIN, 20);
		gameOverFont = new Font("Arial", Font.BOLD, 48);
		scoreFont = new Font("Arial", Font.PLAIN, 20);
		player = new Player(11, 720, 35, 35, 5);
		manager = new ObjectManager();
		manager.addPlayer(player);
		playerX = player.x;
		playerY = player.y;
		enemySpeed = 3;
		level = new LevelManager();
		level.createLevel1(manager);

		try {
			playerImg = ImageIO.read(this.getClass().getResourceAsStream("images/PlayerImg.png"));
			enemyImg = ImageIO.read(this.getClass().getResourceAsStream("images/EnemyImg.png"));
			bulletImg = ImageIO.read(this.getClass().getResourceAsStream("images/BulletImg.png"));
			wallImg = ImageIO.read(this.getClass().getResourceAsStream("images/WallImg.png"));
			menuBkgndImg = ImageIO.read(this.getClass().getResourceAsStream("images/MenuBackground.png"));
			endBkgndImg = ImageIO.read(this.getClass().getResourceAsStream("images/EndImg.png"));
			gameBkgndImg = ImageIO.read(this.getClass().getResourceAsStream("images/GameImg.png"));
			winBkgndImg = ImageIO.read(this.getClass().getResourceAsStream("images/WinImg.png"));

		} catch (IOException e) {
			// TODO: handle exceptions
			e.printStackTrace();
		}

	}

	void updateMenuState() {

	}

	void updateGameState() {
		playerX = player.x;
		playerY = player.y;
		manager.update();
		manager.manageEnemies();
		manager.checkCollision();
		if (player.isAlive == false) {
			currentState = END_STATE;
			manager.reset();
			player = new Player(11, 720, 35, 35, 5);
			manager.addPlayer(player);
		}
		if (player.x > 700 && player.y < 40) {
			currentState = WIN_STATE;
		}
	}

	void updateEndState() {

	}

	void updateWinState() {

	}

	void drawMenuState(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, 780, 780);
		g.drawImage(menuBkgndImg, 0, 0, 780, 780, null);
		g.setFont(titleFont);
		g.setColor(Color.BLACK);
		g.drawString("Adventure Maze", 50, 100);
		g.setFont(startFont);
		g.setColor(Color.BLACK);
		g.drawString("Press ENTER to start", 160, 200);
		// Instructions header background box
		g.setColor(new Color(255, 215, 175));
		g.fillRect(350, 300, 200, 30);
		// Instructions header
		g.setColor(Color.BLACK);
		g.setFont(instructionsFont);
		g.drawString("Instructions:", 395, 320);
		// Instructions Background box
		g.setColor(new Color(255, 215, 175));
		g.fillRect(200, 340, 500, 200);
		// Instructions text
		g.setColor(Color.black);
		g.drawString("Use the ARROW KEYS to control your character", 230, 370);
		g.drawString("Press the SPACE BAR to shoot projectiles at enemies", 210, 395);
		g.drawString("Get to the end of the maze to win", 295, 445);
		g.drawString("If an enemy touches you, you lose", 290, 420);
		g.drawString("Press E, M, H, or L to choose the difficulty", 255, 470);
		g.drawString("E = Easy, M = Medium, H = Hard, or L = Ludicrus", 230, 495);
		g.drawString("Enter a number 1 to 4 to choose the level", 255, 520);

		g.setColor(Color.black);
		g.drawString("Current Level: " + currentLevel, 550, 650);
	}

	void drawGameState(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 780, 780);
		g.drawImage(GamePanel.gameBkgndImg, 0, 0, 780, 780, null);
		g.setColor(Color.GREEN);
		g.fillRect(710, 10, 60, 60);
		g.setColor(Color.GRAY);
		g.fillRect(10, 710, 60, 60);
		g.setColor(Color.black);
		g.drawString("Level " + currentLevel, 720, 750);
		manager.draw(g);
	}

	void drawEndState(Graphics g) {
		g.setColor(new Color(216, 91, 91));
		g.fillRect(0, 0, 780, 780);
		g.drawImage(endBkgndImg, 0, 0, 780, 780, null);
		g.setFont(gameOverFont);
		g.setColor(Color.BLACK);
		g.drawString("Game Over", 255, 200);
		g.setFont(scoreFont);
		g.setColor(Color.BLACK);
		g.drawString("Press ENTER to try again", 275, 420);
	}

	void drawWinState(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, 780, 780);
		g.drawImage(winBkgndImg, 0, 0, 780, 780, null);
		g.setFont(gameOverFont);
		g.setColor(Color.BLACK);
		g.drawString("Congratulations!", 100, 110);
		g.drawString("You won!", 170, 170);
		g.setFont(scoreFont);
		g.setColor(Color.BLACK);
		g.drawString("Press ENTER to play again", 445, 630);
		g.setColor(new Color(142, 225, 255));
		g.fillRect(475, 475, 190, 40);
		g.setColor(Color.BLACK);
		g.drawString("You beat Level " + currentLevel, 495, 500);

	}

	void startGame() {
		timer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		if (currentState == MENU_STATE) {
			drawMenuState(g);
		} else if (currentState == GAME_STATE) {
			drawGameState(g);
		} else if (currentState == END_STATE) {
			drawEndState(g);
		} else if (currentState == WIN_STATE) {
			drawWinState(g);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (currentState == MENU_STATE) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				currentState++;
				switch (currentLevel) {
				case 1:
					level.createLevel1(manager);
					break;
				case 2:
					level.createLevel2(manager);
					break;
				case 3:
					level.createLevel3(manager);
					break;
				case 4:
					level.createLevel4(manager);
					break;
				}
				break;
			case KeyEvent.VK_E:
				enemySpeed = 2;
				break;
			case KeyEvent.VK_M:
				enemySpeed = 3;
				break;
			case KeyEvent.VK_H:
				enemySpeed = 5;
				break;
			case KeyEvent.VK_L:
				enemySpeed = 8;
				break;
			case KeyEvent.VK_1:
				currentLevel = 1;
				break;
			case KeyEvent.VK_2:
				currentLevel = 2;
				break;
			case KeyEvent.VK_3:
				currentLevel = 3;
				break;
			case KeyEvent.VK_4:
				currentLevel = 4;
				break;

			}
		}

		if (currentState == END_STATE) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				currentState = MENU_STATE;
			}
		}

		if (currentState == WIN_STATE) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				currentState = MENU_STATE;
			}
		}

		if (currentState == GAME_STATE) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				player.up = true;
				Projectile.lastMove = 0;
				break;
			case KeyEvent.VK_RIGHT:
				player.right = true;
				Projectile.lastMove = 1;
				break;
			case KeyEvent.VK_DOWN:
				player.down = true;
				Projectile.lastMove = 2;
				break;
			case KeyEvent.VK_LEFT:
				player.left = true;
				Projectile.lastMove = 3;
				break;
			case KeyEvent.VK_SPACE:
				manager.addProjectile(new Projectile(player.x + 15, player.y + 15, 10, 10));
				break;
			}

		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			player.up = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.down = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.left = false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (currentState == MENU_STATE) {
			updateMenuState();
		} else if (currentState == GAME_STATE) {
			updateGameState();
		} else if (currentState == END_STATE) {
			updateEndState();
			manager.reset();
			player = new Player(11, 720, 35, 35, 5);
			manager.addPlayer(player);
		} else if (currentState == WIN_STATE) {
			updateWinState();
			manager.reset();
			player = new Player(11, 720, 35, 35, 5);
			manager.addPlayer(player);
		}

		repaint();
	}

}
