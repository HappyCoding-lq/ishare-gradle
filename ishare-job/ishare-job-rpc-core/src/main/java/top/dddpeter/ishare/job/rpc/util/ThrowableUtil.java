package top.dddpeter.ishare.job.rpc.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author hqins 2019-12-10
 */
public class ThrowableUtil {

    /**
     * parse error to string
     *
     * @param e
     * @return
     */
    public static String toString(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String errorMsg = stringWriter.toString();
        return errorMsg;
    }

}
