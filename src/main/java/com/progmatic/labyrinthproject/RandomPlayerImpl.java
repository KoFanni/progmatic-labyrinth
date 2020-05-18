package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;

public class RandomPlayerImpl implements Player {
    @Override
    public Direction nextMove(Labyrinth l) {
        int randomMove = (int)(Math.random())*(l.possibleMoves().size()-1);
        return l.possibleMoves().get(randomMove);
    }
}
