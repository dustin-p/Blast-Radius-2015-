package com.porterdustin.zombienuke;

public class Bounds {
    private int horizontalPadding;
    private int verticalPadding;
    private int screenSize;
    private Position centerScreen;

    public Bounds (int screenWidth, int screenHeight) {
        screenSize = Math.min(screenWidth, screenHeight);

        if (screenWidth > screenHeight) {
            horizontalPadding = (screenWidth - screenHeight) / 2;
            verticalPadding = 0;
        } else {
            verticalPadding = (screenHeight - screenWidth) / 2;
            horizontalPadding = 0;
        }

        centerScreen = new Position
                (horizontalPadding + screenSize/2, verticalPadding + screenSize / 2);
    }

    public boolean outOfBounds(Position position) {
        float x = position.getX();
        float y = position.getY();
        return (x < horizontalPadding || x > screenSize + horizontalPadding ||
                y < verticalPadding || y > screenSize + verticalPadding);
    }
}
