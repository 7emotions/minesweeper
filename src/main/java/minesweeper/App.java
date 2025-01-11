package minesweeper;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 64;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int FPS = 30;

    public int mineCount = 100;

    public String configPath;

    public static Random random = new Random();
	
	public static int[][] mineCountColour = new int[][] {
            {0,0,0}, // 0 is not shown
            {0,0,255},
            {0,133,0},
            {255,0,0},
            {0,0,132},
            {132,0,0},
            {0,132,132},
            {132,0,132},
            {32,32,32}
    };

    public boolean isGameOver = false;
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    public App(int mineCount) {
        this.configPath = "config.json";
        this.mineCount = mineCount;
    }

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    private Tile[][] board;
    private ArrayList<Tile> mines = new ArrayList<>();
    private int sec = 0;
    private HashMap<String, PImage> sprites = new HashMap<>();

    public PImage getSprite(String s) {
        PImage result = sprites.get(s);
        if (result == null) {
            result = loadImage(this.getClass().getResource(s+".png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
            sprites.put(s, result);
        }
        return result;
    }

    public Tile[][] getBoard() {
        return this.board;
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        
		frameRate(FPS);
		//See PApplet javadoc:
		//loadJSONObject(configPath)
		//loadImage(this.getClass().getResource(filename).getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        String[] sprites = new String[] {
                "tile1",
                "tile2",
                "flag",
                "tile"
        };
        for (int i = 0; i < sprites.length; i++) {
            getSprite(sprites[i]);
        }
        for (int i = 0; i < 10; i++) {
            getSprite("mine"+String.valueOf(i));
        }

        //create attributes for data storage, eg board
        this.board = new Tile[(HEIGHT-TOPBAR)/CELLSIZE][WIDTH/CELLSIZE];

        for (int i = 0; i < this.board.length; i++) {
            for (int i2 = 0; i2 < this.board[i].length; i2++) {
                this.board[i][i2] = new Tile(i2, i);
            }
        }

        for (int i = 0; i < mineCount; i++) {
            int y = random.nextInt(this.board.length);
            int x = random.nextInt(this.board[y].length);
            if (!this.board[y][x].hasMine()) {
                this.board[y][x].setMine(true);
                mines.add(this.board[y][x]);
            } else {
                i -= 1;
            }
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        if (event.getKey() == 'r') {
            this.isGameOver = false;
            this.mines.clear();
            this.setup();
            frameCount = 0;
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
        
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isGameOver) {
            return;
        }
        int mouseX = e.getX();
        int mouseY = e.getY();
        mouseY -= App.TOPBAR;
        if (mouseX >= 0 && mouseX < WIDTH && mouseY >= 0 && mouseY < HEIGHT) {
            Tile t = board[mouseY/App.CELLSIZE][mouseX/App.CELLSIZE];
            if (e.getButton() == 39) {
                if (!t.isRevealed()) {
                    t.toggleFlag();
                }
            } else if (e.getButton() == 37) {
                if (!t.isFlagged()) {
                    t.reveal();
                }
            }
        }

    }

    public int getUnExplodedMines() {
        return mines.size();
    }

    public Tile next_mine() {
        for (Tile tile : mines) {
            if (!tile.isExploded()) {
                tile.setExploded(true);
                return tile;
            }
        }
        return null;
    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        //draw game board
        background(200,200,200);
        int nonRevealedNonMines = 0;
        for (int i = 0; i < this.board.length; i++) {
            for (int i2 = 0; i2 < this.board[i].length; i2++) {
                this.board[i][i2].draw(this);
                if (!this.board[i][i2].isRevealed() && !this.board[i][i2].hasMine()) {
                    nonRevealedNonMines += 1;
                }
                if (this.board[i][i2].hasMine() && this.board[i][i2].isRevealed()) {
                    textSize(30);
                    fill(0);
                    text("You lost!", 150, App.TOPBAR-10);
                    this.board[i][i2].setExploded(true);
                    isGameOver = true;
                }
            }
        }


        if (nonRevealedNonMines == 0) {
            textSize(30);
            fill(0);
            text("You win!", 150, App.TOPBAR-10);
            isGameOver = true;
        }
        if (!isGameOver){
            sec =  frameCount / FPS;
        }
        textSize(30);
        fill(0);
        text("Time: " + sec + " s", WIDTH-200, App.TOPBAR-10);
    }


    public static void main(String[] args) {
        String[] applet = new String[] {App.class.getName()};
        if (args.length == 1) {
            App myApp = new App(Integer.parseInt(args[0]));
            PApplet.runSketch(applet, myApp);
        } else {
            PApplet.main(applet);
        }
    }

}
