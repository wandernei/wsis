package example.serverSystem;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.AllDocsRequest;
import com.cloudant.client.org.lightcouch.CouchDbException;
import com.google.gson.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class WeatherStationController {
	//region vars
	private static CloudantClient cloudant = null;
	private static Database db = null;
	private static String user = "6aa44b53-233e-4a74-b9b1-97845f434407-bluemix";
	private static String password = "704deeb0161785a2c03911c1c8e8b2acbe991afb6a2e178c79e4cc0efb2024e2";
    private static String VCAP_SERVICES = "{ \"AvailabilityMonitoring\": [{ \"binding_name\": null, \"credentials\": { \"cred_url\": \"https://perfbroker.ng.bluemix.net\", \"token\": \"mPotbm7z2enx7pDwcrQqrAxj8klGsrifX3rvqEQVcsSXn6GGkMjqreD8qsattsX/B+cQzRIs2jwrEW4GF++ZuestQPBS8IP4RNEWgMZwjXo=\" }, \"instance_name\": \"availability-monitoring-auto\", \"label\": \"AvailabilityMonitoring\", \"name\": \"availability-monitoring-auto\", \"plan\": \"Lite\", \"provider\": null, \"syslog_drain_url\": null, \"tags\": [ \"ibm_created\", \"bluemix_extensions\", \"dev_ops\", \"lite\" ], \"volume_mounts\": [] }], \"cloudantNoSQLDB\": [{ \"binding_name\": null, \"credentials\": { \"apikey\": \"LeGylJU17ND6tWCxd1ZJBgKs3N-TtS_GZ2HwYsgwlQJ6\", \"host\": \"9236e842-1401-409a-901e-0a91cee20e00-bluemix.cloudant.com\", \"iam_apikey_description\": \"Auto generated apikey during resource-bind operation for Instance - crn:v1:bluemix:public:cloudantnosqldb:us-south:a/03d989cfe3e1462cb75fb5c7bf6fda08:906c9ac6-4fa2-4b5f-9716-d273c1593312::\", \"iam_apikey_name\": \"auto-generated-apikey-888bd84e-5df4-4849-80d5-5404f05ce9af\", \"iam_role_crn\": \"crn:v1:bluemix:public:iam::::serviceRole:Writer\", \"iam_serviceid_crn\": \"crn:v1:bluemix:public:iam-identity::a/03d989cfe3e1462cb75fb5c7bf6fda08::serviceid:ServiceId-9a6d4c47-4124-4cbe-8f5b-602d0619931d\", \"password\": \"b12d6f0f7955b22e00271580559017ed0cd2ab06ded867c9bfffd433ef9e2f66\", \"port\": 443, \"url\": \"https://9236e842-1401-409a-901e-0a91cee20e00-bluemix:b12d6f0f7955b22e00271580559017ed0cd2ab06ded867c9bfffd433ef9e2f66@9236e842-1401-409a-901e-0a91cee20e00-bluemix.cloudant.com\", \"username\": \"9236e842-1401-409a-901e-0a91cee20e00-bluemix\" }, \"instance_name\": \"wandernei-wsi-cloudantNoSQLDB\", \"label\": \"cloudantNoSQLDB\", \"name\": \"wandernei-wsi-cloudantNoSQLDB\", \"plan\": \"Lite\", \"provider\": null, \"syslog_drain_url\": null, \"tags\": [ \"data_management\", \"ibm_created\", \"lite\", \"ibm_dedicated_public\", \"ibmcloud-alias\" ], \"volume_mounts\": [] }], \"iotf-service\": [{ \"binding_name\": null, \"credentials\": { \"apiKey\": \"a-j5nr7b-zbn9rvfc2k\", \"apiToken\": \")MYvddHj0hEx5eGapg\", \"http_host\": \"j5nr7b.internetofthings.ibmcloud.com\", \"iotCredentialsIdentifier\": \"a2g6k39sl6r5\", \"mqtt_host\": \"j5nr7b.messaging.internetofthings.ibmcloud.com\", \"mqtt_s_port\": 8883, \"mqtt_u_port\": 1883, \"org\": \"j5nr7b\" }, \"instance_name\": \"server001ws-iotf-service\", \"label\": \"iotf-service\", \"name\": \"server001ws-iotf-service\", \"plan\": \"iotf-service-free\", \"provider\": null, \"syslog_drain_url\": null, \"tags\": [ \"internet_of_things\", \"Internet of Things\", \"ibm_created\", \"ibm_dedicated_public\", \"lite\" ], \"volume_mounts\": [] }, { \"binding_name\": null, \"credentials\": { \"apiKey\": \"a-3jziyn-8b7qta9tia\", \"apiToken\": \"lfNIx-Gs8LUEbV@SqL\", \"http_host\": \"3jziyn.internetofthings.ibmcloud.com\", \"iotCredentialsIdentifier\": \"a2g6k39sl6r5\", \"mqtt_host\": \"3jziyn.messaging.internetofthings.ibmcloud.com\", \"mqtt_s_port\": 8883, \"mqtt_u_port\": 1883, \"org\": \"3jziyn\" }, \"instance_name\": \"wandernei-wsi-iotf-service\", \"label\": \"iotf-service\", \"name\": \"wandernei-wsi-iotf-service\", \"plan\": \"iotf-service-free\", \"provider\": null, \"syslog_drain_url\": null, \"tags\": [ \"internet_of_things\", \"Internet of Things\", \"ibm_created\", \"ibm_dedicated_public\", \"lite\" ], \"volume_mounts\": [] } ] }";
    private static String databaseName = "teste2";
    private CloudantClient client;
	//endregion

	private static void initClient() {
		if (cloudant == null) {
			synchronized (WeatherStationController.class) {
				if (cloudant != null) {
					return;
				}
				cloudant = createClient();
			}
		}
	}

	private static CloudantClient createClient() {
//		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		String VCAP_SERVICES1 = VCAP_SERVICES;

		String serviceName = null;

		if (VCAP_SERVICES1 != null) {
			// When running in Bluemix, the VCAP_SERVICES env var will have the credentials for all bound/connected services
			// Parse the VCAP JSON structure looking for cloudant.
			JsonObject obj = (JsonObject) new JsonParser().parse(VCAP_SERVICES1);
			Entry<String, JsonElement> dbEntry = null;
			Set<Entry<String, JsonElement>> entries = obj.entrySet();
			// Look for the VCAP key that holds the cloudant no sql db information
			for (Entry<String, JsonElement> eachEntry : entries) {
				if (eachEntry.getKey().toLowerCase().contains("cloudant")) {
					dbEntry = eachEntry;
					break;
				}
			}
			if (dbEntry == null) {
				throw new RuntimeException("Could not find cloudantNoSQLDB key in VCAP_SERVICES env variable");
			}

			obj = (JsonObject) ((JsonArray) dbEntry.getValue()).get(0);
			serviceName = (String) dbEntry.getKey();
			System.out.println("Service WeatherStation1 - " + serviceName);

			obj = (JsonObject) obj.get("credentials");

			user = obj.get("username").getAsString();
			password = obj.get("password").getAsString();

		} else {
			System.out.println("VCAP_SERVICES env var doesn't exist: running locally.");
		}

		try {
			System.out.println("Connecting to Cloudant : " + user);
			CloudantClient client = ClientBuilder.account(user)
					.username(user)
					.password(password)
					.build();
			return client;
		} catch (CouchDbException e) {
			throw new RuntimeException("Unable to connect to repository", e);
		}
	}

	public static Database getDB() {
		if (cloudant == null) {
			initClient();
		}

		if (db == null) {
			try {
				db = cloudant.database(databaseName, true);
			} catch (Exception e) {
				throw new RuntimeException("DB Not found", e);
			}
		}
		return db;
	}

	public WeatherStationController() throws IOException {
		// Use the IBM Cloudant library to create an IBM Cloudant client.
		this.client = createClient(); // same as >> System.out.println(getDB());
	}

	public WeatherStationDriver createStation(int stationId) throws IOException {
//		new WeatherStationController();
//      JsonObject ws1message = this.ws("22", "2018-10-13T00:43:53.807Z", "15", "0", "0");
		WeatherStationDriver ws1 = new WeatherStationDriver(stationId);
		System.out.println("resposta ws1: " + ws1.msg.getMessage());

		return ws1;
	}


	public WeatherStationDriver controlStation(int stationId, boolean irrigate, String configuration) throws IOException {
		WeatherStationDriver ws1 = createStation(stationId);
		//       JsonObject ws1message = this.ws("22", "2018-10-13T00:43:53.807Z", "15", "0", "0");
		ws1 = new WeatherStationDriver(stationId, irrigate, configuration);
		System.out.println("resposta ws1: " + ws1.msg.getMessage());

        JsonObject ws1message = ws1.ws.getWeatherStation();

        // Create an instance of the database
        Database teste2 = client.database(databaseName, true);
		teste2.post(ws1message);
        Response ws_saved = teste2.save(ws1message);
        teste2.ensureFullCommit();

        String ws_saved_id = ws_saved.getId();
        String ws_saved_rev = ws_saved.getRev();

        ws1message.addProperty("_id",ws_saved_id );
        ws1message.addProperty("_rev",ws_saved_rev );
        teste2.update(ws1message);


        AllDocsRequest allDocsRequest = getDB().getAllDocsRequestBuilder().includeDocs(true).build();
        List<JsonObject> listaTeste = getDB().getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(JsonObject.class);
//        for (JsonObject ws : listaTeste) {
//            System.out.println(ws.toString());
//        }

        ArrayList<String> keys =new ArrayList<>() ;
        int keysSize = keys.size();

        keys.add(ws_saved_id);
//        keys.add("2");

        List<Object> allDocsByKeys = getDB().getAllDocsRequestBuilder()
                .includeDocs(true)
                .keys(keys.toArray(new String[keysSize]))
                .build().getResponse().getDocsAs(Object.class);

        for (Object allDocsByKey : allDocsByKeys) {
            System.out.println(allDocsByKey.toString());
        }

		System.out.println(getDB().search(ws_saved_id));

		return ws1;
	}

	public WeatherStation getWs() throws IOException {
        WeatherStationDriver ws1 = new WeatherStationDriver();
        System.out.println("resposta ws1: " + ws1.msg.getMessage());

        return ws1.ws;
    }

	public void reconfigure(){
//        WeatherStationDriver ws1 = createStation(stationId);
//        //       JsonObject ws1message = this.ws("22", "2018-10-13T00:43:53.807Z", "15", "0", "0");
//        ws1 = new WeatherStationDriver(stationId, irrigate, configuration);
//        System.out.println("resposta ws1: " + ws1.msg.getMessage());
//
//        JsonObject ws1message = ws1.ws.getWeatherStation();
//
//        // Create an instance of the database
//        Database teste2 = client.database(databaseName, true);
//        teste2.post(ws1message);
//        Response ws_saved = teste2.save(ws1message);
//        teste2.ensureFullCommit();
//
//        String ws_saved_id = ws_saved.getId();
//        String ws_saved_rev = ws_saved.getRev();
//
//        ws1message.addProperty("_id",ws_saved_id );
//        ws1message.addProperty("_rev",ws_saved_rev );
//        teste2.update(ws1message);
//
//
//        AllDocsRequest allDocsRequest = getDB().getAllDocsRequestBuilder().includeDocs(true).build();
//        List<JsonObject> listaTeste = getDB().getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(JsonObject.class);
////        for (JsonObject ws : listaTeste) {
////            System.out.println(ws.toString());
////        }
//
//        ArrayList<String> keys =new ArrayList<>() ;
//        int keysSize = keys.size();
//
//        keys.add(ws_saved_id);
////        keys.add("2");
//
//        List<Object> allDocsByKeys = getDB().getAllDocsRequestBuilder()
//                .includeDocs(true)
//                .keys(keys.toArray(new String[keysSize]))
//                .build().getResponse().getDocsAs(Object.class);
//
//        for (Object allDocsByKey : allDocsByKeys) {
//            System.out.println(allDocsByKey.toString());
//        }
//
//        System.out.println(getDB().search(ws_saved_id));
//
//        return reconfigured;
	}

	public void showAllData() {

	}

	public boolean setDataSampleRate() {
		return false;
	}

	public boolean saveConfiguration(String setConfiguration) {
		return false;
	}

	public boolean enableDisableSensor() {
		return false;
	}

	public boolean sendDataWhenReady() {
		return false;
	}

//    public static void main(String[] args) throws IOException {
////        WeatherStationController one = new WeatherStationController();
//
////        one.createStation(11);
//
////        one.controlStation(11,true,"55");
//
//    }

}
