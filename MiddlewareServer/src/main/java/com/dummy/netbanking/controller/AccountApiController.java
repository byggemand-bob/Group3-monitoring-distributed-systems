package com.dummy.netbanking.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.openapitools.api.AccountApi;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.AccountApiClient;
import org.openapitools.client.model.TimingMonitorData.EventCodeEnum;
import org.openapitools.model.Account;
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
public class AccountApiController implements AccountApi {

	private MonitorClientInterface monitorClientInterface = Messenger.getMonitorClientInterface();;
	private AccountApiClient accountApiClient;
    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountApiController(NativeWebRequest request) {
    	monitorClientInterface = new MonitorClientInterface();
        this.request = request;
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("localhost:8082");
        accountApiClient = new AccountApiClient(apiClient);
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
        
        ApiResponse<Void> apiResponse;
        ResponseEntity<Void> returnValue;
        
		try {
			apiResponse = accountApiClient.createAccountWithHttpInfo(convertAccountType(account));
			returnValue = new ResponseEntity<Void>(HttpStatus.resolve(apiResponse.getStatusCode()));
		} 
		catch (ApiException e) {
			returnValue = new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		
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

        ApiResponse<Void> apiResponse;
        ResponseEntity<Void> returnValue;
        
        try {
			apiResponse = accountApiClient.deleteAccountWithHttpInfo(account);
			returnValue = new ResponseEntity<Void>(HttpStatus.resolve(apiResponse.getStatusCode()));
		} catch (ApiException e) {
			returnValue = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		}
		
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
        
        ApiResponse<Double> apiResponse;
        ResponseEntity<Double> returnValue;
        
        try {
			apiResponse = accountApiClient.getAccountBalanceWithHttpInfo(account);
			returnValue = new ResponseEntity<Double>(HttpStatus.resolve(apiResponse.getStatusCode()));
		} catch (ApiException e) {
			returnValue = new ResponseEntity<Double>(HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		}
		
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
        
        ApiResponse<List<org.openapitools.client.model.Account>> apiResponse;
        ResponseEntity<List<Account>> returnValue;
        
        try {
			apiResponse = accountApiClient.getUsersAllAccountWithHttpInfo(user);
			returnValue = new ResponseEntity<List<Account>>(HttpStatus.resolve(apiResponse.getStatusCode()));
		} catch (ApiException e) {
			returnValue = new ResponseEntity<List<Account>>(HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		}
		
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
        
        ApiResponse<Void> apiResponse;
        ResponseEntity<Void> returnValue;
        
		try {
			double newBalance = accountApiClient.getAccountBalance(account) - balance;
			apiResponse = accountApiClient.updateAccountBalanceWithHttpInfo(account, newBalance);
			returnValue = new ResponseEntity<Void>(HttpStatus.resolve(apiResponse.getStatusCode()));
		} catch (ApiException e) {
			returnValue = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		}
		
		monitorClientInterface.queueMonitorData(eventID, "/Account/balance", EventCodeEnum.SENDRESPONSE);
		return returnValue;
    }
    
    /**
     * Convert from {@link Account} to {@link org.openapitools.client.model.Account}
     * @param account the account that you want to convert
     * @return the converted account
     */
    private org.openapitools.client.model.Account convertAccountType(Account account) {
    	org.openapitools.client.model.Account clientAccount = new org.openapitools.client.model.Account();
    	clientAccount.setBalance(account.getBalance());
    	clientAccount.setName(account.getName());
    	clientAccount.setOwner(account.getOwner());
    	
    	return clientAccount;
    }
}
