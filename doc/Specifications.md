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

## Libraries

- [LibGDX](www.libgdx.com)
