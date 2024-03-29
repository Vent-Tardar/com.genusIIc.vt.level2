package com.genusIIc.vt;

import org.apache.logging.log4j.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        if ((args.length > 2) || (args.length < 2)){
            logger.error("Parameters entered incorrectly");
        } else {
            ComparisonDoc cd = new ComparisonDoc();
            List lst = new ArrayList();
            lst = cd.compare(args[0], args[1]);
            for (Object s : lst) {
                System.out.println(s);
            }
        }
    }
}
