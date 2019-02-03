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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  @since 1.0
 *  @author NurMarvin
 */
public final class Board {
    private final ArrayList<Field> fields;
    private final int maxBombs;
    private Field revealedField;

    public Board() {
        this(32);
    }

    /**
     * Creates a new board with a given amount of max bombs
     * @param maxBombs The maximum amount of bombs
     */
    private Board(int maxBombs) {
        this.maxBombs = maxBombs;
        this.fields = new ArrayList<>();
        this.populate();
    }

    /**
     * Populates the mine field
     */
    private void populate() {
        int currentBombs = 0;
        for (int y = 0; y < 8; y++)
            for (int x = 0; x < 8; x++)
                if (currentBombs < maxBombs) {
                    if (ThreadLocalRandom.current().nextBoolean()) {
                        this.addField(new Field(true, x, y));
                        currentBombs++;
                    } else this.addField(new Field(false, x, y));
                } else this.addField(new Field(false, x, y));
    }

    /**
     * Adds a {@link Field} to the mine field and can happen to become the initially revealed field
     * @param field The field to add
     */
    private void addField(Field field) {
        if (this.revealedField == null && ThreadLocalRandom.current().nextBoolean() && !field.isBomb())
            this.revealedField = field;
        this.fields.add(field);
    }


    /**
     * Transforms the board into a message with a spoiler tag for each field
     * @return The board as a discord message
     */
    @NotNull
    public final String toSpoiler() {
        boolean revealedFieldSet = false;
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : this.fields) {
            if (field.getX() == 0 && field.getY() != 0) stringBuilder.append("\n");

            if (field.isBomb()) {
                stringBuilder.append("||\uD83D\uDCA3||");
                continue;
            }

            if ((!revealedFieldSet && this.revealedField == null) || revealedField.equals(field)) {
                stringBuilder.append(this.getBombsNear(field)).append("⃣");
                revealedFieldSet = true;
            } else stringBuilder.append("||").append(this.getBombsNear(field)).append("⃣").append("||");
        }
        return stringBuilder.toString();
    }

    /**
     * Gets the bombs the near a given {@link Field}. Can be 8 at max.
     * @param field The {@link Field} which should have the amount of bombs around it checked
     * @return The amount of bombs around the {@link Field}
     */

    private int getBombsNear(@NotNull Field field) {
        return this.getBombsNear(field.getX(), field.getY());
    }

    /**
     * Gets the bombs the near the given coordinates. Can be 8 at max.
     * @param x The X coordinate that should be checked
     * @param y The Y coordinate that should be checked
     * @return The amount of bombs around the given coordinates
     */

    private int getBombsNear(int x, int y) {
        int bombs = 0;
        for (int xOffset = -1; xOffset < 2; xOffset++)
            for (int yOffset = -1; yOffset < 2; yOffset++) {
                Field field = this.getFieldAt(x + xOffset, y + yOffset);
                if (field == null) continue;

                if (field.isBomb()) bombs++;
            }
        return bombs;
    }

    /**
     * Gets the {@link Field} at the given coordinates.
     * @param x The X coordinate of the {@link Field}
     * @param y The Y coordinate of the {@link Field}
     * @return The {@link Field} at the given coordinates. May be null.
     */

    @Nullable
    private Field getFieldAt(int x, int y) {
        for (Field field : this.fields) {
            if (field.getX() == x && field.getY() == y) return field;
        }
        return null;
    }
}