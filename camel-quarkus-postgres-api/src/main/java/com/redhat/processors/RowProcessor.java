package com.redhat.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.redhat.models.Key;
import com.redhat.models.KeyList;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

 
public class RowProcessor implements Processor {
 
    public void process(Exchange exchange) throws Exception {
        List<Map<String, Object>> rows = exchange.getIn().getBody(List.class);
        //List<Key> keys = new ArrayList<Key>();
        Key key = new Key();
        for (Map<String, Object> row : rows) {
 
            key.setId(((Number) row.get("_id")).intValue());
            key.setKey((String) row.get("_key"));
            key.setValue((String) row.get("_value"));
            //keys.add(key);
        }
        exchange.getOut().setBody(key);
    }
}
