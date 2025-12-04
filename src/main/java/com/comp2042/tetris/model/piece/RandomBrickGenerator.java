package com.comp2042.tetris.model.piece;

import com.comp2042.tetris.model.piece.types.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Generates bricks using the "bag" randomization system from modern Tetris.
 * This ensures fair distribution by shuffling all 7 brick types into a "bag",
 * dealing them out sequentially, then creating a new shuffled bag.
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    private List<Brick> currentBag;

    // Initialize the generator
    public RandomBrickGenerator() {
        // Create the list of all brick types
        brickList = BrickFactory.createAllBricks();

        // Create and shuffle the first bag
        currentBag = new ArrayList<>(brickList);
        Collections.shuffle(currentBag);

        // Pre-fill the queue with the first bag
        nextBricks.addAll(currentBag);

        // Prepare next bag
        refillBag();
    }

    // Return the current brick and prepare the next one
    @Override
    public Brick getBrick() {
        Brick brick = nextBricks.poll();

        // Refill the bag if queue drops below a complete bag
        if (nextBricks.size() < 7) {
            refillBag();
        }

        return brick;
    }

    // Preview the next brick without removing it from the queue
    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }

    // Creates a new shuffled bag and adds it to the queue
    private void refillBag() {
        // Create and shuffle a new bag
        currentBag = new ArrayList<>(brickList);
        Collections.shuffle(currentBag);

        // Add the new bag to the queue
        nextBricks.addAll(currentBag);
    }
}