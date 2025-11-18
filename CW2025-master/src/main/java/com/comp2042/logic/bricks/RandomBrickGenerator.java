package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    private List<Brick> currentBag;

    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());

        currentBag = new ArrayList<>(brickList);
        Collections.shuffle(currentBag);

        // Pre-fill the queue with the first bag
        nextBricks.addAll(currentBag);

        // Prepare next bag
        refillBag();
    }

    @Override
    public Brick getBrick() {
        Brick brick = nextBricks.poll();

        if (nextBricks.size() < 7) {
            refillBag();
        }

        return brick;
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }

    private void refillBag() {
        // Create and shuffle a new bag
        currentBag = new ArrayList<>(brickList);
        Collections.shuffle(currentBag);

        // Add the new bag to the queue
        nextBricks.addAll(currentBag);
    }
}