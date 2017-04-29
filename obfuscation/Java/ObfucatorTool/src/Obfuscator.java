import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by harry on 29/04/2017.
 */
public class Obfuscator {
    public static void main(String[] args){
        //TODO run python for each file, and auto find XML file / strings
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
                    s = encryptString(s);
                    // Sets the text content
                    node.setTextContent(s);
                }

            }

            //Once all changes have been made to DOM, write to XML
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer trans = tFactory.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
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

    private static String encryptString(String s){
        // Generate secret key
        String encryptedString = "";
        String pass = "password12345678";
        SecretKeySpec secretKey = new SecretKeySpec(pass.getBytes(), "AES");

        // Encrypt string
        try {
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedString = new String(c.doFinal(s.getBytes("UTF-8"))); // TODO Is this the right way??

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return encryptedString;
    }

    private static void insertDecryptionMethod(){
        //TODO
    }

}
