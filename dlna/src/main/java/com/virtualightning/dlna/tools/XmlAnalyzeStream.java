package com.virtualightning.dlna.tools;

import org.xml.sax.Attributes;

import java.util.LinkedList;
import java.util.List;

public class XmlAnalyzeStream {
    private final String elementName;
    private final boolean isRepeatElem;
    private boolean needNotmatch;
    private boolean forceRoot;
    boolean needCharacterValue;
    private List<XmlAnalyzeStream> childStreams;
    final OnElementCallback callback;
    private XmlAnalyzeStream parentStream;

    public XmlAnalyzeStream(String elementName) {
        this(elementName,false,null);
    }

    public XmlAnalyzeStream(String elementName, boolean isRepeatElem) {
        this(elementName,isRepeatElem,null);
    }

    public XmlAnalyzeStream(String elementName, boolean isRepeatElem, OnElementCallback callback) {
        this.elementName = elementName;
        this.isRepeatElem = isRepeatElem;
        this.callback = callback;
        this.needNotmatch = true;
        this.forceRoot = false;
        if(this.callback != null) {
            needCharacterValue = callback.needCharacterValue;
            this.callback.stream = this;
        }
    }

    public XmlAnalyzeStream addChildElement(XmlAnalyzeStream stream) {
        if(childStreams == null)
            childStreams = new LinkedList<>();

        stream.parentStream = this;
        childStreams.add(stream);
        return this;
    }

    public XmlAnalyzeStream notMatch(boolean needNotmatch) {
        this.needNotmatch = needNotmatch;
        return this;
    }

    public XmlAnalyzeStream forceRoot(boolean forceRoot) {
        this.forceRoot = forceRoot;
        return this;
    }


    XmlAnalyzeStream startElement(String elementName, Attributes attributes, boolean isFirstSearch){
        if(elementName.equals(this.elementName)) {
            if(callback != null)
                callback.onElementStart(attributes);

            if(forceRoot)
                forceRoot = false;
            return this;
        }
        XmlAnalyzeStream selectStream;
        if(!forceRoot && isFirstSearch && childStreams != null) {
            for (XmlAnalyzeStream stream : childStreams) {
                selectStream = stream.startElement(elementName,attributes,false);
                if(selectStream != null)
                    return selectStream;
            }
        }

        return null;
    }

    void characters(char[] ch, int start, int length) {
        if(needCharacterValue && callback != null)
            callback.onElementValue(new String(ch,start,length));
    }

    void endElement() {
        if(parentStream != null && !isRepeatElem) {
            parentStream.childStreams.remove(this);
        }

        if(callback != null)
            callback.onElementEnd();
    }

    String getElementName() {
        return elementName;
    }

    public static class OnElementCallback {
        protected boolean needCharacterValue;
        protected XmlAnalyzeStream stream;
        protected OnElementCallback() {
            this(false);
        }

        protected OnElementCallback(boolean needCharacterValue) {
            this.needCharacterValue = needCharacterValue;
        }

        public void onElementStart(Attributes attributes) {
        }

        public void onElementValue(String value) {

        }

        public void onElementEnd() {

        }

        protected String getElementName() {
            return stream.getElementName();
        }
    }


    public static class AnalyzerChain {
        private final XmlAnalyzeStream rootNode;
        private XmlAnalyzeStream measureNode;
        private XmlAnalyzeStream currentNode;
        private boolean isNotMatch;
        private String notMatchElementName;

        public AnalyzerChain(XmlAnalyzeStream rootNode) {
            this.measureNode = rootNode;
            this.rootNode = rootNode;
        }

        public void startElement(String elementName,Attributes attributes){
            if(isNotMatch)
                return;
            if(measureNode != null)
                currentNode = measureNode.startElement(elementName, attributes, true);
            if(currentNode != null) {
                measureNode = currentNode;
            } else {
                if(measureNode == null || measureNode.needNotmatch) {
                    isNotMatch = true;
                    notMatchElementName = elementName;
                }
            }
        }

        public void characters(char[] ch, int start, int length) {
            if(isNotMatch)
                return;
            if(currentNode != null)
                currentNode.characters(ch,start,length);
        }

        public void endElement(String elementName) {
            if(isNotMatch) {
                if(notMatchElementName.equals(elementName))
                    isNotMatch = false;
                return;
            }
            if(currentNode != null) {
                measureNode = currentNode.parentStream;
                currentNode.endElement();
                currentNode = measureNode;
            }
        }
    }


    public static class DefaultElement extends XmlAnalyzeStream {
        boolean isStart;
        boolean hasChild;
        String tempStreamName;

        public DefaultElement(boolean isRepeatElem,OnElementCallback callback) {
            super(null,isRepeatElem, callback);
        }

        @Override
        public XmlAnalyzeStream addChildElement(XmlAnalyzeStream stream) {
            return this;
        }

        @Override
        XmlAnalyzeStream startElement(String elementName, Attributes attributes, boolean isFirstSearch) {
            if(!isStart) {
                isStart = true;
                tempStreamName = elementName;
                if(callback != null)
                    callback.onElementStart(attributes);
                return this;
            } else {
                hasChild = true;
                return null;
            }
        }

        @Override
        void characters(char[] ch, int start, int length) {
            if(hasChild)
                return;

            super.characters(ch, start, length);
        }

        @Override
        void endElement() {
            isStart = false;
            if(hasChild)
                hasChild = false;

            super.endElement();
        }

        @Override
        String getElementName() {
            return tempStreamName;
        }
    }
}
