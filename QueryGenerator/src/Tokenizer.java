
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vinicius
 */
public class Tokenizer extends DefaultHandler{
    private Set<String> tokens;
    
    public Tokenizer(){
        this.tokens = new HashSet<>();
    }
    
    @Override
    public void startDocument(){
    }
    
    @Override
    public void endDocument(){
    }

    @Override
    public void startElement(String uri, String name, String qName, Attributes atts){
        String label = "";
        if ("".equals (uri))
            label = qName;
        else
            label = name;
        
        tokens.add(label);
    }
    
    public void characters (char ch[], int start, int length){
        String nodeContent = "";
        List<String> nodeTokens = new ArrayList<>();
	for (int i = start; i < start + length; i++) {
	    if(!(ch[i] == '\\' || ch[i] == '"' || ch[i] == '\r' || ch[i] == '\t' || ch[i] == '\n')){
                //System.out.print(""+ch[i]);
                nodeContent = (nodeContent+ch[i]);
            }
	}
        //System.out.println("");
        nodeTokens = new ArrayList<>(Arrays.asList(nodeContent.split("([.,;:_ /#@!?~`|\"'{})(*&^%$-])+")));
        
        nodeTokens.remove("");
        this.tokens.addAll(nodeTokens);
        
    }

    public Set<String> getTokens() {
        return tokens;
    }

    public void setTokens(Set<String> tokens) {
        this.tokens = tokens;
    }
    
    
}
