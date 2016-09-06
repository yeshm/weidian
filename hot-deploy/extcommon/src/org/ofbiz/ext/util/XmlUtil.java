package org.ofbiz.ext.util;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.ofbiz.base.util.UtilValidate;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * XML助手类
 */
public class XmlUtil {

    public static String formatXML(String inputXML) throws Exception {
        if(UtilValidate.isWhitespace(inputXML)) return inputXML;

        SAXReader reader = new SAXReader();
        Document document = reader.read(new StringReader(inputXML));
        String requestXML = null;
        XMLWriter writer = null;
        if (document != null) {
            try {
                StringWriter stringWriter = new StringWriter();
                OutputFormat format = new OutputFormat(" ", true);
                writer = new XMLWriter(stringWriter, format);
                writer.write(document);
                writer.flush();
                requestXML = stringWriter.getBuffer().toString();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return requestXML;
    }
}
