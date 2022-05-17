package com.redhat.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.redhat.models.Key;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MapProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        // take the Employee object from the exchange and create the parameter map
        Key key = exchange.getIn().getBody(Key.class);
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put("key", key.getKey());
        keyMap.put("value", key.getValue());
        keyMap.put("id", key.getId());
        exchange.getIn().setBody(keyMap);
    }
}
