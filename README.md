# Minesweeper
- zh_CN [简体中文](README.zh_CN.md)
- en [English](README.md)

## Task Description

Implement the Minesweeper game using the Processing library for graphics and gradle as a dependency manager. You can access the documentation from here. 

Each tile in the 18x27 grid is initially blue (hidden), and may contain a mine. Your program must accept a parameter from command line arguments, which is the number of mines that should be spawned randomly on the board. If no parameter is provided, or an invalid one is, then the default is 100 mines.


The player can left click on a tile to reveal it. If there is a mine, all mines will begin exploding and the game ends. Display the message “You Lost!” in the top bar. The mines explode in a cascade/progressive manner, where each explosion will begin 3 frames after the previous one began.

The mine animation image sequence is provided in the scaffold, comprising of the 10 files “mine0.png” to “mine9.png”.

If there isn’t a mine on that tile, the tile will show a number representing how many adjacent mines there are. The colours for these numbers are given to you in an int[] array for RGB values in the scaffold, mineCountColour. If there are no adjacent mines, it remains blank, and all adjacent tiles will also be revealed. If all non-mine tiles are revealed, the game ends. Display the message “You win!”. The player can right click on a tile to flag or unflag it. If flagged, it cannot be left-clicked to reveal.

A timer in the top-right corner records how many seconds elapsed since the game began. The player can press ‘r’ to restart the game. 

