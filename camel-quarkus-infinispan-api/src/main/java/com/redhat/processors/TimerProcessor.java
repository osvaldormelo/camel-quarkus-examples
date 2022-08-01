package com.redhat.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class TimerProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        long timeBegan  = Long.parseLong(exchange.getIn().getHeader("startTimer").toString());
        long timeFinish  = Long.parseLong(exchange.getIn().getHeader("stopTimer").toString());
        float time = timeFinish - timeBegan;
        exchange.getIn().setHeader("timer", String.valueOf(time)); 
    }
}