package com.dummy.netbanking.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.openapitools.api.UserApi;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.AccountApiClient;
import org.openapitools.client.api.UserApiClient;
import org.openapitools.client.model.Account;
import org.openapitools.client.model.TimingMonitorData.EventCodeEnum;
import org.openapitools.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

import com.group3.monitorClient.MonitorClientInterface;
import com.group3.monitorClient.messenger.Messenger;

import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-05-05T13:58:29.883868300+02:00[Europe/Copenhagen]")
@Controller
@RequestMapping("${openapi.swaggerBankSystem.base-path:}")
public class UserApiController implements UserApi {

	private MonitorClientInterface monitorClientInterface = Messenger.getMonitorClientInterface();;
	private UserApiClient userApiClient;
	private AccountApiClient accountApiClient; 
    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UserApiController(NativeWebRequest request) {
    	monitorClientInterface = new MonitorClientInterface();
        this.request = request;
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://localhost:8082");
        userApiClient = new UserApiClient(apiClient);
        accountApiClient = new AccountApiClient(apiClient);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }
    /**
     * POST /User : Create new user
     *
     * @param user  (required)
     * @return Success (status code 201)
     *         or Error (status code 500)
     * @see UserApi#createNewUser
     */
    @Override
    public ResponseEntity<Void> createNewUser(@ApiParam(value = "" ,required=true )  @Valid @RequestBody User user) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/User", EventCodeEnum.RECEIVEREQUEST);
        
        ApiResponse<Void> apiResponse;
        ResponseEntity<Void> returnValue;
        
        try {
			apiResponse = userApiClient.createNewUserWithHttpInfo(convertUserType(user));
			returnValue = new ResponseEntity<Void>(HttpStatus.resolve(apiResponse.getStatusCode()));
		} catch (ApiException e) {
			returnValue = new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		
		monitorClientInterface.queueMonitorData(eventID, "/User", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

    /**
     * DELETE /User : Delete user
     *
     * @param user Delete user from the value of a query parameter (required)
     * @return Success (status code 200)
     *         or Bad request (status code 400)
     * @see UserApi#deleteUser
     */
    @Override
    public ResponseEntity<Void> deleteUser(@NotNull @ApiParam(value = "Delete user from the value of a query parameter", required = true) @Valid @RequestParam(value = "user", required = true) String user) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/User", EventCodeEnum.RECEIVEREQUEST);
        
        ApiResponse<Void> apiResponse;
        ResponseEntity<Void> returnValue;
        
        try {
        	List<Account> accounts = accountApiClient.getUsersAllAccount(user);
        	
        	for (int i = 0; i < accounts.size(); i++) {
        		accountApiClient.deleteAccount(accounts.get(i).getName());
        	}
        	
			apiResponse = userApiClient.deleteUserWithHttpInfo(user);
			returnValue = new ResponseEntity<Void>(HttpStatus.resolve(apiResponse.getStatusCode()));
		} catch (ApiException e) {
			returnValue = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		}
       
		monitorClientInterface.queueMonitorData(eventID, "/User", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

    /**
     * GET /User : get a user
     *
     * @param user Get a user from the value of the query parameter (required)
     * @return Success (status code 200)
     *         or Bad request (status code 400)
     * @see UserApi#getUser
     */
    @Override
    public ResponseEntity<User> getUser(@NotNull @ApiParam(value = "Get a user from the value of the query parameter", required = true) @Valid @RequestParam(value = "user", required = true) String user) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/User", EventCodeEnum.RECEIVEREQUEST);
        
        ApiResponse<org.openapitools.client.model.User> apiResponse;
        ResponseEntity<User> returnValue;
        
        
        try {
			apiResponse = userApiClient.getUserWithHttpInfo(user);
			returnValue = new ResponseEntity<User>(convertUserType(apiResponse.getData()),HttpStatus.resolve(apiResponse.getStatusCode()));
		} catch (ApiException e) {
			returnValue = new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		}
		
		monitorClientInterface.queueMonitorData(eventID, "/User", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }
    
    /**
     * Convert from {@link User} to {@link org.openapitools.client.model.User}
     * @param user the user that you want to convert
     * @return the converted user
     */
    private org.openapitools.client.model.User convertUserType(User user) {
    	org.openapitools.client.model.User clientUser = new org.openapitools.client.model.User();
    	clientUser.setName(user.getName());
    	clientUser.setPassword(user.getPassword());
    	
    	return clientUser;
    }
    
    private User convertUserType(org.openapitools.client.model.User user) {
    	User clientUser = new User();
    	clientUser.setName(user.getName());
    	clientUser.setPassword(user.getPassword());
    	
    	return clientUser;
    }
}
