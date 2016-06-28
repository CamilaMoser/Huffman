
import com.sun.xml.internal.ws.util.StringUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.UUID;
import static javax.management.Query.lt;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Lucas e Camila
 */
public class Huffman {

    public static Nodo root = null;
    public static HashMap<Character, String> mapBinaryWord;
    public static LinkedList<Nodo> list;
    public static LinkedList<Nodo> listAux;
    public static String word;
    public static int cont = 0;
    
    public static void main(String[] args) throws IOException {
        word = "aaaaabbbbcccdde";
        buildMap(word);        
        buildTable();        
        writeInGraphviz();
        System.out.println(word + " - " + cont);        
    }

    public static String searchNode(int cont) {
        String binary = "";
        binary = searchNode(listAux.get(cont), binary);
        listAux.get(cont).setSequenceBinaty(binary);
        return binary;
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

    public static Map buildMapAux() {
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
        return mapWord;
    }

    public static void buildMap(String word) {
        if (word.length() <= 1) {
            throw new IllegalArgumentException("Não é uma palavra ou frase");
        }
        Map mapWord = buildMapAux();
        list = new LinkedList<>();
        listAux = new LinkedList<>();
        for (Object key : mapWord.keySet()) {
            Nodo nodo = new Nodo(key + "", (int) mapWord.get(key));
            nodo.setLeafToTrue();
            list.add(nodo);
        }
        root = buildTree();
    }

    public static Nodo nodeLess() {
        Nodo less = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).frequency < less.frequency) {
                less = list.get(i);
            }
        }
        list.remove(less);
        return less;
    }

    public static void buildSubTree(Nodo less1, Nodo less2, Nodo nodoFather) {
        /**
         * Se os dois nodos filhos forem folhas então o de menor frequencia fica
         * a direita do nodo pai e o de maior frequencia a esquerda assim como
         * exemplifica o enunciado
         */
        if (less1.isLeaf() && less2.isLeaf()) {
            nodoFather.right = less1;
            nodoFather.left = less2;
            less2.father = nodoFather;
            less1.father = nodoFather;
        } else {
            if ((less1.frequency - less2.frequency) >= 0) {
                nodoFather.right = less1;
                nodoFather.left = less2;
                less1.father = nodoFather;
                less2.father = nodoFather;
            } else {
                nodoFather.right = less2;
                nodoFather.left = less1;
                less2.father = nodoFather;
                less1.father = nodoFather;
            }
        }
    }

    public static Nodo buildTree() {
        if (list.size() == 1) {
            return list.get(0);
        }
        Nodo less1 = nodeLess();
        Nodo less2 = nodeLess();
        Nodo nodoFather = new Nodo(
                less1.character + "" + less2.character,
                less1.frequency + less2.frequency
        );
        buildSubTree(less1, less2, nodoFather);
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
            }
        }
    }

    public static void compress() {
        BinaryOut binary = new BinaryOut("outputBinary.txt");
        for (int i = 0; i < word.length(); i++) {
            String code = mapBinaryWord.get(word.charAt(i));
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '1') {
                    System.out.print("1");
                    binary.write(true);
                } else {
                    System.out.print("0");
                    binary.write(false);
                }
            }
        }
        binary.flush();
        binary.close();
        System.out.println(binary.toString());
    }

    public static void writeInGraphviz() throws IOException {
        cont = cont + 2;
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter("graph.gv"));
        buffWrite.append("digraph {\n");
        writeInGraphviz(root, buffWrite);
        cont = cont + 2;
        buffWrite.append("}");
        buffWrite.close();        
    }

    private static void writeInGraphviz(Nodo nodo, BufferedWriter buffWrite) throws IOException {
        cont = cont + 4;
        if (nodo == null) {
            return;            
        }
        cont++;
        if (nodo.left != null) {
            cont = cont + 2;
            if (nodo.left.isLeaf()) {
                cont = cont + 5;
                buffWrite.append(nodo.frequency + " -> " + nodo.left.character + "_" + nodo.left.frequency + "_" + nodo.left.sequenceBinaty + ";" + "\n");
            } else {
                cont = cont + 3;
                buffWrite.append(nodo.frequency + " -> " + nodo.left.frequency + ";" + "\n");
            }
            cont = cont + 2;
            writeInGraphviz(nodo.left, buffWrite);
        }
        cont++;
        if (nodo.right != null) {
            cont = cont + 2;
            if (nodo.right.isLeaf()) {
                cont = cont + 5;
                buffWrite.append(nodo.frequency + " -> " + nodo.right.character + "_" + nodo.right.frequency + "_" + nodo.right.sequenceBinaty + ";" + "\n");
            } else {
                cont = cont + 3;
                buffWrite.append(nodo.frequency + " -> " + nodo.right.frequency + ";" + "\n");
            }
            cont = cont + 2;
            writeInGraphviz(nodo.right, buffWrite);
        }
        
    }
}
