import java.awt.*; 
import javax.swing.*; 
import java.io.*; 
import java.util.*;
import java.awt.event.*; 
import javax.swing.plaf.metal.*; 
import javax.swing.text.*; 

class editor extends JFrame implements ActionListener { 
    JTextArea t; 
    JFrame f; 
    editor() { 
        f = new JFrame("editor"); 
        try { 
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); 
            MetalLookAndFeel.setCurrentTheme(new OceanTheme()); 
        } 
        catch (Exception e) { } 
        t = new JTextArea(); 
        JMenuBar mb = new JMenuBar(); 
        JMenu m1 = new JMenu("File"); 
        JMenuItem mi1 = new JMenuItem("New"); 
        JMenuItem mi2 = new JMenuItem("Open"); 
        JMenuItem mi3 = new JMenuItem("Save"); 
        JMenuItem mi9 = new JMenuItem("Print"); 

        mi1.addActionListener(this); 
        mi2.addActionListener(this); 
        mi3.addActionListener(this); 
        mi9.addActionListener(this); 
  
        m1.add(mi1); 
        m1.add(mi2); 
        m1.add(mi3); 
        m1.add(mi9); 
  
        JMenu m2 = new JMenu("Edit"); 
  
        JMenuItem mi4 = new JMenuItem("Cut"); 
        JMenuItem mi5 = new JMenuItem("Copy"); 
        JMenuItem mi6 = new JMenuItem("Paste"); 
  
        mi4.addActionListener(this); 
        mi5.addActionListener(this); 
        mi6.addActionListener(this); 
  
        m2.add(mi4); 
        m2.add(mi5); 
        m2.add(mi6); 
  
        JMenuItem mc = new JMenuItem("Quit"); 
        mc.addActionListener(this); 
  
        mb.add(m1); 
        mb.add(m2); 
        mb.add(mc); 
  
        f.setJMenuBar(mb); 
        f.add(t); 
        f.setSize(500, 500); 
        f.show(); 
    } 
  
    public void actionPerformed(ActionEvent e) { 
        String s = e.getActionCommand();
        if (s.equals("Cut")) { 
            t.cut(); 
        } else if (s.equals("Copy")) { 
            t.copy(); 
        } else if (s.equals("Paste")) { 
            t.paste(); 
        } else if (s.equals("Save")) { 
            JFileChooser j = new JFileChooser("f:"); 
            int r = j.showSaveDialog(null); 
            if (r == JFileChooser.APPROVE_OPTION) { 
                File fi = new File(j.getSelectedFile().getAbsolutePath()); 
                try {
                    FileWriter wr = new FileWriter(fi, false);
                    BufferedWriter w = new BufferedWriter(wr);
                    w.write(t.getText()); 
                    w.flush(); 
                    w.close(); 
                } 
                catch (Exception evt) { 
                    JOptionPane.showMessageDialog(f, evt.getMessage()); 
                } 
            } 
            else {
                JOptionPane.showMessageDialog(f, "the user cancelled the operation"); 
            }
        } 
        else if (s.equals("Print")) { 
            try { 
                t.print(); 
            } 
            catch (Exception evt) { 
                JOptionPane.showMessageDialog(f, evt.getMessage()); 
            } 
        } 
        else if (s.equals("Open")) { 
            JFileChooser j = new JFileChooser("f:"); 
  
            int r = j.showOpenDialog(null); 

            if (r == JFileChooser.APPROVE_OPTION) { 
                File fi = new File(j.getSelectedFile().getAbsolutePath()); 
  
                try {
                    String s1 = "", sl = ""; 
                    FileReader fr = new FileReader(fi); 
                    BufferedReader br = new BufferedReader(fr); 
                    sl = br.readLine();
                    while ((s1 = br.readLine()) != null) { 
                        sl = sl + "\n" + s1; 
                    } 
                    t.setText(sl); 
                } 
                catch (Exception evt) { 
                    JOptionPane.showMessageDialog(f, evt.getMessage()); 
                } 
            } 
            else
                JOptionPane.showMessageDialog(f, "the user cancelled the operation"); 
        } 
        else if (s.equals("New")) { 
            t.setText(""); 
        } 
        else if (s.equals("Quit")) { 
            f.setVisible(false); 
        } 
    } 

    public static void main(String args[]) 
    { 
        editor e = new editor(); 
    } 
} 

class SpellChecker {
    public ArrayList<String> suggest = new ArrayList<String>();
    public char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public SeparateChainingHashST<String, String> wordBank = new SeparateChainingHashST();

    public void spellCheck(String word) {
        Scanner input = new Scanner(System.in);
        SpellChecker sc = new SpellChecker();
        sc.hashWords();
        sc.checkWord(word);
    }

    public void checkWord(String word) {
        if(check(word)) {
            System.out.println("No mistakes found");
        } else {
            addCharStart(word);
            addCharEnd(word);
            rmCharStart(word);
            rmCharEnd(word);
            exchChars(word);
            int suggestions = suggest.size();
            if(suggestions != 0) {
                if(suggestions == 1) {
                    System.out.println("1 suggestion: ");
                } else {
                    System.out.println(suggestions + " suggestions: ");
                }
                for(int i = 0; i < suggestions; i++) {
                    System.out.println(suggest.get(i));
                }
            } else {
                System.out.println("No suggestions");
            }
        }
    }

    public boolean check(String word) {
        boolean match = false;
        if(wordBank.contains(word)) {
            suggest.add(word);
            match = true;
        }
        return match;
    }

    public void hashWords() {
        String filePath = "words.txt";
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
            String lineText = null;
         
            while ((lineText = lineReader.readLine()) != null) {
                wordBank.put(lineText, lineText);
            }
         
            lineReader.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void addCharStart(String word) {
        for(int i = 0; i < 26; i++) {
            StringBuilder sb = new StringBuilder(word);
            sb.insert(0, alphabet[i]);
            check(sb.toString());
        }
    }

    public void addCharEnd(String word) {
        int len = word.length();
        for(int i = 0; i < 26; i++) {
            StringBuilder sb = new StringBuilder(word);
            sb.insert(len, alphabet[i]);
            check(sb.toString());
        }
    }

    public void rmCharStart(String word) {
        check(word.substring(1));
    }

    public void rmCharEnd(String word) {
        int len = (word.length() - 1);
        check(word.substring(0, len));
    }

    public void exchChars(String word) {
        for (int i = 0; i < word.length(); i++) {
            for (int j = i + 1; j < word.length(); j++) {
                exch(word, i, j);
            }
        }
    }

    public void exch(String word, int a, int b) {
        StringBuilder sb = new StringBuilder(word); 
        sb.setCharAt(a, word.charAt(b)); 
        sb.setCharAt(b, word.charAt(a)); 
        check(sb.toString()); 
    }
}

class SeparateChainingHashST<Key, Value> {
    private static final int INIT_CAPACITY = 4;
    private int n;
    private int m;
    private SequentialSearchST<Key, Value>[] st;

    public SeparateChainingHashST() {
        this(INIT_CAPACITY);
    } 

    public SeparateChainingHashST(int m) {
        this.m = m;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[m];
        for (int i = 0; i < m; i++)
            st[i] = new SequentialSearchST<Key, Value>();
    } 

    private void resize(int chains) {
        SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<Key, Value>(chains);
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys()) {
                temp.put(key, st[i].get(key));
            }
        }
        this.m  = temp.m;
        this.n  = temp.n;
        this.st = temp.st;
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    } 

    public int size() {
        return n;
    } 

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    } 

    public Value get(Key key) {
        int i = hash(key);
        return st[i].get(key);
    } 

    public void put(Key key, Value val) {
        if (n >= 10*m) resize(2*m);

        int i = hash(key);
        if (!st[i].contains(key)) n++;
        st[i].put(key, val);
    } 
}

class SequentialSearchST<Key, Value> {
    private int n;
    private Node first;

    private class Node {
        private Key key;
        private Value val;
        private Node next;

        public Node(Key key, Value val, Node next)  {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    public SequentialSearchST() {
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    public Value get(Key key) {
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key))
                return x.val;
        }
        return null;
    }

    public void put(Key key, Value val) {
        if (val == null) {
            delete(key);
            return;
        }

        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        }
        first = new Node(key, val, first);
        n++;
    }

    public void delete(Key key) {
        first = delete(first, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;
        if (key.equals(x.key)) {
            n--;
            return x.next;
        }
        x.next = delete(x.next, key);
        return x;
    }
    public Iterable<Key> keys()  {
        Queue<Key> queue = new LinkedList<Key>();
        for (Node x = first; x != null; x = x.next)
            queue.add(x.key);
        return queue;
    }

}