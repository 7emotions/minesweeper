package minesweeper;

import processing.core.PConstants;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

public class Tile {

    private boolean revealed;
    private int x;
    private int y;

    private boolean mine; //feel free to change this
    private boolean flagged = false;
    private boolean exploded = false;
    private int explosion_duration = 0;

    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    public boolean isExploded() {
        return exploded;
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.revealed = false;
    }

    public void draw(App app) {
        PImage tile = app.getSprite("tile1");
        
        if (revealed) {
            tile = app.getSprite("tile");
        } else if (app.mouseX >= x*App.CELLSIZE && app.mouseX < (x+1)*App.CELLSIZE &&
                app.mouseY >= y*App.CELLSIZE+App.TOPBAR && app.mouseY < (y+1)*App.CELLSIZE+App.TOPBAR) {
            if (app.mousePressed && app.mouseButton == PConstants.LEFT) {
                tile = app.getSprite("tile");
            } else {
                tile = app.getSprite("tile2");
            }
        }

        if(mine && exploded) {
            if (explosion_duration < 10) {
                app.image(app.getSprite("mine" + explosion_duration), x*App.CELLSIZE, y*App.CELLSIZE+App.TOPBAR);
                if (app.frameCount % 3 == 0) {
                    explosion_duration++;
                }
            } else {
                app.image(app.getSprite("mine9"), x*App.CELLSIZE, y*App.CELLSIZE+App.TOPBAR);
                if (explosion_duration++ == 10) {
                    Tile next = app.next_mine();
                    if (next != null) {
                        next.setExploded(true);
                    }
                }
            }
            return;
        }

        app.image(tile, x*App.CELLSIZE, y*App.CELLSIZE+App.TOPBAR);
        if (flagged) {
            app.image(app.getSprite("flag"), x*App.CELLSIZE, y*App.CELLSIZE+App.TOPBAR);
        }
        if (this.hasAdjacentEmptyTile(app)/* && app.frameCount % 4 == 0*/) {
            this.revealed = true;
        }

        if (this.revealed && !this.mine) {
            int count = this.countAdjacentMines(app);
            if (count > 0) {
                app.fill(App.mineCountColour[count][0], App.mineCountColour[count][1] , App.mineCountColour[count][2]);
                app.text(count, x*App.CELLSIZE + App.CELLSIZE / 10, y*App.CELLSIZE+App.CELLSIZE * 4 / 5+App.TOPBAR);
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasMine() {
        return this.mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void reveal() {
        if (flagged) {
            return;
        }
        this.revealed = true;
    }

    public boolean isFlagged() {
       return this.flagged;
    }

    public void toggleFlag() {
        flagged = !flagged;
    }

    public boolean isRevealed() {
        return this.revealed;
    }

    public List<Tile> getAdjacentTiles(App app) {
        ArrayList<Tile> result = new ArrayList<>();
        if (x+1 < app.getBoard()[y].length) {
            result.add(app.getBoard()[y][x+1]);
        }
        if (y+1 < app.getBoard().length && x+1 < app.getBoard()[y].length) {
            result.add(app.getBoard()[y+1][x+1]);
        }
        if (y-1 >= 0 && x+1 < app.getBoard()[y].length) {
            result.add(app.getBoard()[y-1][x+1]);
        }
        if (y+1 < app.getBoard().length) {
            result.add(app.getBoard()[y+1][x]);
        }
        if (y-1 >= 0) {
            result.add(app.getBoard()[y-1][x]);
        }
        if (x-1 >= 0) {
            result.add(app.getBoard()[y][x-1]);
        }
        if (x-1 >= 0 && y+1 < app.getBoard().length) {
            result.add(app.getBoard()[y+1][x-1]);
        }
        if (x-1 >= 0 && y-1 >= 0) {
            result.add(app.getBoard()[y-1][x-1]);
        }
        return result;
    }

    public boolean hasAdjacentEmptyTile(App app) {
        for (Tile t : getAdjacentTiles(app)) {
            if (t.revealed && t.countAdjacentMines(app) == 0) { //ensure the cell has no adjacent mines here
                return true;
            }
        }
        return false;
    }

    public int countAdjacentMines(App app) {
        if (this.hasMine()) {
            return -1;
        }
        int count = 0;
        for (Tile t : getAdjacentTiles(app)) {
            if (t.hasMine()) {
                count++;
            }
        }
        return count;
    }
}
