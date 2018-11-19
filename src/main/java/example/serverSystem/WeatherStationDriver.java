package example.serverSystem;

import java.io.IOException;

public class WeatherStationDriver {
//region vars
	public WeatherStation ws;
	public WsMqttMessage msg;
	private String messageResponse;
//endregion

	public int getWsId(){ return this.ws.getStationId(); }

	public WeatherStationDriver(int stationId, boolean irrigate, String configuration) throws IOException {
        this.msg = new WsMqttMessage();
        // quando cria Ws manda stationId para Ws comecar a enviar dados
        this.ws = new WeatherStation(stationId, irrigate, configuration);

        msg.transceive(this.ws.toString(),"127.0.0.1",1883); // envia p Ws
        messageResponse = msg.getMessage();
        this.ws.setWeatherStation(messageResponse);
	}

	public WeatherStationDriver(int stationId, boolean irrigate) throws IOException {
		this.msg = new WsMqttMessage();
		// quando cria Ws manda stationId para Ws comecar a enviar dados
		this.ws = new WeatherStation(stationId, irrigate);

		msg.transceive(this.ws.toString(),"127.0.0.1",1883); // envia p Ws
		messageResponse = msg.getMessage();
		this.ws.setWeatherStation(messageResponse);
	}

	public WeatherStationDriver(int stationId) throws IOException {
		this.msg = new WsMqttMessage();
		// quando cria Ws manda stationId para Ws comecar a enviar dados
		this.ws = new WeatherStation(stationId);

		msg.transceive(this.ws.toString(),"127.0.0.1",1883); // envia p Ws
		messageResponse = msg.getMessage();
		this.ws.setWeatherStation(messageResponse);
	}


    public WeatherStationDriver() throws IOException {
        this.msg = new WsMqttMessage();
        // quando cria Ws manda stationId para Ws comecar a enviar dados
        this.ws = new WeatherStation();

        msg.transceive(this.ws.toString(),"127.0.0.1",1883); // envia p Ws
        messageResponse = msg.getMessage();
        this.ws.setWeatherStation(messageResponse);
    }

}
