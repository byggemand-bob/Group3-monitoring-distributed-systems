package com.dummy.netbanking.controller;



import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.openapitools.api.AccountApi;
import org.openapitools.client.model.TimingMonitorData.EventCodeEnum;
import org.openapitools.model.Account;
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
public class AccountApiController implements AccountApi {

	private MonitorClientInterface monitorClientInterface = Messenger.getMonitorClientInterface();;

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountApiController(NativeWebRequest request) {
    	monitorClientInterface = new MonitorClientInterface();
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }
    /**
     * POST /Account : Create new account
     *
     * @param account  (required)
     * @return Success (status code 201)
     *         or Error (status code 500)
     * @see AccountApi#createAccount
     */
    @Override
    public ResponseEntity<Void> createAccount(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Account account) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/Account", EventCodeEnum.RECEIVEREQUEST);
        
        /* TODO Implement controller logic here!
         * 
         * Input a instance of Account: account
         * Make a CREATE SQL statement with the values from the input
         * 
         */
        
        ResponseEntity<Void> returnValue = AccountApi.super.createAccount(account);
		
		monitorClientInterface.queueMonitorData(eventID, "/Account", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

    /**
     * DELETE /Account : Delete account
     *
     * @param account Delete a account from the value of a query parameter (required)
     * @return Success (status code 200)
     *         or Bad request (status code 400)
     * @see AccountApi#deleteAccount
     */
    @Override
    public ResponseEntity<Void> deleteAccount(@NotNull @ApiParam(value = "Delete a account from the value of a query parameter", required = true) @Valid @RequestParam(value = "account", required = true) String account) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/Account", EventCodeEnum.RECEIVEREQUEST);

        
        /* TODO Implement controller logic here!
         * 
         * Input: String: the accounts name
         * Make a DELETE SQL statement where name in DB is the name as the input
         * Throw error if the account can't be found?
         * 
         */
        
        ResponseEntity<Void> returnValue = AccountApi.super.deleteAccount(account);
		
		monitorClientInterface.queueMonitorData(eventID, "/Account", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

    /**
     * GET /Account/balance : Get the balance of a acccount
     *
     * @param account Get the balance of a account (required)
     * @return Success (status code 200)
     *         or Bad request (status code 400)
     * @see AccountApi#getAccountBalance
     */
    @Override
    public ResponseEntity<Double> getAccountBalance(@NotNull @ApiParam(value = "Get the balance of a account", required = true) @Valid @RequestParam(value = "account", required = true) String account) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/Account/balance", EventCodeEnum.RECEIVEREQUEST);
        
        /* TODO Implement controller logic here!
         * 
         * Input: String: the accounts name
         * Make a SELECT SQL statement where Account is the same as input
         * Throw error if the account can't be found?
         * Get the balance of the found account
         * Return balance
         * 
         */
        
        ResponseEntity<Double> returnValue = AccountApi.super.getAccountBalance(account);
		
		monitorClientInterface.queueMonitorData(eventID, "/Account/balance", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

    /**
     * GET /Account : Get all account assigned to a user
     *
     * @param user Get all the account that has been assigned for the user (required)
     * @return Success (status code 200)
     *         or Bad request (status code 400)
     * @see AccountApi#getUsersAllAccount
     */
    @Override
    public ResponseEntity<List<Account>> getUsersAllAccount(@NotNull @ApiParam(value = "Get all the account that has been assigned for the user", required = true) @Valid @RequestParam(value = "user", required = true) String user) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/Account", EventCodeEnum.RECEIVEREQUEST);
        
        /* TODO Implement controller logic here!
         * 
         * Input String: the name of a user
         * Make a SELECT SQL statement where user is the same as account owner
         * throw a error is none is found?
         * return the account(s)
         * 
         */
        
        ResponseEntity<List<Account>> returnValue = AccountApi.super.getUsersAllAccount(user);
		
		monitorClientInterface.queueMonitorData(eventID, "/Account", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

    /**
     * PUT /Account/balance : Update the balance of a acccount
     *
     * @param account Update the balance of a account (required)
     * @param balance Change the balance (required)
     * @return Success (status code 200)
     *         or Bad request (status code 400)
     * @see AccountApi#updateAccountBalance
     */
    @Override
    public ResponseEntity<Void> updateAccountBalance(@NotNull @ApiParam(value = "Update the balance of a account", required = true) @Valid @RequestParam(value = "account", required = true) String account,@NotNull @ApiParam(value = "Change the balance", required = true) @Valid @RequestParam(value = "balance", required = true) Double balance) {
        final long eventID = monitorClientInterface.getNextEventID();
        monitorClientInterface.queueMonitorData(eventID, "/Account/balance", EventCodeEnum.RECEIVEREQUEST);
        
        /* TODO Implement controller logic here!
         * 
         * Input: String: account name, Double: balance
         * Make a SELECT SQL statement to find the account that matches the account name
         * Throw error is none is found?
         * If a account is found calculate the new balance
         * Make a UPDATE SQL statement for the account with the new balance (can go in negative)
         * 
         */
        
        ResponseEntity<Void> returnValue = AccountApi.super.updateAccountBalance(account,balance);
		
		monitorClientInterface.queueMonitorData(eventID, "/Account/balance", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }

}
