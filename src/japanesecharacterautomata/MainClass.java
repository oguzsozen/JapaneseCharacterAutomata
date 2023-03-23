/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package japanesecharacterautomata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author oguzs
 */
public class MainClass {
    
    public static String prevText = "";

    public static boolean newStringFromStack = false;
    public static boolean useAutomataIndex = false;
    public static boolean pressedBackSpace = false;

    public static void main(String[] args) {

        JapaneseCharacterAutomata automata = new JapaneseCharacterAutomata();

        JFrame frame = new JFrame("Japanese Character Automata");

        JCheckBox cbxEnableJpnChars = new JCheckBox("Enable Japanese Characters", true);

        JRadioButton rbtnHiragana = new JRadioButton("Use Hiragana / ひらがな", true);
        JRadioButton rbtnKatakana = new JRadioButton("Use Katakana / カタカナ");

        JTextArea txtarea = new JTextArea();
        
        txtarea.setLineWrap(true);

        cbxEnableJpnChars.setBounds(250, 35, 190, 20);

        rbtnHiragana.setBounds(40, 20, 165, 20);
        rbtnKatakana.setBounds(40, 50, 165, 20);

        txtarea.setBounds(40, 90, 410, 340);

        txtarea.getCaretPosition();

        txtarea.setText(automata.getStack());

        ButtonGroup bg = new ButtonGroup();

        bg.add(rbtnHiragana);
        bg.add(rbtnKatakana);
        
        cbxEnableJpnChars.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                automata.convertToKana(cbxEnableJpnChars.isSelected());
            }
            
        });
        
        rbtnHiragana.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                automata.useHiragana(true);
            }
        });
        
        rbtnKatakana.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                automata.useHiragana(false);
            }
        });

        txtarea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                changeIndexFunction(e);
            }

            private void changeIndexFunction(CaretEvent e) {
                Runnable changeIndex = new Runnable() {
                    @Override
                    public void run() {
                        //System.out.println(e.getDot());
                        if (useAutomataIndex) {
                            useAutomataIndex = false;
                            txtarea.setCaretPosition(automata.getIndex());
                        } else {
                            automata.changeIndex(e.getDot());
                        }
                    }
                };
                SwingUtilities.invokeLater(changeIndex);
            }
        });
        
        txtarea.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE)
                    pressedBackSpace = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
            
        });

        txtarea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(pressedBackSpace)
                {
                    pressedBackSpace = false;
                    System.out.println(prevText.substring(e.getOffset(), e.getOffset() + 1));
                    
                    automata.removeFromStack(e.getOffset());
                    
                    prevText = txtarea.getText();
                }
                
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                pushToAutomata(e);
                
                prevText = txtarea.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                
            }

            private void pushToAutomata(DocumentEvent e) {

                Runnable pushCharacter = new Runnable() {
                    @Override
                    public void run() {
                        if (newStringFromStack) {
                            newStringFromStack = false;
                            return;
                        }

                        automata.addToStack(txtarea.getText().substring(e.getOffset(), e.getOffset() + 1));

                        System.out.println(txtarea.getText().substring(e.getOffset(), e.getOffset() + 1));

                        if (!automata.getStack().equals(txtarea.getText())) {
                            newStringFromStack = true;
                            useAutomataIndex = true;
                            txtarea.setText(automata.getStack());
                        }
                    }
                };
                SwingUtilities.invokeLater(pushCharacter);
            }

        });

        frame.add(rbtnHiragana);
        frame.add(rbtnKatakana);
        frame.add(cbxEnableJpnChars);
        frame.add(txtarea);

        frame.setBounds(250, 100, 500, 500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);

    }
}
