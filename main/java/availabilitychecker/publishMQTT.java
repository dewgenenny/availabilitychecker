package availabilitychecker;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URISyntaxException;

/**
 * Created by tom on 19.03.17.
 */


@Component
@PropertySource("classpath:publishMQTT.properties")


public class publishMQTT {

    private static final Logger log = LoggerFactory.getLogger(scheduledTasks.class);
    private final String mqttHost = "192.168.178.20";
    private final  String mqttPort = "1883";


    //static String mqttHost_static = new String (mqttHost);

    public boolean writeMQTT(String topic, String content) {



        MQTT mqtt = new MQTT();

        mqtt.setConnectAttemptsMax(5);
        try {
            log.info("Connecting to host: "+mqttHost);
            mqtt.setHost(mqttHost, Integer.parseInt(mqttPort));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // or
     //   log.info("Got to connection creation");
        BlockingConnection connection = mqtt.blockingConnection();
        try {
      //  log.info("about to connect");
            connection.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            log.info("Publishing to "+topic);
            connection.publish(topic, content.getBytes(), QoS.AT_LEAST_ONCE, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            connection.disconnect();
          //  log.info("successfully disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
