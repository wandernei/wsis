package example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;

public class wsMqttMessage implements MqttCallback {
//region vars
    private static ArrayList<MqttMessage> mqttMessages;
    private static MqttMessage mqttMessageReceived;
//endregion

//    public static void main(String[] args) {
//        //String msg="{\"stationId\":33,\"temp\":17,\"humidity\":26,\"irrigate\":0}";
//        String msg = "{\"stationId\":11,\"irrigate\":1}";
//
//        new wsMqttMessage().transceive(msg);
//    }

    public void transceive(String msg, String ip, int port) {
        MemoryPersistence persistence = new MemoryPersistence();
        String broker = "tcp://"+ip+":"+Integer.toString(port);
        String clientId = broker + " Sending";

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            client.connect(connOpts);
            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) {
//                    mqttMessages.add(mqttMessage);
                    mqttMessageReceived = mqttMessage;
//                    System.out.println("-------------------------------------------------");
//                    System.out.println("| Topic:" + topic);
//                    System.out.println("| Message: " + new String(mqttMessage.getPayload()));
//                    System.out.println("-------------------------------------------------");
                }

                public void deliveryComplete(IMqttDeliveryToken token) {}
            });
            client.subscribe("getData",2); //RECEBER

            MqttMessage message = new MqttMessage();
            message.setPayload(msg
                    .getBytes());
            client.publish("setData", message); //ENVIAR
            Thread.sleep(1000);
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void receive(String ip, int port) {
        MemoryPersistence persistence = new MemoryPersistence();
        String s = persistence.toString();
        String broker = "tcp://"+ip+":"+Integer.toString(port);
        String clientId = broker + " Sending";

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            client.connect(connOpts);
            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) {
//                    mqttMessages.add(mqttMessage);
                    mqttMessageReceived = mqttMessage;
//                    System.out.println("-------------------------------------------------");
//                    System.out.println("| Topic:" + topic);
//                    System.out.println("| Message: " + new String(mqttMessage.getPayload()));
//                    System.out.println("-------------------------------------------------");
                }

                public void deliveryComplete(IMqttDeliveryToken token) {}
            });
            client.subscribe("getData",2); //RECEBER

            Thread.sleep(1000);
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Conexao perdida favor reconectar.");
//        String[] m = {}; main(m);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        mqttMessageReceived = message;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete, Mensagem entrega!");
    }

    public String getMessage() {
        return new String(mqttMessageReceived.getPayload());
    }

    public ArrayList<MqttMessage> getMessages() {
        return mqttMessages;
    }

}
            


  
