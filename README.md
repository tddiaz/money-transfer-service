### Assumptions
* Account info is being consumed from events coming from other services; 
specific fields will be saved from the consumed event. (ex. Account Number, Balance, Currency)
* The given amount that will be received by the beneficiary account is already converted base on the its currency.
* The given amount that will be deducted from payee's account is already calculated, and is equivalent to the converted
amount that the beneficiary account will be receiving.

### Technologies used
* [Micronaut](https://micronaut.io/) - Micronaut is a lightweight JVM framework for building cloud native applications using Java, Groovy or Kotlin.
              
  It provides (among many other things) all of the following:
      
  - An efficient compile-time dependency-injection container
  - A reactive HTTP server & client based on Netty
  - A suite of cloud-native features to boost developer productivity when building microservice systems
* Java 11
* Maven 
* Mockito
* AssertJ
* RestAssured
* JUnit5


### Run the Application

To run the application, using the terminal, navigate to the project directory and execute: 
```
make run
```
This will test and package the application and will run the executable jar

### APIs

#### Transfer Amount
API specs for transferring amount between accounts

##### Sample Request

    $ curl 'http://localhost:38000/accounts/78901234567890/transfer-amount' -i -X POST \
        -H 'Content-Type: application/json' \
        -d '{
      	"amountToDebitValue": "100",
      	"amountToDebitCurrency": "USD",
      	"beneficiaryAccountNumber": "12340987654321",
      	"amountToCreditValue": "80",
      	"amountToCreditCurrency": "GBP"
    }' 

##### Path Parameters 
    /accounts/{payeeAccountNumber}/transfer-amount
| Parameter             | Description             |
| :---                  | :---                    |
| `payeeAccountNumber`  | Payee's account number  |

##### Request Body
    {
        "amountToDebitValue": "200",
        "amountToDebitCurrency": "USD",
        "beneficiaryAccountNumber": "12340987654321",
        "amountToCreditValue": "200",
        "amountToCreditCurrency": "GBP"
    }
    
##### Example Response
    HTTP/1.1 201 Created
    Content-Length: 478
    Content-Type: application/json;charset=UTF-8
    
    {
        "accountNumber": "78901234567890",
        "currency": "USD",
        "balance": 4980,
        "transactions": [
            {
                "amount": 80,
                "payeeAccountNumber": "12340987654321",
                "transactionType": "CREDIT",
                "balance": 4980,
                "date": "2019-08-22"
            },
            {
                "amount": 100,
                "beneficiaryAccountNumber": "12340987654321",
                "transactionType": "DEBIT",
                "balance": 4900,
                "date": "2019-08-22"
            }
        ]
    }

#### Get Account
    
### Test Snippets

#### AccountResourceTest

    @Test
    void givenValidRequestPayload_whenPostingTransferAmount_shouldReturnSuccessResponse() {

        when(accountService.transferAmount(any(TransferAmount.class))).thenReturn(accountDto());

        var payload = TestData.validTransferAmountCommand();

        given()
            .port(38000)
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post(BASE_URL + "/transfer-amount", payload.getPayeeAccountNumber())
        .then()
            .statusCode(OK.getCode())
            .body("accountNumber", is("12345678901234"))
            .body("balance", is(1000))
            .body("currency", is("GBP"))
            .body("transactions", hasSize(1))
            .body("transactions[0].amount", is(100))
            .body("transactions[0].payeeAccountNumber", is("89076543126788"))
            .body("transactions[0].transactionType", is("CREDIT"))
            .body("transactions[0].balance", is(1000))
            .body("transactions[0].date", notNullValue());

        verify(accountService).transferAmount(eq(payload));
    }
