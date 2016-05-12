
import com.sun.xml.internal.ws.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Lucas
 */
public class Huffman {

    private static Nodo root = null;
    private static Map<String, Integer> mapBinaryWord;
    private static LinkedList<Nodo> list;

    public static void main(String[] args) {
        buildMap("aabbbcccc");
        printTree();
    }

    public static void printTree() {
        buildMap("aaaabbbbccc");
        System.out.println(root.toString());
        System.out.println(root.left.toString());
        System.out.println(root.left.left.toString());
        System.out.println(root.right.toString());
        
    }
    

    public static void buildMap(String word) {
        if (word.length() <= 0) {
            throw new IllegalArgumentException("Palavra vazia");
        }
        Map<Character, Integer> mapWord = new HashMap<Character, Integer>();
        mapWord.put(word.charAt(0), 1);
        for (int i = 1; i < word.length(); i++) {
            char key = word.charAt(i);
            if (mapWord.containsKey(key)) {
                int frequency = mapWord.get(key);
                frequency++;
                mapWord.put(key, frequency);
            } else {
                mapWord.put(key, 1);
            }
        }
        list = new LinkedList<Nodo>();
        for (char key : mapWord.keySet()) {
            Nodo nodo = new Nodo(key + "", mapWord.get(key));
            list.add(nodo);
        }
        root = buildTree(list);        
//        for(int i = 0; i < list.size(); i++){
//            searchNode(root, list.get(i));    
//        }
        
    }
    
    public static void searchNode(Nodo root, Nodo node) {        
        String binary = "";                       
        binary = searchNode(node.character, root, binary);
        mapBinaryWord.put(binary, node.frequency);
    }

    private static String searchNode(String character, Nodo nodo, String binary) {        
        if(nodo == null){
            throw new IllegalArgumentException("nenhum caracter encontrado");
        }
        if (!character.equals(nodo.character)) {
            searchNode(character, nodo.left, binary + "0");
            searchNode(character, nodo.right, binary + "1");            
        }
        return binary;        
    }

    public static Nodo buildTree(LinkedList<Nodo> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        Nodo less1 = list.get(0);
        Nodo less2 = list.get(1);
        for (int i = 2; i < list.size(); i++) {
            if (list.get(i).frequency < less1.frequency) {
                less1 = list.get(i);
            } else if (list.get(i).frequency < less2.frequency) {
                less2 = list.get(i);
            }
        }
        Nodo nodoFather = new Nodo(
                less1.character + "" + less2.character,
                less1.frequency + less2.frequency
        );
        if ((less1.frequency - less2.frequency) >= 0) {
            nodoFather.right = less2;
            nodoFather.left = less1;
        } else {
            nodoFather.right = less1;
            nodoFather.left = less2;
        }
        list.remove(less1);
        list.remove(less2);
        list.add(nodoFather);
        return buildTree(list);
    }
}
