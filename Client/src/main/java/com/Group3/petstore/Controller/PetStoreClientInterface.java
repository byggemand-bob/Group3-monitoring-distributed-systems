package com.Group3.petstore.Controller;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PetApi;
import org.openapitools.client.model.Pet;

import com.Group3.monitor.client.MonitorClientInterface;

import java.util.List;
import java.util.regex.Pattern;

public class PetStoreClientInterface {
//    ApiClient client;
//    PetApi api;
//    MonitorClientInterface MonitorClient = new MonitorClientInterface();
//
//    public PetStoreClientInterface(String ServerIP){
//        client = new ApiClient();
//        SetServerIP(ServerIP);
//        api = new PetApi(client);
//    }
//
//    public void SetServerIP(String ServerIP){
//        if (Pattern.matches("^http://\\d+.\\d+.\\d+.\\d+:\\d+$", ServerIP)) {
//            client.setBasePath(ServerIP);
//        } else if(Pattern.matches("^\\d+.\\d+.\\d+.\\d+:\\d+$", ServerIP)){
//            client.setBasePath("http://" + ServerIP);
//        } else if(Pattern.matches("^http://localhost:\\d+$", ServerIP)){
//            client.setBasePath(ServerIP);
//        } else if(Pattern.matches("^localhost:\\d+$", ServerIP)){
//            client.setBasePath("http://" + ServerIP);
//        }
//    }
//
//    public List<Pet> GetAllPets() throws ApiException {
//        MonitorClient.addMonitorData(1L, 1L, null);
//        return api.getAllPets();
//    }
}
