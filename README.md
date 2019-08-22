### Requirements
Design and implement a RESTful API (including data model and the backing implementation) for money transfers between accounts.

1. You can use Java or Kotlin.

2. Keep it simple and to the point (e.g. no need to implement any authentication).

3. Assume the API is invoked by multiple systems and services on behalf of end users.

4. You can use frameworks/libraries if you like (except Spring), but don't forget about requirement #2 and keep it simple and avoid heavy frameworks.

5. The datastore should run in-memory for the sake of this test.

6. The final result should be executable as a standalone program (should not require a pre-installed container/server).

7. Demonstrate with tests that the API works as expected.

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
* JaCoCo
* [vmlens](http://vmlens.com/) - multithreaded testing


### Run the Application

To run the application, using the terminal, navigate to the project directory and execute: 
```
make run
```
This will test and package the application and will run the executable jar

### APIs

#### Transfer Amount
##### Sample Request

    $ curl 'http://localhost:38000/accounts/78901234567890/transfer-amount' -i -X POST \
        -H 'Content-Type: application/json' \
        -d '{
      	"amountToDebitValue": 100,
      	"amountToDebitCurrency": "USD",
      	"beneficiaryAccountNumber": "12340987654321",
      	"amountToCreditValue": 80,
      	"amountToCreditCurrency": "GBP"
    }' 
##### Path Parameters 
    /accounts/{payeeAccountNumber}/transfer-amount
##### Request Body
    {
        "amountToDebitValue": 100,
        "amountToDebitCurrency": "USD",
        "beneficiaryAccountNumber": "12340987654321",
        "amountToCreditValue": 80,
        "amountToCreditCurrency": "GBP"
    }
    
##### Example Response
    HTTP/1.1 200 OK
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
##### Sample Request
    $ curl 'http://localhost:38000/accounts/78901234567890' -i -X GET \
        -H 'Content-Type: application/json'

##### Path Parameters 
    /accounts/{accountNumber}/transfer-amount

##### Example Response
    HTTP/1.1 200 OK
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


### Test Data
Preloaded data to play around with

    Account.of(AccountNumber.of("12340987654321"), Currency.of("GBP"), BigDecimal.valueOf(5000))
    Account.of(AccountNumber.of("09876543214321"), Currency.of("GBP"), BigDecimal.valueOf(5000))
    Account.of(AccountNumber.of("78901234567890"), Currency.of("USD"), BigDecimal.valueOf(5000))
    
Go to `money-transfer-service/app/src/main/java/com/github/tddiaz/moneytransferservice/app/DataBootstrap.java` class to add/modify the preloaded data 


### Code Coverage

**JaCoCo** is the code coverage tool used to generate coverage report.

Code Coverage ratio is set to **90** percent

     <configuration>
        <rules>
            <rule>
                <element>PACKAGE</element>
                <limits>
                    <limit>
                        <counter>LINE</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.9</minimum>
                    </limit>
                </limits>
            </rule>
        </rules>
    </configuration>

To generate the report:
```
make test-report
```
And open the generated reports html file to a browser
```
app/target/test-report/index.html
```
```
domain/target/test-report/index.html
```

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
