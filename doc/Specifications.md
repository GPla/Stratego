# Stratego Android Game

## General

A simple android game version of the popular strategic game [Stratego](https://en.wikipedia.org/wiki/Stratego). Stratego is two player, round-based game. The game objective is to capture the opponent's flag.

## Board

- 2 Players
- 10x10 Grid
- 2 2x2 Lakes in the middle of the board
- 40 Pieces for each player

## Setup

1. Choose colors (red and blue)
2. Arrange all pieces in 4x10 formation

## Gameplay

- Players alternate moving; red moves first
- Each players moves one piece per turn
- A player must move a piece in his turn
- Pieces cannot be placed or moved into the lakes
- **Game objective**: capture the opponents flag  
- Draw possible

## Rules of movement

- All pieces may move only on step to any adjacent space vertically or horizontally (exception the Scout)
- A piece may not move onto a space occupied by a like-color piece
- Bomb and Flag are not moveable
- The Scout may move any number of spaces in a straight line and can strike and the same round
- No piece can move back and forth between the same to spaces for more than three consecutive turns (two square rule)
- A piece cannot endlessly chase a piece it has no hope of capturing  (more square rule)
- When a player wants to attack, they move their piece onto a square occupied by an opposing piece
- Both, the attacking and defending piece, are revealed; the weaker piece is removed from the board, if they are equal in rank, both are removed
- The Bomb immediately eliminates any other piece striking it, without itself being destroyed; it can only be defused by a Miner
- The Spy only suceeds if it attacks the Marshal or the Flag. If the Spy attacks any other piece, or is attacked by any piece, the Spy is defeated
- The attacking piece may move onto the square of the defeated piece

## Pieces

| Rank |    Piece    |  No. per player |                              Special properties                             |
|------|-------------|-----------------|-----------------------------------------------------------------------------|
| B    |  Bomb       |               6 |  Immovable; defeats any attacking piece execpt Miner                        |
| 10   |  Marshal    |               1 |  Can be captured by the Spy                                                 |
| 9    |  General    |               1 |                                                                             |
| 8    |  Colonel    |               2 |                                                                             |
| 7    |  Major      |               3 |                                                                             |
| 6    |  Captain    |               4 |                                                                             |
| 5    |  Lieutenant |               4 |                                                                             |
| 4    |  Sergant    |               4 |                                                                             |
| 3    |  Miner      |               5 |  Can defuse bombs                                                           |
| 2    |  Scout      |               8 |  Can move any distance in a straight line without leaping over pieces/lakes |
| 1    |  Spy        |               1 |  Can defeat the Marshal but only if the Spy makes the attack                |
| F    |  Flag       |               1 |  Immovable; its capture ends the game                                       |

## Project Structure

- Screens
  - Main menu
  - Game
  - Settings
  - Rules

### Main Menu

- Use scene2d for the UI of the menus

### Game

Game orientation: landscape( ) or portrait(x)?

#### Map

- Tiled map (Orthographic or Isometric?)
- Tile set and art style to be determined
- Size: 14 x 12 (portrait), Game: 10x10
- One tile = one grid square, one piece = one tile
- Top down or 3d like

Optional:

- Animated water tiles
- Animate border tiles (non game tiles)
- [Water reflections](https://gamedev.stackexchange.com/questions/102940/how-to-achieve-sprite-reflection-effect-in-libgdx)  

#### Unit

[Sprite set](https://pipoya.itch.io/pipoya-free-rpg-character-sprites-32x32)  
modify to show rank

Placement:

- All placeable pieces should be shown in a list
- Tap a piece and the square to place it, all available/valid squares should show be highlighted

Movement:

- Tap on a piece and all movable square are highligted
- Tap on a highlighted square to move a piece
- If a highlighted square with an opponents piece is selected, the pieces will attack each other

Attack:

- Both pieces involed will be revealed
- Outcome: see rules
(Idea: show popup with pictures and rank and a little animation)

Implementation:
ECS mit Command pattern :)

- Entity
  - Data
    - Name
    - Texture

- Unit
  - Behaviour:
    - Move (0, 1, line)
    - Attack (triggers effect: KILL, KILL_DEAD)
    - Highlight (possible moves)
  - Data:
    - Rank (F, B, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    - Position (x, y)
    - Ownership (player, opponent)

Systems:

- Game States:
  1. Preparation (Unit Placement)
  2. GamePlay
     1. Players turn
     2. Enemys turn

For Gameplay:  
Player 1 <- Referee -> Proxy - Network - Proxy <- Referee --> Player 2  
Enables easy single player and multiplayer without chaging the engine [see](https://pdfs.semanticscholar.org/f35c/df2b5cb1a36d703ab6c4a4d80cbaaf3cc603.pdf)  

- [InputHandlerSystem](https://stackoverflow.com/questions/38278201/libgdx-ashley-how-do-i-control-a-player-the-proper-way-ecs-framework)
- RefereeSystem -> PlayerComponent / InputHandler
- RenderSystem (based on ownership and rank?, list of all units?)
  - loads all textures
- MoveSystem (commands)
- AttackSystem (commands, MoveSytem adds attack component if collided with opponent)
- PlacementSystem (commands)
- HighlightSystem (highlights possible moves for a unit)

Input Handling (4 Layer) [see](https://javadocmd.com/blog/libgdx-ashley-on-the-stage/)

Animation:

- 12 different units
  - 2 moveing frames, 1 standing
- highlights should blink

## Architecture

- [Command pattern](http://gameprogrammingpatterns.com/command.html)

## Libraries

- [LibGDX](https://github.com/libgdx/libgdx)
- [Ashley](https://github.com/libgdx/ashley)
- [GreenRobot EventBus](https://github.com/greenrobot/EventBus)
