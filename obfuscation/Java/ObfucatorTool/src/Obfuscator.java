import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;

/**
 * Created by harry on 29/04/2017.
 */
public class Obfuscator {
    public static void main(String[] args){

        runPythonScript("test.java");
        encryptXML();

        System.out.print("Get Obfuscated");

    }

    // Runs python script, takes class file as input
    private static void runPythonScript(String classFile){
        try {
            Runtime.getRuntime().exec("python obfuscateScript.py" + classFile);
        } catch (IOException e) {}

    }

    private static void encryptXML(){
        try {
            String pathOfXML = "strings.xml";
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuilder = null;
            dbuilder = dFactory.newDocumentBuilder();
            Document doc = dbuilder.parse(pathOfXML);

            // Root element
            Node root = doc.getFirstChild();
            NodeList stringList = root.getChildNodes();
            for (int i=0 ; i < stringList.getLength(); i++){ //loops through each node
                Node node = stringList.item(i);
                if (node.getNodeName().equals("string")){
                    System.out.println(node.getTextContent());
                    Node newNode = node.cloneNode(true);
                    newNode.setTextContent("hello");
                    root.appendChild(newNode);
                    i++;
                }

            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void insertDecryptionMethod(){
        //TODO
    }

}
