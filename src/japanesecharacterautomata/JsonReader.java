/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package japanesecharacterautomata;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author oguzs
 */
public class JsonReader {
    
    public static String getJsonFromFile(String filename)
    {
        String jsonText = "";
        
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            
            String line;
            
            while((line = reader.readLine()) != null)
            {
                jsonText += line + "\n";
            }
            
            reader.close();
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return jsonText;
    }
}
