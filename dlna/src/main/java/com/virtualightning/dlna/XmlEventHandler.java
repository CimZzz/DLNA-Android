package com.virtualightning.dlna;

import org.xml.sax.Attributes;

import java.util.HashMap;

import com.virtualightning.dlna.tools.XmlAnalyzeStream;


public class XmlEventHandler extends XmlBaseHandler<SubscribeEvent> {
    private final XmlAnalyzeStream.AnalyzerChain analyzerChain;

    XmlEventHandler(final SubscribeEvent subscribeEvent) {
        subscribeEvent.feature = new HashMap<>();

        XmlAnalyzeStream root = new XmlAnalyzeStream("InstanceID",true,new XmlAnalyzeStream.OnElementCallback(){
            @Override
            public void onElementStart(Attributes attributes) {
                subscribeEvent.instanceId = Integer.parseInt(attributes.getValue("val"));
            }
        })
                .notMatch(false)
                .forceRoot(true)
                .addChildElement(new XmlAnalyzeStream.DefaultElement(true,new XmlAnalyzeStream.OnElementCallback(){
                    @Override
                    public void onElementStart(Attributes attributes) {
                        subscribeEvent.feature.put(getElementName(),attributes.getValue("val"));
                    }
                }));

        analyzerChain = new XmlAnalyzeStream.AnalyzerChain(root);
    }

    @Override
    protected XmlAnalyzeStream.AnalyzerChain getAnalyzerChain() {
        return analyzerChain;
    }
}
