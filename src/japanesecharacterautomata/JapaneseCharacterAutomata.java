/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package japanesecharacterautomata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * The {@code JapaneseCharacterAutomata} class works like a push down automata for
 * convert string of roman characters such as {@code "a", "shi", "no"} to strings of
 * Japanese's Hiragana characters {@code "あ", "し", "の"} and strings of Katakana characters
 * {@code "ア", "シ", "ノ"}.
 *
 *
 *
 * @author Oguz Sozen
 * @since 1.0
 */
public class JapaneseCharacterAutomata {
    
    

    //<editor-fold defaultstate="collapsed" desc="Regex Rules">
    private static Pattern gate_Vowels = Pattern.compile("[aiueo]");

    private static Pattern gate_VowelAUO = Pattern.compile("[auo]");
    private static Pattern gate_VowelI = Pattern.compile("[i]");
    private static Pattern gate_VowelU = Pattern.compile("[u]");

    private static Pattern gate_J = Pattern.compile("j.");

    private static Pattern gate_Sh = Pattern.compile("sh.");

    private static Pattern gate_Ch = Pattern.compile("ch.");

    private static Pattern gate_Y = Pattern.compile("y.");
    private static Pattern gate_Consonants = Pattern.compile("^[kgsztdnhbmpr].*");

    private static Pattern gate_F = Pattern.compile("f.");

    private static Pattern gate_V = Pattern.compile("v.");
    
    private static Pattern gate_LowerK = Pattern.compile("[lx]k[ea]");

    private static Pattern gate_Ts = Pattern.compile("ts.");

    private static Pattern gate_H = Pattern.compile("[td]h.");

    private static Pattern gate_LX = Pattern.compile("^[lx].*");

    private static Pattern gate_W1 = Pattern.compile("w.");

    private static Pattern gate_W2 = Pattern.compile("[td]w.");

    private static Pattern gate_N = Pattern.compile("n[^y]");

    private static Pattern gate_LowerTsu = Pattern.compile("(.)\\1");

    //private static Pattern gate_Punctuations_WhiteSpace = Pattern.compile("[\\p{Punct}\\p{Space}]");
    
    private static Pattern gate_Punctuations_WhiteSpace = Pattern.compile("[\\p{Space}!(),\\-.?]");

    //</editor-fold>
    private static HashMap<String, HashMap<String, ArrayList<String>>> allMaps
            = new HashMap<String, HashMap<String, ArrayList<String>>>();

    private String _stack;
    private int _index;
    private boolean _usingHiragana;
    private boolean _convertToKana;
    
    /**
     * Returns stack value.
     * 
     * @return Stack's value
     */
    public String getStack() {
        return _stack;
    }
    /**
     * Returns index value.
     * 
     * @return Index's value
     */
    public int getIndex() {
        return _index;
    }
    /**
     * Determines whether to use Hiragana or Katakana.
     * 
     * @param value {@code true} is use Hiragana and {@code false} is use Katakana.
     */
    public void useHiragana(boolean value)
    {
        _usingHiragana = value;
    }
    /**
     * Determines whether to convert stack's characters to Japanese Characters.
     * 
     * @param value {@code true} is convert to Japanese Characters and {@code false} is don't
     */
    public void convertToKana(boolean value)
    {
        _convertToKana = value;
    }
    /**
     * {@code JapaneseCharacterAutomata}'s constructor method.
     * 
     */
    public JapaneseCharacterAutomata() {
        _stack = "";
        _index = 0;
        _usingHiragana = true;
        _convertToKana = true;

        getListFromJSON();
    }
    /**
     * Adds new value to stack.
     * 
     * @param input The new value to add to the stack
     */
    public void addToStack(String input) {

        writeToStack(input, null);

        changeIndex(1, null);
        
        if(_convertToKana)
            checkStringRule();
    }
    /**
     * The method of check stack for the find acceptable string where the
     * index points in the stack.
     * 
     */
    private void checkStringRule() {

        int inputLength = 1;
        String input = getInputFromStack(_index, inputLength);
        String output = "";

        //<editor-fold defaultstate="collapsed" desc="Vowels">
        if (gate_Vowels.matcher(input).matches()) {

            //<editor-fold defaultstate="collapsed" desc="Vowel AUO">
            if (gate_VowelAUO.matcher(input).matches()) {
                inputLength = 3;
                input = getInputFromStack(_index, inputLength);

                if (gate_Sh.matcher(input).matches()) {
                    output += getJapaneseCharacter("si");
                    output += getJapaneseCharacter("ly" + input.substring(2, 3));

                    writeToStack(output, input.length());

                    changeIndex(newindexDistance(input, output), false);

                    return;
                }

                if (gate_Ch.matcher(input).matches()) {
                    output += getJapaneseCharacter("ti");
                    output += getJapaneseCharacter("ly" + input.substring(2, 3));

                    writeToStack(output, input.length());

                    changeIndex(newindexDistance(input, output), false);

                    return;
                }

                inputLength = 1;
                input = getInputFromStack(_index, inputLength);

                //<editor-fold defaultstate="collapsed" desc="Vowel U">
                if (gate_VowelU.matcher(input).matches()) {
                    inputLength = 3;
                    input = getInputFromStack(_index, inputLength);

                    if (gate_Ts.matcher(input).matches()) {
                        inputLength++;
                        input = getInputFromStack(_index, inputLength);

                        if (gate_LX.matcher(input).matches()) {
                            output += getJapaneseCharacter("ltu");

                            writeToStack(output, input.length());

                            changeIndex(newindexDistance(input, output), false);

                            return;
                        }

                        inputLength--;
                        input = getInputFromStack(_index, inputLength);

                        output += getJapaneseCharacter("tu");

                        writeToStack(output, input.length());

                        changeIndex(newindexDistance(input, output), false);

                        return;
                    }

                    if (gate_W2.matcher(input).matches()) {
                        output += getJapaneseCharacter(input.substring(0, 1) + "o");
                        output += getJapaneseCharacter("lu");

                        writeToStack(output, input.length());

                        changeIndex(newindexDistance(input, output), false);

                        return;
                    }

                    inputLength = 2;
                    input = getInputFromStack(_index, inputLength);

                    if (gate_W1.matcher(input).matches()) {
                        output += getJapaneseCharacter(input.substring(1));

                        writeToStack(output, input.length());

                        changeIndex(newindexDistance(input, output), false);

                        return;
                    }

                    if (gate_F.matcher(input).matches()) {
                        output += getJapaneseCharacter("hu");

                        writeToStack(output, input.length());

                        changeIndex(newindexDistance(input, output), false);

                        return;
                    }

                    if (gate_V.matcher(input).matches()) {
                        output += getJapaneseCharacter(input);

                        writeToStack(output, input.length());

                        changeIndex(newindexDistance(input, output), false);

                        return;
                    }
                }

                //</editor-fold>
                inputLength = 2;
                input = getInputFromStack(_index, inputLength);

                if (gate_J.matcher(input).matches()) {
                    output += getJapaneseCharacter("zi");
                    output += getJapaneseCharacter("ly" + input.substring(1));

                    writeToStack(output, input.length());

                    changeIndex(newindexDistance(input, output), false);

                    return;
                }

                if (gate_W1.matcher(input).matches()) {
                    output += getJapaneseCharacter(input);

                    writeToStack(output, input.length());

                    changeIndex(newindexDistance(input, output), false);

                    return;
                }

                if (gate_Y.matcher(input).matches()) {
                    inputLength++;
                    input = getInputFromStack(_index, inputLength);

                    if (gate_LX.matcher(input).matches()) {
                        output += getJapaneseCharacter("l" + input.substring(1));

                        writeToStack(output, input.length());

                        changeIndex(newindexDistance(input, output), false);

                        return;
                    }

                    if (gate_Consonants.matcher(input).matches()) {
                        output += getJapaneseCharacter(input.substring(0, 1) + "i");
                        output += getJapaneseCharacter("ly" + input.substring(2));

                        writeToStack(output, input.length());

                        changeIndex(newindexDistance(input, output), false);

                        return;
                    }

                    inputLength--;
                    input = getInputFromStack(_index, inputLength);

                    output += getJapaneseCharacter(input);

                    writeToStack(output, input.length());

                    changeIndex(newindexDistance(input, output), false);

                    return;
                }

                inputLength = 1;
                input = getInputFromStack(_index, inputLength);
            }

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Vowel I">
            if (gate_VowelI.matcher(input).matches()) {
                inputLength = 3;
                input = getInputFromStack(_index, inputLength);

                if (gate_Sh.matcher(input).matches()) {
                    output += getJapaneseCharacter("si");

                    writeToStack(output, input.length());

                    changeIndex(newindexDistance(input, output), false);

                    return;
                }

                if (gate_Ch.matcher(input).matches()) {
                    output += getJapaneseCharacter("ti");

                    writeToStack(output, input.length());

                    changeIndex(newindexDistance(input, output), false);

                    return;
                }

                if (gate_H.matcher(input).matches()) {
                    output += getJapaneseCharacter(input.substring(0, 1) + "e");
                    output += getJapaneseCharacter("li");

                    writeToStack(output, input.length());

                    changeIndex(newindexDistance(input, output), false);

                    return;
                }

                inputLength = 2;
                input = getInputFromStack(_index, inputLength);

                if (gate_J.matcher(input).matches()) {
                    output += getJapaneseCharacter("zi");

                    writeToStack(output, input.length());

                    changeIndex(newindexDistance(input, output), false);

                    return;
                }

                if (gate_Y.matcher(input).matches()) {
                    output += getJapaneseCharacter("i");

                    writeToStack(output, input.length());

                    changeIndex(newindexDistance(input, output), false);

                    return;
                }
            }

            //</editor-fold>
            inputLength = 3;
            input = getInputFromStack(_index, inputLength);

            if (gate_Ts.matcher(input).matches()) {
                output += getJapaneseCharacter("tu");
                output += getJapaneseCharacter("l" + input.substring(2));

                writeToStack(output, input.length());

                changeIndex(newindexDistance(input, output), false);

                return;
            }

            if (gate_Sh.matcher(input).matches()) {
                output += getJapaneseCharacter("si");
                output += getJapaneseCharacter("l" + input.substring(2));

                writeToStack(output, input.length());

                changeIndex(newindexDistance(input, output), false);

                return;
            }

            if (gate_Ch.matcher(input).matches()) {
                output += getJapaneseCharacter("ti");
                output += getJapaneseCharacter("l" + input.substring(2));

                writeToStack(output, input.length());

                changeIndex(newindexDistance(input, output), false);

                return;
            }
            
            if (gate_LowerK.matcher(input).matches()) {
                output += getJapaneseCharacter(input);

                writeToStack(output, input.length());

                changeIndex(newindexDistance(input, output), false);

                return;
            }

            inputLength = 2;
            input = getInputFromStack(_index, inputLength);

            if (gate_F.matcher(input).matches()) {
                output += getJapaneseCharacter("hu");
                output += getJapaneseCharacter("l" + input.substring(1));

                writeToStack(output, input.length());

                changeIndex(newindexDistance(input, output), false);

                return;
            }

            if (gate_J.matcher(input).matches()) {
                output += getJapaneseCharacter("zi");
                output += getJapaneseCharacter("l" + input.substring(1));

                writeToStack(output, input.length());

                changeIndex(newindexDistance(input, output), false);

                return;
            }

            if (gate_W1.matcher(input).matches()) {
                output += getJapaneseCharacter("u");
                output += getJapaneseCharacter("l" + input.substring(1));

                writeToStack(output, input.length());

                changeIndex(newindexDistance(input, output), false);

                return;
            }

            if (gate_V.matcher(input).matches()) {
                output += getJapaneseCharacter("vu");
                output += getJapaneseCharacter("l" + input.substring(1));

                writeToStack(output, input.length());

                changeIndex(newindexDistance(input, output), false);

                return;
            }
            
            if (gate_Y.matcher(input).matches()) {
                output += getJapaneseCharacter("i");
                output += getJapaneseCharacter("l" + input.substring(1));

                writeToStack(output, input.length());

                changeIndex(newindexDistance(input, output), false);

                return;
            }
            
            if (gate_Consonants.matcher(input).matches()) {
                output += getJapaneseCharacter(input);

                writeToStack(output, input.length());

                changeIndex(newindexDistance(input, output), false);

                return;
            }

            if (gate_LX.matcher(input).matches()) {
                output += getJapaneseCharacter("l" + input.substring(1));

                writeToStack(output, input.length());

                changeIndex(newindexDistance(input, output), false);

                return;
            }

            inputLength = 1;
            input = getInputFromStack(_index, inputLength);

            output += getJapaneseCharacter(input);

            writeToStack(output, input.length());

            changeIndex(newindexDistance(input, output), false);

            return;
        }

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Punctuations">
        if (gate_Punctuations_WhiteSpace.matcher(input).matches()) {
            output += getJapaneseCharacter(input);

            writeToStack(output, input.length());

            changeIndex(newindexDistance(input, output), false);

            return;
        }

        //</editor-fold>
        inputLength = 2;
        input = getInputFromStack(_index, inputLength);

        //<editor-fold defaultstate="collapsed" desc="Consonant N">
        if (gate_N.matcher(input).matches()) {
            //For check if used double "n"
            if (gate_LowerTsu.matcher(input).matches()) {
                output += getJapaneseCharacter(input.substring(0, 1));
            } else {
                output += getJapaneseCharacter(input.substring(0, 1));
                output += input.substring(1);
            }

            writeToStack(output, input.length());

            changeIndex(newindexDistance(input, output), false);

            return;
        }

        //</editor-fold>
        inputLength = 2;
        input = getInputFromStack(_index, inputLength);

        //<editor-fold defaultstate="collapsed" desc="Lower Tsu">
        if (gate_LowerTsu.matcher(input).matches()) {
            output += getJapaneseCharacter("ltu");
            output += input.substring(1);

            writeToStack(output, input.length());

            changeIndex(newindexDistance(input, output), false);

            return;
        }

        //</editor-fold>
    }
    /**
     * Increases or decreases the value of the index by {@code value}.
     * 
     * @param value Change amount of index
     * @param plus {@code true} is increases and {@code false} is decreases
     */
    public void changeIndex(int value, Boolean plus) {
        if (plus == null) {
            plus = true;
        }
        if (value == 0) {
            return;
        }
        if (plus) {
            if (_index + value >= _stack.length()) {
                _index = _stack.length();
            } else {
                _index += value;
            }
        } else {
            if (_index - value <= 0) {
                _index = 0;
            } else {
                _index -= value;
            }
        }
    }
    /**
     * Changes index value as {@code newIndex}'s value.
     * 
     * @param newIndex New index value
     */
    public void changeIndex(int newIndex) {
        if (newIndex < 0) {
            _index = 0;
        } else if (newIndex > _stack.length()) {
            _index = _stack.length();
        } else {
            _index = newIndex;
        }
    }
    /**
     * Returns string from the directory that {@code paramIndex}
     * points to in the stack.
     * 
     * @param paramIndex The index of string to return
     * @param Length The length of string to return
     * 
     * @return The string of to return from stack
     */
    private String getInputFromStack(int paramIndex, int Length) {
        int stringStartIndex;

        if (Length < 1) {
            Length = 1;
        }

        if (paramIndex - Length < 0) {
            stringStartIndex = 0;
        } else {
            stringStartIndex = paramIndex - Length;
        }

        return _stack.substring(stringStartIndex, paramIndex);
    }
    /**
     * Returns string from the string accepted by language in the {@code allMaps}.
     * 
     * @param input String accepted by language
     * 
     * @return The string of to return from {@code allMaps}
     */
    private String getJapaneseCharacter(String input) {
        // Default Value
        String searchedMapName = "punctuations";

        int kanaType = (_usingHiragana) ? 0 : 1;

        for (String mapName : allMaps.keySet()) {
            if (mapName.substring(mapName.length() - 1, mapName.length()).equals(input.substring(input.length() - 1, input.length()))) {
                searchedMapName = mapName;
                break;
            }
        }

        return allMaps.get(searchedMapName).get(input).get(kanaType);
    }

    private void writeToStack(String substring, Integer removingCharCount) {
        if (removingCharCount == null) {
            removingCharCount = 0;
        }

        _stack = _stack.substring(0, _index - removingCharCount)
                .concat(substring)
                .concat(_stack.substring(_index, _stack.length()));
    }
    
    /**
     * Removes the characters from the directory that {@code removingCharIndex}
     * points to in the stack.
     * 
     * @param removingCharIndex The index of characters to remove
     */
    public void removeFromStack(int removingCharIndex) {
        removeFromStack(removingCharIndex, 1);
    }
    
    /**
     * Removes the characters from the directory that {@code removingCharIndex}
     * points to in the stack.
     * 
     * @param removingCharIndex The index of characters to remove
     * @param removingCharCount The count of characters to remove
     */
    public void removeFromStack(int removingCharIndex, int removingCharCount) {
        
        if(removingCharIndex < 0 || removingCharIndex + removingCharCount > _stack.length())
            return;
        
        changeIndex(removingCharCount, false);
        
        _stack = _stack.substring(0, removingCharIndex)
                .concat(_stack.substring(removingCharIndex + removingCharCount, _stack.length()));
    }
    /**
     * Sets Japanese Characters with keys to {@code allMaps}.
     * 
     */
    private static void getListFromJSON() {
        String jsonText = JsonReader.getJsonFromFile("src/kanastrings/KanaStrings.json");

        try {
            JSONParser parser = new JSONParser();
            JSONObject mainDoct = (JSONObject) parser.parse(jsonText);

            for (Object mapKeyObject : mainDoct.keySet()) {

                String mapKey = (String) mapKeyObject;

                JSONObject mapObject = (JSONObject) mainDoct.get(mapKey);

                HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

                for (Object charKeyObject : mapObject.keySet()) {
                    String charKey = (String) charKeyObject;

                    ArrayList<String> charList = new ArrayList<String>();

                    JSONArray charListObject = (JSONArray) mapObject.get(charKey);

                    for (Object charObject : charListObject) {
                        charList.add((String) charObject);
                    }

                    map.put(charKey, charList);
                }

                allMaps.put(mapKey, map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int newindexDistance(String input, String output) {
        return input.length() - output.length();
    }
}
