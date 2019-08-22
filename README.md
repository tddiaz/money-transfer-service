### Assumptions
* Account info is being consumed from events (ex. Account Enrolled Event) published by a account enrollment service; 
specific fields will be saved from the consumed event. (ex. Account Number, Balance, Currency)
* The given amount that will be received by the beneficiary account is already converted base on the its currency.
* The given amount that will be deducted from payee's account is already calculated, and is equivalent to the converted
amount that the beneficiary account will be receiving.
* For simplicity, accounts are assumed to be on the same bank and will be updated on a single transaction
### Technologies used
* Java 11
* Micronaut 
* Mockito
* JUnit5
