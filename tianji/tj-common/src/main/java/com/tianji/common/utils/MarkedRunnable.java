package com.tianji.common.utils;

import org.slf4j.MDC;

import java.util.Map;

public class MarkedRunnable implements Runnable{
    private Runnable runnable;
    private Map<String, String> context;
    public MarkedRunnable(Runnable runnable) {
        this.runnable = runnable;
        this.context = MDC.getCopyOfContextMap();
    }

    @Override
    public void run() {
        if(context == null){
            MDC.clear();
        }else {
            MDC.setContextMap(context);
        }
        try {
            runnable.run();
        }finally {
            MDC.clear();
        }
    }
}
