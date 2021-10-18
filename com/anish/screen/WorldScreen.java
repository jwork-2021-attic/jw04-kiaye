package com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.plaf.ColorUIResource;

import com.anish.calabashbros.BubbleSorter;
import com.anish.calabashbros.Calabash;
import com.anish.calabashbros.World;
import com.anish.calabashbros.Random;

import asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {

    private World world;
    private Calabash[][] bros;
    String[] sortSteps;

    public WorldScreen() {
        world = new World();

        bros = new Calabash[16][16];

        Random rank = new Random();

        for (int i = 0; i < 256; i++){
            int row = rank.arr[i]/16;
            int col = rank.arr[i]%16;
            Color co;
            if (i <= 51){
                co = new Color(255, i*5, 0);
                bros[row][col] = new Calabash(co, i, world);
            }
            else if (i > 51 && i <=102){
                co = new Color(255 - (i-51)*5, 255, 0);
                bros[row][col] = new Calabash(co, i, world);
            }
            else if (i > 102 && i <= 153){
                co = new Color(0, 255, (i-102)*5);
                bros[row][col] = new Calabash(co, i, world);
            }
            else if (i > 153 && i <= 204){
                co = new Color(0, 255-(i-153)*5, 255);
                bros[row][col] = new Calabash(co, i, world);
            }
            else if (i > 204 && i <= 255){
                co = new Color((i-204)*5, 0, 255);
                bros[row][col] = new Calabash(co, i, world);
            }
        }
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 16; j++){
                world.put(bros[i][j], i*2, j*2);        
            }
        }
       

        BubbleSorter<Calabash> b = new BubbleSorter<>();
        b.load(bros);
        b.sort();

        sortSteps = this.parsePlan(b.getPlan());
    }

    private String[] parsePlan(String plan) {
        return plan.split("\n");
    }

    private void execute(Calabash[][] bros, String step) {
        String[] couple = step.split("<->");
        getBroByRank(bros, Integer.parseInt(couple[0])).swap(getBroByRank(bros, Integer.parseInt(couple[1])));
    }

    private Calabash getBroByRank(Calabash[][] bros, int rank) {
        for (Calabash[] bro : bros) {
            for (Calabash bo : bro){
                if (bo.getRank() == rank) {
                    return bo;
                }
            }
        }
        return null;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {

        if (i < this.sortSteps.length) {
            this.execute(bros, sortSteps[i]);
            i++;
        }

        return this;
    }

}
