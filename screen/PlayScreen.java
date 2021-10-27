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
package screen;

import world.*;
import world.Dfs.point;
import asciiPanel.AsciiPanel;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private World world;
    private Creature player;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private List<String> oldMessages;
    private int endx;
    private int endy;
    private int nextx;
    private int nexty;

    public PlayScreen() {
        this.screenWidth = 31;
        this.screenHeight = 31;
        createWorld();
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();

        createxit();
        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory);
    }

    private void createCreatures(CreatureFactory creatureFactory) {
        this.player = creatureFactory.newPlayer(this.messages);

        // for (int i = 0; i < 8; i++) {
        //     creatureFactory.newFungus();
        // }
    }

    private void createWorld() {
        world = new WorldBuilder(32, 32).makeCaves().build();
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        // Show terrain
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;

                if (player.canSee(wx, wy)) {
                    terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
                } else {
                    terminal.write(world.glyph(wx, wy), x, y, Color.DARK_GRAY);
                }
            }
        }
        // Show creatures
        // for (Creature creature : world.getCreatures()) {
        //     if (creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
        //             && creature.y() < top + screenHeight) {
        //         if (player.canSee(creature.x(), creature.y())) {
        //             terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
        //         }
        //     }
        // }
        // Creatures can choose their next action now

        // Show paths
        Dfs dfs = new Dfs(this.world, player.x(), player.y(), endx, endy);
        dfs.dfs();
        point p = dfs.findpath();
        nextx = p.getx();
        nexty = p.gety();
        for (Path path : world.getPaths()){
            if (path.x() >= left && path.x() < left + screenWidth && path.y() >= top
                && path.y() < top + screenHeight) {
                if (player.canSee(path.x(), path.y())) {
                    terminal.write(path.glyph(), path.x() - left, path.y() - top, path.color());
                }
            }
        }
        for (Path path : world.getPaths2()){
            if (path.x() >= left && path.x() < left + screenWidth && path.y() >= top
                && path.y() < top + screenHeight) {
                if (player.canSee(path.x(), path.y())) {
                    terminal.write(path.glyph(), path.x() - left, path.y() - top, path.color());
                }
            }
        }
        world.update();
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = this.screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            terminal.write(messages.get(i), 1, top + i + 1);
        }
        this.oldMessages.addAll(messages);
        messages.clear();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // Terrain and creatures
        displayTiles(terminal, getScrollX(), getScrollY());
        // Player
        terminal.write(player.glyph(), player.x() - getScrollX(), player.y() - getScrollY(), player.color());
        // Stats
        // String stats = String.format("%3d/%3d hp", player.hp(), player.maxHP());
        // terminal.write(stats, 1, 23);
        // Messages
        //displayMessages(terminal, this.messages);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                player.moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
                player.moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                player.moveBy(0, 1);
                break;
            case KeyEvent.VK_A:
                automove();
                break;
        }
        return this;
    }

    public int getScrollX() {
        return Math.max(0, Math.min(player.x() - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y() - screenHeight / 2, world.height() - screenHeight));
    }
    private void createxit(){
        for (int i = screenWidth - 1; i >= 0; i--){
            for (int j = screenHeight -1; j >= 0; j--){
                if (this.world.tile(i, j).isGround()){
                    endx = i;
                    endy = j;

                    Path path = new Path(world, (char)6, AsciiPanel.green);
                    path.setX(endx);
                    path.setY(endy);
                    this.world.addpath(path);
                    return;
                }
            }
        }
    }

    private void automove(){
        player.moveBy(nextx-player.x(), nexty-player.y());
    }
}
