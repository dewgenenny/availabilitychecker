package availabilitychecker;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;





import static availabilitychecker.availabilityTester.pingURL;

@Configuration
@Component
@PropertySource("classpath:scheduledTasks.properties")



public class scheduledTasks {


  @Value("${url}")
  String url;


   @Value("${location}")
   String location;

    private static final Logger log = LoggerFactory.getLogger(scheduledTasks.class);

    //private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 10000)

    public void reportCurrentTime() {

        String availability_topic = "/devops_light/"+location+"/status";
        JSONObject obj2 = new JSONObject();
        obj2.put("green",0);
        obj2.put("red",255);
        obj2.put("blue",0);
        obj2.put("transition",1);
        //publishMQTT dimBeforeCheck = new publishMQTT();

        //dimBeforeCheck.writeMQTT(availability_topic,obj2.toJSONString());

        log.info("Starting availability check");
        //availabilitychecker.publishMQTT mqttPublisher = new availabilitychecker.publishMQTT();

        boolean result = false;

        try {
            result = pingURL(url,5000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();

        if(result){
            obj.put("green",255);
            obj.put("red",0);
            obj.put("blue",0);
            obj.put("transition",0);
        }
        else
        {
            obj.put("green",0);
            obj.put("red",255);
            obj.put("blue",0);
            obj.put("transition",0);
        }




        log.info("Availability check complete - "+obj.toJSONString());
        log.info("Publishing result to MQTT");



        publishMQTT publishResults = new publishMQTT();

        publishResults.writeMQTT(availability_topic,obj.toJSONString());

    }
}
