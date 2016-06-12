
import com.sun.xml.internal.ws.util.StringUtils;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    public static HashMap<Character, String> mapBinaryWord;
    public static LinkedList<Nodo> list;
    public static LinkedList<Nodo> listAux;

    public static void main(String[] args) throws IOException {
        buildMap("aaaaabbbbcccdde");
        System.out.println(listAux);
        buildTable();
        writeInGraphviz();
    }

     public static String searchNode(int cont) {
        String binary = "";
        return searchNode(listAux.get(cont), binary);
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
    
    /**
     * Constroi uma tabela com os caracters e sua sequencia de bits
     *
     * @return
     */
    public static void buildTable() {
        mapBinaryWord = new HashMap<Character, String>();
        for (int i = 0; i < listAux.size(); i++) {
            if (listAux.get(i).character.length() == 1) {
                char character = listAux.get(i).character.charAt(0);
                StringBuffer sb = new StringBuffer(searchNode(i));
                sb.reverse();
                String code = sb.toString();
                mapBinaryWord.put(character, code);
                System.out.println(sb + " " + listAux.get(i).character + " " + listAux.get(i).frequency);
            }
        }
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

    public static void writeInGraphviz() throws IOException {        
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter("graph.gv"));
        buffWrite.append("digraph {\n"); 
        writeInGraphviz(root, buffWrite);
        buffWrite.append("}"); 
        buffWrite.close();
    }

    private static void writeInGraphviz(Nodo nodo, BufferedWriter buffWrite) throws IOException {       
        if(nodo == null) {
            return;
        }
        if(nodo.left != null){
            buffWrite.append(nodo.character + "_" + nodo.frequency + " -> " + nodo.left.character + "_" + nodo.left.frequency + ";" + "\n");
            System.out.println(nodo.character + "_" + nodo.frequency + " -> " + nodo.left.character + "_" + nodo.left.frequency + ";");    
            writeInGraphviz(nodo.left, buffWrite);
        }
        if(nodo.right != null){
            buffWrite.append(nodo.character + "_" + nodo.frequency + " -> " + nodo.right.character + "_" + nodo.right.frequency   + ";" + "\n");
            System.out.println(nodo.character + "_" + nodo.frequency + " -> " + nodo.right.character + "_" + nodo.right.frequency   + ";");    
            writeInGraphviz(nodo.right, buffWrite);
        }
        
    }
}
