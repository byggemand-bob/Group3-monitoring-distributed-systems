package com.dummy.netbanking.controller;



import com.group3.monitorClient.messenger.Messenger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.model.Account;

import com.dummy.netbanking.database.AccountDBManager;
import com.group3.monitorClient.MonitorClientInterface;

import org.openapitools.api.DBAccountApi;
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
public class DBAccountApiController implements DBAccountApi {

	private MonitorClientInterface monitorClientInterface = Messenger.getMonitorClientInterface();
	private final AccountDBManager manager;

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public DBAccountApiController(NativeWebRequest request) {
    	monitorClientInterface = new MonitorClientInterface();
    	manager = new AccountDBManager();
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }
    /**
     * POST /DBAccount : Create new account
     *
     * @param account  (required)
     * @return Success (status code 201)
     *         or Error (status code 500)
     * @see DBAccountApi#createAccount
     */
    @Override
    public ResponseEntity<Void> createAccount(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Account account) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/DBAccount", EventCodeEnum.RECEIVEREQUEST);
        
        // Implement controller logic here!
        ResponseEntity<Void> returnValue;
        try {
			manager.insertAccount(account);
			returnValue = new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (SQLException e) {
			System.err.println("Encountered an error while trying to insert account <" + account + "> in the DB, error message was <" + e.getMessage() + ">");
			returnValue = new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		monitorClientInterface.queueMonitorData(eventID, "/DBAccount", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

    /**
     * DELETE /DBAccount : Delete account
     *
     * @param account Delete a account from the value of a query parameter (required)
     * @return Success (status code 200)
     *         or Bad request (status code 400)
     * @see DBAccountApi#deleteAccount
     */
    @Override
    public ResponseEntity<Void> deleteAccount(@NotNull @ApiParam(value = "Delete a account from the value of a query parameter", required = true) @Valid @RequestParam(value = "account", required = true) String account) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/DBAccount", EventCodeEnum.RECEIVEREQUEST);
        
        // Implement controller logic here!
        ResponseEntity<Void> returnValue;
        boolean success = false;
        try {
			success = manager.deleteAccount(account);
		} catch (SQLException e) {
			System.err.println("Encountered an error while trying to delete account <" + account + "> in the DB, error message was <" + e.getMessage() + ">");
		}
        
        if (success) {
        	returnValue = new ResponseEntity<Void>(HttpStatus.OK);
        } else {
        	returnValue = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
		
		monitorClientInterface.queueMonitorData(eventID, "/DBAccount", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

    /**
     * GET /DBAccount/balance : Get the balance of a acccount
     *
     * @param account Get the balance of a account (required)
     * @return Success (status code 200)
     *         or Bad request (status code 400)
     * @see DBAccountApi#getAccountBalance
     */
    @Override
    public ResponseEntity<Double> getAccountBalance(@NotNull @ApiParam(value = "Get the balance of a account", required = true) @Valid @RequestParam(value = "account", required = true) String account) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/DBAccount/balance", EventCodeEnum.RECEIVEREQUEST);
        
        // Implement controller logic here!
        ResponseEntity<Double> returnValue;
        try {
			final Double ret = manager.selectBalance(account);
			if (ret != null) {
				returnValue = new ResponseEntity<Double>(ret, HttpStatus.OK);
			} else {
				returnValue = new ResponseEntity<Double>(HttpStatus.BAD_REQUEST);
			}
		} catch (SQLException e) {
			System.err.println("Encountered an error while trying to select the balance from account <" + account + "> in the DB, error message was <" + e.getMessage() + ">");
			returnValue = new ResponseEntity<Double>(HttpStatus.BAD_REQUEST);
		}
		
		monitorClientInterface.queueMonitorData(eventID, "/DBAccount/balance", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

    /**
     * GET /DBAccount : Get all account assigned to a user
     *
     * @param user Get all the account that has been assigned for the user (required)
     * @return Success (status code 200)
     *         or Bad request (status code 400)
     * @see DBAccountApi#getUsersAllAccount
     */
    @Override
    public ResponseEntity<List<Account>> getUsersAllAccount(@NotNull @ApiParam(value = "Get all the account that has been assigned for the user", required = true) @Valid @RequestParam(value = "user", required = true) String user) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/DBAccount", EventCodeEnum.RECEIVEREQUEST);
        
        // Implement controller logic here!
        ResponseEntity<List<Account>> returnValue;
        try {
			Account[] ret = manager.selectAccountsForUser(user);
			returnValue = new ResponseEntity<List<Account>>(Arrays.asList(ret), HttpStatus.OK);
		} catch (SQLException e) {
			System.err.println("Encountered an error while trying to select all accounts for user <" + user + "> in the DB, error message was <" + e.getMessage() + ">");
			returnValue = new ResponseEntity<List<Account>>(HttpStatus.BAD_REQUEST);
		}
        
		
		monitorClientInterface.queueMonitorData(eventID, "/DBAccount", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

    /**
     * PUT /DBAccount/balance : Update the balance of a acccount
     *
     * @param account Update the balance of a account (required)
     * @param balance Change the balance (required)
     * @return Success (status code 200)
     *         or Bad request (status code 400)
     * @see DBAccountApi#updateAccountBalance
     */
    @Override
    public ResponseEntity<Void> updateAccountBalance(@NotNull @ApiParam(value = "Update the balance of a account", required = true) @Valid @RequestParam(value = "account", required = true) String account,@NotNull @ApiParam(value = "Change the balance", required = true) @Valid @RequestParam(value = "balance", required = true) Double balance) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/DBAccount/balance", EventCodeEnum.RECEIVEREQUEST);
        
        // Implement controller logic here!
        ResponseEntity<Void> returnValue;
        boolean success = false;
        try {
			success = manager.updateBalance(balance, account);
		} catch (SQLException e) {
			System.err.println("Encountered an error while trying to insert account <" + account + "> in the DB, error message was <" + e.getMessage() + ">");
		}
        
        if (success) {
        	returnValue = new ResponseEntity<Void>(HttpStatus.OK);
        } else {
        	returnValue = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
		
		monitorClientInterface.queueMonitorData(eventID, "/DBAccount/balance", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

}
