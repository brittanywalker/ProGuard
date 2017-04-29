import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
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
                    String s = node.getTextContent();
                    //TODO encrypt
                    s = s + " hello";
                    // Sets the text content
                    node.setTextContent(s);



                }

            }

            //Once all changes have been made to DOM, write to XML
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer trans = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(pathOfXML));
            trans.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    private static void insertDecryptionMethod(){
        //TODO
    }

}
