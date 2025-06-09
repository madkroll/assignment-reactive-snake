package com.madkroll.snake.state;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class Grid {

    private final int width;
    private final int height;
    private final String[][] renderables;
    private final List<Integer> vacantCells;

    public Grid(final int width, final int height) {
        this.width = width;
        this.height = height;
        renderables = new String[height][width];
        vacantCells = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                vacantCells.add(toIndex(x, y));
            }
        }

        placeFruit();
    }

    public void placeFruit() {
        int fruitIndex = takeRandomVacantCell();
        int fruitX = toX(fruitIndex);
        int fruitY = toY(fruitIndex);
        renderables[fruitX][fruitY] = "fruit";
    }

    private int takeRandomVacantCell() {
        return vacantCells.remove(new Random().nextInt(vacantCells.size()));
    }

    private int toIndex(int x, int y) {
        return y * width + x;
    }

    private int toX(int index) {
        return index % width;
    }

    private int toY(int index) {
        return index / width;
    }
}
