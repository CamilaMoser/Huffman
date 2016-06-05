
import com.sun.xml.internal.ws.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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

    public static Nodo root = null;
    public static Map<String, Integer> mapBinaryWord;
    public static LinkedList<Nodo> list;
    public static LinkedList<Nodo> listAux;

    public static void main(String[] args) {
        buildMap("aaaaabbbbcccdde");
        System.out.println(listAux);
        System.out.println(searchNode());        
        writeInGraphviz();
    }

    public static String searchNode() {
        String binary = "";
        return searchNode(listAux.get(0), binary);
    }

    private static String searchNode(Nodo node, String binary) {
        if (node.father == null) {
            return binary;
        }
        if (node.father.left.character.equalsIgnoreCase(node.character)) {
            binary = binary + "0";
        } else {
            binary = binary + "1";
        }
        return searchNode(node.father, binary);
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
        list = new LinkedList<>();
        listAux = new LinkedList<>();
        for (char key : mapWord.keySet()) {
            Nodo nodo = new Nodo(key + "", mapWord.get(key));
            list.add(nodo);

        }
        root = buildTree();
    }

    public static Nodo buildTree() {
        if (list.size() == 1) {
            return list.get(0);
        }

        Nodo less1 = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).frequency < less1.frequency) {
                less1 = list.get(i);
            }
        }
        list.remove(less1);
        Nodo less2 = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).frequency < less2.frequency) {
                less2 = list.get(i);
            }
        }
        list.remove(less2);

        Nodo nodoFather = new Nodo(
                less1.character + "" + less2.character,
                less1.frequency + less2.frequency
        );
        if ((less1.frequency - less2.frequency) >= 0) {
            nodoFather.right = less1;
            nodoFather.left = less2;
            less1.father = nodoFather;
            less2.father = nodoFather;
        } else {
            nodoFather.right = less2;
            nodoFather.left = less1;
            less1.father = nodoFather;
            less2.father = nodoFather;

        }
        listAux.add(less1);
        listAux.add(less2);
        list.add(nodoFather);
        return buildTree();
    }

    public static ArrayList<Nodo> getNivel(int n) {
        ArrayList<Nodo> r = new ArrayList<>();
        getNivel(root, n, r, 0);
        return r;
    }

    private static void getNivel(Nodo nodo, int n, List<Nodo> r, int atual) {
        if (nodo == null) {
            throw new IllegalArgumentException("Nível não existe: " + n);
        }
        if (n == atual) {
            r.add(nodo);
        } else if (atual < n) {
            if (nodo.left != null) {
                getNivel(nodo.left, n, r, atual + 1);
            }
            if (nodo.right != null) {
                getNivel(nodo.right, n, r, atual + 1);
            }

        }
    }

    public static int getAltura() {
        return getAltura0(root);
    }

    private static int getAltura0(Nodo nodo) {
        if (nodo == null) {
            return -1;
        }

        int ae = getAltura0(nodo.left);
        int ad = getAltura0(nodo.right);

        return 1 + Math.max(ae, ad);
    }

    public static void writeInGraphviz() {
        System.out.println(getNivel(0).get(0).frequency);
        for (int i = 1; i <= getAltura(); i++) {
            if (getNivel(i+1).size() == Math.pow(2, i+1)) {
                for (int j = 0; j < getNivel(i).size(); j++) {
                    System.out.println(getNivel(i).get(j).frequency);                    
                    if(getNivel(i).get(j).left != null && getNivel(i).get(j).left != null) {
                        System.out.println(getNivel(i).get(j).left.frequency);
                        System.out.println(getNivel(i).get(j).right.frequency);
                    }                    
                }
            } else {
                for (int j = 0; j < getNivel(i+1).size(); j++) {
                    System.out.println(getNivel(i+1).get(j).frequency);
                }
            }
        }
    }
}
