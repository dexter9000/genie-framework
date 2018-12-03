package com.genie.generator.uml;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 格式化xmi文件
 */
public class XmiFileFormat {

    private static Map<String, String> primitiveTypes;

    static {
        primitiveTypes = new HashMap<>();
        primitiveTypes.put("String", UUID.randomUUID().toString());
        primitiveTypes.put("Date", UUID.randomUUID().toString());
        primitiveTypes.put("Integer", UUID.randomUUID().toString());
        primitiveTypes.put("Boolean", UUID.randomUUID().toString());
        primitiveTypes.put("Long", UUID.randomUUID().toString());
        primitiveTypes.put("Double", UUID.randomUUID().toString());
        primitiveTypes.put("EDate", primitiveTypes.get("Date"));
        primitiveTypes.put("ELong", primitiveTypes.get("Long"));
        primitiveTypes.put("EDouble", primitiveTypes.get("Double"));
    }
    public static String generate(InputStream from, String packagePath) {
        try {
            Document doc = convert(from);
            Source source = new DOMSource(doc);
            OutputStream outputStream = new ByteArrayOutputStream();
            Result result = new StreamResult(outputStream);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            return outputStream.toString();
        } catch (Exception e) {
            System.out.println("error");
        }
        return null;
    }


    public static void generateFile(InputStream from, String toPath, String packagePath) {
        try {
            /* 通过文件方式读取,注意文件保存的编码和文件的声明编码要一致(默认文件声明是UTF-8) */
            Document doc = convert(from);

            Source source = new DOMSource(doc);
            Result result = new StreamResult(new File(toPath));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

        } catch (Exception e) {
            System.out.println("error");
        }
    }

    public static void generate(String fromFile, String toPath, String packagePath) {
        try {
            /* 通过文件方式读取,注意文件保存的编码和文件的声明编码要一致(默认文件声明是UTF-8) */
            File file = new File(fromFile);
            InputStream is = new FileInputStream(file);
            Document doc = convert(is);

            Source source = new DOMSource(doc);
            Result result = new StreamResult(new File(toPath));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

        } catch (Exception e) {
            System.out.println("error");
        }
    }

    private static Document convert(InputStream input) throws SAXException, IOException, XPathExpressionException, ParserConfigurationException {
        Document doc;
        DocumentBuilder builder = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        doc = builder.parse(input);

        XPathFactory xfactory =XPathFactory.newInstance();
        XPath xpath = xfactory.newXPath();

        XPathExpression expr =
            xpath.compile("//ownedAttribute/type");

        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength();i++)  {
            Node node = nodes.item(i);
            String typeStr = node.getAttributes().getNamedItem("href").getTextContent().split("#")[1];
            Node p = node.getParentNode();
            p.removeChild(node);
            Attr attr = doc.createAttribute("type");
            attr.setValue(primitiveTypes.get(typeStr));
            p.getAttributes().setNamedItem(attr);
        }

        Node root = doc.getElementsByTagName("uml:Model").item(0);

        root.getAttributes().getNamedItem("xmlns:uml").setNodeValue("http://www.eclipse.org/uml2/5.0.0/UML");

        root.appendChild(createPrimitiveType(doc, "String"));
        root.appendChild(createPrimitiveType(doc, "Date"));
        root.appendChild(createPrimitiveType(doc, "Integer"));
        root.appendChild(createPrimitiveType(doc, "Long"));
        root.appendChild(createPrimitiveType(doc, "Double"));
        root.appendChild(createPrimitiveType(doc, "Boolean"));
        return doc;
    }

    private static Node createPrimitiveType(Document doc, String type) {
        Node result = doc.createElement("packagedElement");
        Attr xmiType = doc.createAttribute("xmi:type");
        xmiType.setValue("uml:PrimitiveType");
        Attr xmiId = doc.createAttribute("xmi:id");
        xmiId.setValue(primitiveTypes.get(type));
        Attr name = doc.createAttribute("name");
        name.setValue(type);
        result.getAttributes().setNamedItem(xmiType);
        result.getAttributes().setNamedItem(xmiId);
        result.getAttributes().setNamedItem(name);
        return result;
    }

}
