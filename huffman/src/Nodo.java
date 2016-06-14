/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucas
 */
public class Nodo {    
    public int frequency;
    public String character;    
    public Nodo right;
    public Nodo left;
    public Nodo father;
    public boolean leaf;

    Nodo(String character, int frequency) {
        this.character = character;
        this.frequency = frequency;                
        this.leaf = false; 
    }
    
    public void setLeafToTrue() {
        this.leaf = true;                
    }
    
    public boolean isLeaf() {
        return this.leaf;
    }
    
    @Override
    public String toString() {
        return "Nodo{" + "frequency=" + frequency + ", character=" + character + '}';
    }
    
    
}
