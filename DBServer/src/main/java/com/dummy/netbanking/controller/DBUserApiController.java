package com.dummy.netbanking.controller;



import com.group3.monitorClient.messenger.Messenger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.List;
import org.openapitools.model.User;

import com.dummy.netbanking.database.UserDBManager;
import com.group3.monitorClient.MonitorClientInterface;

import org.openapitools.api.DBUserApi;
import org.openapitools.client.model.TimingMonitorData.EventCodeEnum;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-05-05T14:12:04.678716400+02:00[Europe/Copenhagen]")
@Controller
@RequestMapping("${openapi.swaggerBankSystem.base-path:}")
public class DBUserApiController implements DBUserApi {

	private MonitorClientInterface monitorClientInterface = Messenger.getMonitorClientInterface();;
	private final UserDBManager manager;

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public DBUserApiController(NativeWebRequest request) {
    	monitorClientInterface = new MonitorClientInterface();
    	this.manager = new UserDBManager();
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }
    /**
     * POST /DBUser : Create new user
     *
     * @param user  (required)
     * @return Success (status code 201)
     *         or Error (status code 500)
     * @see DBUserApi#createNewUser
     */
    @Override
    public ResponseEntity<Void> createNewUser(@ApiParam(value = "" ,required=true )  @Valid @RequestBody User user) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/DBUser", EventCodeEnum.RECEIVEREQUEST);
        
        // Implement controller logic here!
        ResponseEntity<Void> returnValue;
        try {
			manager.insertUser(user);
			returnValue = new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (SQLException e) {
			System.err.println("Encountered an error while trying to insert user <" + user + "> in the DB, error message was <" + e.getMessage() + ">");
			returnValue = new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		monitorClientInterface.queueMonitorData(eventID, "/DBUser", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

    /**
     * DELETE /DBUser : Delete user
     *
     * @param user Delete user from the value of a query parameter (required)
     * @return Success (status code 200)
     *         or Bad request (status code 400)
     * @see DBUserApi#deleteUser
     */
    @Override
    public ResponseEntity<Void> deleteUser(@NotNull @ApiParam(value = "Delete user from the value of a query parameter", required = true) @Valid @RequestParam(value = "user", required = true) String user) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/DBUser", EventCodeEnum.RECEIVEREQUEST);
        
        // Implement controller logic here!
        ResponseEntity<Void> returnValue;
        boolean success = false;
        try {
        	success = manager.deleteUser(user);
		} catch (SQLException e) {
			System.err.println("Encountered an error while trying to delete user <" + user + "> in the DB, error message was <" + e.getMessage() + ">");
		}
        
        if (success) {
        	returnValue = new ResponseEntity<Void>(HttpStatus.OK);
        } else {
        	returnValue = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
		
		monitorClientInterface.queueMonitorData(eventID, "/DBUser", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

    /**
     * GET /DBUser : get a user
     *
     * @param user Get a user from the value of the query parameter (required)
     * @return Success (status code 200)
     *         or Bad request (status code 400)
     * @see DBUserApi#getUser
     */
    @Override
    public ResponseEntity<User> getUser(@NotNull @ApiParam(value = "Get a user from the value of the query parameter", required = true) @Valid @RequestParam(value = "user", required = true) String user) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/DBUser", EventCodeEnum.RECEIVEREQUEST);
        
        // Implement controller logic here!
        ResponseEntity<User> returnValue;
        try {
			User retUser = manager.selectUser(user);
			returnValue = new ResponseEntity<User>(retUser, HttpStatus.OK);
		} catch (SQLException e) {
			System.err.println("Encountered an error while trying to select user <" + user + "> in the DB, error message was <" + e.getMessage() + ">");
			returnValue = new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}
		
		monitorClientInterface.queueMonitorData(eventID, "/DBUser", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

}
