package com.group3.petstore.Controller;

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
