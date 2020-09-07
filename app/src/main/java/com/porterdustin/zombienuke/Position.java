package com.porterdustin.zombienuke;

public class Position {
    private double x;
    private double y;
    private double scale;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
        scale = 1;
    }

    public Position(double x, double y, double scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public Position(Position parent) {
        this.x = parent.getX();
        this.y = parent.getY();
        this.scale = parent.getScale();
    }

    public void move(double speed, double direction) {
        float hSpeed = (float) (speed*Math.cos(Math.toRadians(direction)));
        float vSpeed = (float) (speed*Math.sin(Math.toRadians(direction)));
        x += hSpeed;
        y += vSpeed;
    }

    public double distanceTo(Position point) {
        return Math.sqrt(
                (point.getX()-x)*(point.getX()-x) +
                        (point.getY()-y)*(point.getY()-y));
    }

    public double directionTo(Position point) {
        return Math.atan2(point.getY()-y, point.getX()-x) * (180 / Math.PI);
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y, double scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public float getX() {
        return (float) x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setX(double x, boolean relative) {
        this.x += x;
    }

    public float getY() {
        return (float) y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setY(double y, boolean relative) {
        this.y += y;
    }

    public float getScale() { return (float) scale; }

    public void setScale(double scale) { this.scale = scale; }

}
