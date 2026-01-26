package com.yodo1.mas.capacitor;

import com.getcapacitor.Logger;

public class Yodo1Mas {

    public String echo(String value) {
        Logger.info("Echo", value);
        return value;
    }
}
