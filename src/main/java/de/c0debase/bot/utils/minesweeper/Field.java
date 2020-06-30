/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Marvin Witt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.c0debase.bot.utils.minesweeper;


/**
 *  @since 1.0
 *  @author NurMarvin
 */
final class Field {
    private final boolean bomb;
    private final int x;
    private final int y;

    /**
     * A field of the {@link Board}
     * @param bomb Is the field a bomb?
     * @param x The X coordinate of the field
     * @param y The Y coordinate of the field
     */
    Field(boolean bomb, int x, int y) {
        this.bomb = bomb;
        this.x = x;
        this.y = y;
    }

    boolean isBomb() {
        return bomb;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Field) {
            Field other = (Field) obj;

            return other.getX() == this.getX() && other.getY() == this.getY() == other.isBomb() == this.isBomb();
        }
        return false;
    }
}
