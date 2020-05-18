package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author pappgergely
 */
public class LabyrinthImpl implements Labyrinth {
    private CellType[][] labyrinth;
    private Coordinate playerPosition;
    private int width = -1;
    private int height = -1;


    public LabyrinthImpl() {
        playerPosition = new Coordinate(0, 0);

    }

    @Override
    public int getWidth() {
        return this.width == 0 ? -1 : this.width;
    }

    @Override
    public int getHeight() {
        return this.height == 0 ? -1 : this.height;
    }

    @Override
    public void loadLabyrinthFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            int width = Integer.parseInt(sc.nextLine());
            int height = Integer.parseInt(sc.nextLine());

            setSize(width, height);

            for (int hh = 0; hh < height; hh++) {
                String line = sc.nextLine();
                for (int ww = 0; ww < width; ww++) {
                    switch (line.charAt(ww)) {
                        case 'W':
                            labyrinth[ww][hh] = CellType.WALL;
                            break;
                        case 'E':
                            labyrinth[ww][hh] = CellType.END;
                            break;
                        case 'S':
                            labyrinth[ww][hh] = CellType.START;
                            playerPosition = new Coordinate(ww, hh);
                            break;
                        default:
                            labyrinth[ww][hh] = CellType.EMPTY;
                    }
                }
            }
        } catch (FileNotFoundException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public CellType getCellType(Coordinate c) throws CellException {
        if (c.getRow() >= height || c.getRow() < 0 || c.getCol() >= width || c.getCol() < 0) {
            throw new CellException(c, "Hoppá! Nem létezik ilyen koordináta.");
        }
        return labyrinth[c.getCol()][c.getRow()];
    }

    @Override
    public void setSize(int width, int height) {
        if (width < 0 || height < 0) {
            return;
        }
        this.width = width;
        this.height = height;
        labyrinth = new CellType[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                labyrinth[j][i] = CellType.EMPTY;
            }
        }
    }

    @Override
    public void setCellType(Coordinate c, CellType type) throws CellException {
        if (c.getRow() >= height || c.getRow() < 0 || c.getCol() >= width || c.getCol() < 0) {
            throw new CellException(c, "Hoppá! Nem létezik ilyen koordináta.");
        } else {
            labyrinth[c.getCol()][c.getRow()] = type;
            if (type == CellType.START) {
                playerPosition = c;
            }
        }
    }

    @Override
    public Coordinate getPlayerPosition() {
        return playerPosition;
    }

    @Override
    public boolean hasPlayerFinished() {
        return labyrinth[playerPosition.getCol()][playerPosition.getRow()] == CellType.END;
    }

    @Override
    public List<Direction> possibleMoves() {
        List<Direction> possibleMoves = new ArrayList<>();

        try {
            if (this.getCellType(new Coordinate(this.getPlayerPosition().getCol(),
                    this.getPlayerPosition().getRow() - 1)).equals(CellType.EMPTY)) {
                possibleMoves.add(Direction.NORTH);
            } else if (this.getCellType(new Coordinate(this.getPlayerPosition().getCol() + 1,
                    this.getPlayerPosition().getRow())).equals(CellType.EMPTY)) {
                possibleMoves.add(Direction.EAST);

            } else if (this.getCellType(new Coordinate(this.getPlayerPosition().getCol(),
                    this.getPlayerPosition().getRow() + 1)).equals(CellType.EMPTY)) {
                possibleMoves.add(Direction.SOUTH);
            } else if (this.getCellType(new Coordinate(this.getPlayerPosition().getCol() - 1,
                    this.getPlayerPosition().getRow())).equals(CellType.EMPTY)) {
                possibleMoves.add(Direction.WEST);
            }

        } catch (CellException e) {
            System.out.println(e.getMessage());
        }
        return possibleMoves;
    }


    public void setPlayerPosition(Coordinate c) {
        this.playerPosition = c;
    }


    @Override
    public void movePlayer(Direction direction) throws InvalidMoveException {
        if (this.possibleMoves().contains(direction)) {
            int col = getPlayerPosition().getCol();
            int row = getPlayerPosition().getRow();

            switch (direction) {
                case EAST:
                    setPlayerPosition(new Coordinate(col + 1, row));
                case WEST:
                    setPlayerPosition(new Coordinate(col - 1, row));
                case NORTH:
                    setPlayerPosition(new Coordinate(col, row - 1));
                case SOUTH:
                    setPlayerPosition(new Coordinate(col, row + 1));
            }
        } else {
            throw new InvalidMoveException();
        }
    }
}

