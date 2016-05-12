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
    int frequency;
    String character;    
    Nodo right;
    Nodo left;

    Nodo(String character, int frequency) {
        this.character = character;
        this.frequency = frequency;        
    }
    
    @Override
    public String toString() {
        return "Nodo{" + "frequency=" + frequency + ", character=" + character + '}';
    }
    
    
}
