package world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import asciiPanel.AsciiPanel;

/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/**
 *
 * @author Aeranythe Echosong
 */
public class World {

    private Tile[][] tiles;
    private int width;
    private int height;
    private List<Creature> creatures;
    private List<Path> paths;
    private List<Path> paths2;

    public static final int TILE_TYPES = 2;

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.creatures = new ArrayList<>();
        this.paths = new ArrayList<>();
        this.paths2 = new ArrayList<>();
    }

    public Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y];
        }
    }

    public char glyph(int x, int y) {
        return tiles[x][y].glyph();
    }

    public Color color(int x, int y) {
        return tiles[x][y].color();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void dig(int x, int y) {
        if (tile(x, y).isDiggable()) {
            tiles[x][y] = Tile.FLOOR;
        }
    }

    public void addAtEmptyLocation(Creature creature) {
        int x = 0;
        int y = 0;

        // do {
        //     x = (int) (Math.random() * this.width);
        //     y = (int) (Math.random() * this.height);
        // } while (!tile(x, y).isGround() || this.creature(x, y) != null);

        for (x = 0; x < this.width; x++){
            boolean breaker = false;
            for (y = 0; y < this.height; y++){
                if (tile(x, y).isGround() && this.creature(x, y) == null){
                    breaker = true;
                    break;
                }
            }
            if (breaker){
                break;
            }
        }

        creature.setX(x);
        creature.setY(y);

        this.creatures.add(creature);
    }

    public Creature creature(int x, int y) {
        for (Creature c : this.creatures) {
            if (c.x() == x && c.y() == y) {
                return c;
            }
        }
        return null;
    }

    public List<Creature> getCreatures() {
        return this.creatures;
    }

    public void remove(Creature target) {
        this.creatures.remove(target);
    }

    public void update() {
        ArrayList<Creature> toUpdate = new ArrayList<>(this.creatures);

        for (Creature creature : toUpdate) {
            creature.update();
        }

        removegreenpath();
    }

    public void addpath(Path path){
        if (tile(path.x(), path.y()).isGround()){
            //&& this.path(path.x(), path.y()) == null
            paths.add(path);
        }
    }

    public void addpath2(Path path){
        if (tile(path.x(), path.y()).isGround()){
            //&& this.path(path.x(), path.y()) == null
            paths2.add(path);
        }
    }

    public Path path(int x, int y) {
        for (Path p : this.paths) {
            if (p.x() == x && p.y() == y) {
                return p;
            }
        }
        return null;
    }

    public Path path2(int x, int y) {
        for (Path p : this.paths2) {
            if (p.x() == x && p.y() == y) {
                return p;
            }
        }
        return null;
    }
    public List<Path> getPaths() {
        return this.paths;
    }

    public List<Path> getPaths2() {
        return this.paths2;
    }

    public void removePath(Path target) {
        this.paths.remove(target);
    }

    public void removePath2(Path target) {
        this.paths2.remove(target);
    }

    public void removegreenpath(){

        for (int i = 0; i < paths2.size(); i++){
            if (paths2.get(i).color() == AsciiPanel.green){
                this.paths2.remove(i);
            }
        }
    }
}
