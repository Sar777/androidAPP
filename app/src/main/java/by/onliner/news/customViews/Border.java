package by.onliner.news.customViews;

import android.graphics.Color;

/**
 * Created by Mi Air on 17.10.2016.
 */

public class Border {
    private int orientation;
    private int width;
    private int color = Color.BLACK;
    private int style;
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public int getStyle() {
        return style;
    }
    public void setStyle(int style) {
        this.style = style;
    }
    public int getOrientation() {
        return orientation;
    }
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
    public Border(int Style) {
        this.style = Style;
    }
}