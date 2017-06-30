package com.virtualightning.dlna;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.virtualightning.dlna.interfaces.option.XmlDecoder;
import com.virtualightning.dlna.tools.XmlAnalyzeStream;

public abstract class XmlBaseHandler<T> extends DefaultHandler implements XmlDecoder<T> {
    private XmlAnalyzeStream.AnalyzerChain analyzerChain;

    @Override
    public boolean decoderXMLStream(T t,InputStream inputStream) {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            analyzerChain = getAnalyzerChain();
            parser.parse(inputStream,this);
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (ParserConfigurationException e) {
            return false;
        } finally {
            if(inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {}
        }
        return true;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        analyzerChain.startElement(qName,attributes);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        analyzerChain.characters(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        analyzerChain.endElement(qName);
    }

    protected abstract XmlAnalyzeStream.AnalyzerChain getAnalyzerChain();
}
