# Entering Point

I´ve created a "post" rest endpoint to handle the money transfer process between accounts
There is a simple DTO Object with information we need to receive to handle this operation

# Basic DTO Validation

User @Validation Process with @BindingResults functionality to only receive valid DTO´s
I´ve also taken as assumption that the min value for any transaction is 1

# Service Layer
The Service Layer receives the DTO from the Controller and do some additional validations like:
- If the "from" account is different from "to" account
- If both accounts exists
- If from account has enough money to perform this action
I´ve created a class to perform this validations.
- 
If everything is OK first we call 

# DB Layer
To update this information in database
We used some Java Atomic Methods to avoid concurrency problems

# Notification Process
Here we are going to send a msg to both accounts saying what happened.

# Tests
I´ve also created some unit tests to check this process
I´ve created a common Examples class to help me during this process. Please take a look.
I´ve mocked Notification Process during test as requested

# Improvements
Implement a Real DB
Implement more tests (units, integrate and acceptance)
Implement am OpenApi like Swagger to document our Api's
Implement Email Notification Services

