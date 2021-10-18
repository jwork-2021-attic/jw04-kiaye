package com.anish.calabashbros;

public class BubbleSorter<T extends Comparable<T>> implements Sorter<T> {

    private T[][] a;

    @Override
    public void load(T[][] a) {
        this.a = a;
    }

    private void swap(int i, int j, int k, int l) {
        T temp;
        temp = a[i][j];
        a[i][j] = a[k][l];
        a[k][l] = temp;
        plan += "" + a[i][j] + "<->" + a[k][l] + "\n";
    }

    private String plan = "";

    @Override
    public void sort() {
        int len = a.length*a[0].length;
        for (int i = 0; i < len; i++){ 
            int minrow = i/16;
            int mincol = i%16;
            for (int j = i + 1; j < len; j++){
                int row = j / 16;
                int col = j % 16;
                if (a[row][col].compareTo(a[minrow][mincol]) < 0){
                        minrow = row;
                        mincol = col;
                }              
            }
            swap(i/16,i%16,minrow, mincol);
        }
    }

    @Override
    public String getPlan() {
        return this.plan;
    }

}