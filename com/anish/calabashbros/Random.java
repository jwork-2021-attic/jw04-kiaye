package com.anish.calabashbros;

public class Random {
    public int[] arr = new int[256];
    public Random(){
        int i;  
        for (i = 0; i < 256; i++) {  
            arr[i] = i;  
        }  
        //费雪耶兹置乱算法  
        //每次生成的随机交换位置:
        for (i = arr.length - 1; i > 0; i--){  
            //随机数生成器，范围[0, i]  
            int rand = (int)(Math.random()*i);  
            int temp = arr[i];  
            arr[i] = arr[rand];  
            arr[rand] = temp;  
        }  
    }
}