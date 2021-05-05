package com.dummy.netbanking.controller;



import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.openapitools.api.UserApi;
import org.openapitools.client.model.TimingMonitorData.EventCodeEnum;
import org.openapitools.model.User;
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

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UserApiController(NativeWebRequest request) {
    	monitorClientInterface = new MonitorClientInterface();
        this.request = request;
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
        
        /* TODO Implement controller logic here!
         * 
         * Input: a instance of User: user
         * Make a CREATE SQL statement with the values from user
         * 
         */
        
        ResponseEntity<Void> returnValue = UserApi.super.createNewUser(user);
		
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
        
        /* TODO Implement controller logic here!
         * 
         * Input: String: the name of a user
         * Make a DELETE SQL statement where name in DB is the name as the input
         * Throw error if a user can't be found?
         * 
         */
        
        ResponseEntity<Void> returnValue = UserApi.super.deleteUser(user);
		
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
        
        /* TODO Implement controller logic here!
         * 
         * Input: String: the name of a user
         * Make a SELECT SQL statement where name in DB is the same as input
         * Throw error if a user can't be found?
         * return the user 
         */
        
        ResponseEntity<User> returnValue = UserApi.super.getUser(user);
		
		monitorClientInterface.queueMonitorData(eventID, "/User", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

}
