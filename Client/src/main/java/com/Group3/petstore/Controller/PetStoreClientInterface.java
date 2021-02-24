package com.Group3.petstore.Controller;

import com.Group3.MonitorClient.Controller.MonitorClientInterface;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PetApi;
import org.openapitools.client.model.Pet;

import java.util.List;
import java.util.regex.Pattern;

public class PetStoreClientInterface {
    ApiClient client;
    PetApi api;
    MonitorClientInterface MonitorClient = new MonitorClientInterface("http://85.191.161.150:8888");

    public PetStoreClientInterface(String ServerIP){
        if (Pattern.matches("^http://\\d+.\\d+.\\d+.\\d+:\\d+$", ServerIP)) {
            client = new ApiClient();
            client.setBasePath(ServerIP);
            api = new PetApi(client);
        } else if(Pattern.matches("^\\d+.\\d+.\\d+.\\d+:\\d+$", ServerIP)){
            client = new ApiClient();
            client.setBasePath("http://" + ServerIP);
            api = new PetApi(client);
        }
    }

    public List<Pet> GetAllPets() throws ApiException {
        MonitorClient.addMonitorData(1L, 1L, null);
        return api.getAllPets();
    }
}
