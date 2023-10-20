Game engine was provided by Professor Phillips to students enrolled in his CSC 133 class.

Instructions and Notes for CSC 131 Project
------------------------------------------------------------------------------------------------------------------------

Title Screen:
On the title screen is the name of the game, a yellow load game button in the top left, a play game button, a view
leaderboard button, and a quit button.  The load game button will load the game if there is saved data.  The play game
button will start the game.  The view leaderboard button will open a menu that shows the top 10 times (an exit button is
in the top right the exit the leaderboard).  The quit button quits the game.

------------------------------------------------------------------------------------------------------------------------

Level 1:
You are in a room with a door in front of you and a puzzle on the left.  Clicking the door will tell you that a puzzle
needs to be completed first.  Clicking the puzzle icon opens a pipe puzzle.  Once the puzzle is solved, an item will
spawn on the screen.  The item can be picked up, moved around, placed in the inventory, or used on the door.  Using the
item on the door removes the item and allows you to move to the next level.

Pipe Puzzle:
The puzzle contains a 6x6 grid of 6 different pipe types.  In the bottom left and bottom right are end points that
cannot be moved.  The goal is to move the pieces around to connect the two end points.  Pieces can be moved by clicking
a piece and then clicking a second piece to swap the pieces.  Once the two ends are connected, a square will appear in
the center that says "puzzle solved".  Click this square to get back to Level1.

------------------------------------------------------------------------------------------------------------------------

Level 2:
You are in an open field with a path in front of you and a puzzle on the left.  Blocking the path is a boulder that
needs to be removed in order to advance.  Clicking the boulder will tell you that a puzzle needs to be completed first.
Clicking the puzzle icon opens a simon says puzzle.  Once the puzzle is solved, an item will spawn on the screen.  The
item can be picked up, moved around, placed in the inventory, or used on the boulder.  Using the item on the boulder
removes the item and allows you to move to the next level.

Simon Says Puzzle:
Once opened, the puzzle will immediately play a sequence of 5 colors.  Once the sequence ends, 4 different colors will
appear below the sequence.  To repeat the sequence, you can click the colors in order.  Once one color is clicked, the
next one can be immediately clicked.  If you need to see the sequence again, you can click the "play sequence" button to
repeat the sequence.  If you enter the sequence incorrectly, the sequence will play again.  When the sequence is entered
correctly, the puzzle will close.

------------------------------------------------------------------------------------------------------------------------

Level 3:
You are at the end of the path.  On the left is a boat and a stick figure.  On the right is an item.  Clicking the boat
tells you to talk to the stick figure first.  Clicking the stick figure gives you a quest where you have to hand in an
item.  The item can be picked up, moved around, placed in the inventory, or given to the stick figure.  Giving the item
to the stick figure completes the quest and allows you to click the boat.  Clicking the boat completes the level and
moves to the finish screen.

------------------------------------------------------------------------------------------------------------------------

Finish Screen:
The finish screen displays a congratulatory message along with the time it took the user to complete the game and a
firework particle.  At this point, the user can click the "add time to leaderboard" button to save the time, click the
"restart" button to restart the game, or the "quit" button to quit the game.  Clicking the "restart" button resets the
state of the game and takes you back to the title screen where you can play the game again.

------------------------------------------------------------------------------------------------------------------------

Note about the sprite sheet:
spriteSheet.png and sprites.txt were created from https://www.leshylabs.com/apps/sstool/
This website takes image files as input and compiles them into a sprite sheet and sprite map text file.  The sprite
map contains the fileName (without .png), x, y, width, height of each sprite on the sprite sheet and formats it in a way
that makes it easy to use with a string tokenizer to load into the backbuffer.


Notes about scripting engine:
All scene backgrounds, except for the FinishScreen and Puzzle backgrounds, are drawn with the scripting engine.

Items are not drawn with the addSpriteToOverlayBuffer scripting engine command because of issues with the item appearing
in front of the hover label.  Instead, the addSpriteToFrontBuffer scripting engine command was used to initially draw
the items when they spawn.  The mouse is drawn with addSpriteToOverlayBuffer, but it is not done through the scripting
engine.

The command to create a RECT object in memory is used to create a RECT for the textBox.  It was not used for other RECT
objects because of issues with interpreting the proper logic for each RECT.

The play music command was used once to play the puzzleComplete song on Level1.  Using it for other levels would result
in an issue where no audio was played.

The start animation command was used for the doorOpen animation on Level1.  It was not used on other levels because of
the delayed start that caused the animation to look weird when running.
