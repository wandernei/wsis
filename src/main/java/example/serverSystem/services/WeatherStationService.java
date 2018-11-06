package example.serverSystem.services;

import example.serverSystem.WeatherStation;
import example.serverSystem.WeatherStationDriver;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@Path("/estacao")
public class WeatherStationService {
	//region vars
	private WeatherStationDriver weatherStation;
	private int stationId;
	//endregion

//    @GET
//    @Path("/{name}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response sayHello(@PathParam("name") String msg) {
//        String output = "Hello, " + msg + "!";
//        return Response.status(200).entity(output).build();
//    }

//    public  void main(String[] args) {

//        try {
//
//            Client client = Client.create();
//
//            WebResource webResource = client
//                    .resource("http://localhost:8080/JavaCloudantApp/rest/estacoes");
//
//            ClientResponse response = webResource.accept("application/json")
//                    .get(ClientResponse.class);
//
//            if (response.getStatus() != 200) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + response.getStatus());
//            }
//
//            String output = response.getEntity(String.class);
//
//            System.out.println("Output from Server .... \n");
//            System.out.println(output);
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }

//    }

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getWeatherStation() throws IOException {
            this.weatherStation = new WeatherStationDriver(11); //inicia o hardware ws p enviar receber(caso n esteja iniciado)
            WeatherStation result = this.weatherStation.ws; //pega os resultados da ws
            return result.toString(); // envia com toString formatado p JSON
    }

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response irrigate(WeatherStation ws) throws IOException {
        this.weatherStation = new WeatherStationDriver(ws.getStationId(), ws.isIrrigating(), ws.getConfiguration()); //inicia o hardware ws p enviar receber(caso n esteja iniciado)
        WeatherStation result = this.weatherStation.ws; //pega os resultados da ws

        String res = "WeatherStation saved : " + ws;
        return Response.status(201).entity(res).build();
    }



}
