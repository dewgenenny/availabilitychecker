package availabilitychecker;
/**
 * Created by tom on 19.03.17.
 */

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class availabilityTester {
/*
    public static void main(String[]args){


        boolean result = pingURL("https://api.volkswagen.com/health",150);
        System.out.println(result);

    }

*/
private static final Logger log = LoggerFactory.getLogger(scheduledTasks.class);

    /**
     * Pings a HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in
     * the 200-399 range.
     * @param url The HTTP URL to be pinged.
     * @param timeout The timeout in millis for both the connection timeout and the response read timeout. Note that
     * the total timeout is effectively two times the given timeout.
     * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
     * given timeout, otherwise <code>false</code>.
     */
    public static boolean pingURL(String url, int timeout) throws ParseException {
        // url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.
        boolean total_status = true;

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(url);


        JSONObject jsonObj = (JSONObject) obj;


        JSONArray msg = (JSONArray) jsonObj.get("urls");
        Iterator<String> iterator = msg.iterator();
        while (iterator.hasNext()) {
            // System.out.println(iterator.next());

            try {
                String urltoCheck = iterator.next();
                log.info("Checking "+ urltoCheck);
                HttpsURLConnection connection = (HttpsURLConnection) new URL(urltoCheck).openConnection();
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
                connection.setRequestMethod("HEAD");
                int responseCode = connection.getResponseCode();
                //System.out.println(responseCode);
                Object o = connection.getContent();
                boolean return_code = (200 <= responseCode && responseCode <= 399);
                //log.info(Boolean.toString(return_code));
                //System.out.println(o.getClass().getName());
                total_status = total_status && return_code;



            } catch (IOException exception) {
                return false;
            }

        }
        return total_status;



    }

}