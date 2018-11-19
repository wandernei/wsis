package example.serverSystem.services;

import example.serverSystem.WeatherStation;
import example.serverSystem.WeatherStationDriver;
import example.serverSystem.WsMqttMessage;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

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
//    @Path("/{stationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getWeatherStation() throws IOException { // @PathParam("stationId") String stId
	    this.weatherStation = new WeatherStationDriver(); //inicia o hardware ws p enviar receber(caso n esteja iniciado)
//            this.weatherStation = new WeatherStationDriver(Integer.parseInt(stId)); //inicia o hardware ws p enviar receber(caso n esteja iniciado)
        WeatherStation result = this.weatherStation.ws; //pega os resultados da ws
        return result.toString(); // envia com toString formatado p JSON
}

    @GET
    @Path("/irrigate/{irrigate}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response irrigate(@PathParam("irrigate") String irr) throws IOException {
        int wsId = new WeatherStationDriver().ws.getStationId();

	    this.weatherStation = new WeatherStationDriver(wsId, Boolean.parseBoolean(irr)); //atualiza o comando irrigar
        WeatherStation result = this.weatherStation.ws; //pega os resultados da ws

        String res = "WeatherStation saved : \n" + result.toString();
        return Response.status(201).entity(res).build();
    }

    @GET
    @Path("/setId/{stationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setId(@PathParam("stationId") String sId) throws IOException {
        this.weatherStation = new WeatherStationDriver( Integer.parseInt(sId) ); //atualiza o comando irrigar
        WeatherStation result = this.weatherStation.ws; //pega os resultados da ws

        String res = "WeatherStation id changed : \n" + result.toString();
        return Response.status(201).entity(res).build();
    }



}
