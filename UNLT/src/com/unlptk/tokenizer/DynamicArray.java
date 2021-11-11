/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unlptk.tokenizer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DynamicArray {
    
    
    private HashMap<Point, String> map = new HashMap<Point, String>();
   // private int maxRow = 0;
    private int maxColumn = 0;
    List<String> list = new ArrayList<String>();
    int flag;
   

    public void add(String data){
        
        
        
    }
    
   /* public void add(int row, int column, String string) {
        map.put(new Point(row, column), string);
        maxRow = Math.max(row, maxRow);
        maxColumn = Math.max(column, maxColumn);
    }

    public String[][] toArray() {
        String[][] result = new String[maxRow + 1][maxColumn + 1];
        for (int row = 0; row <= maxRow; ++row)
            for (int column = 0; column <= maxColumn; ++column) {
                Point p = new Point(row, column);
                result[row][column] = map.containsKey(p) ? map.get(p) : "";
            }
        return result;
    }*/
    
    
    
    
}
