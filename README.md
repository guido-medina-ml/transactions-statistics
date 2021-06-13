# N26 Challenge - Transactions statistics

Restful API for N26 statistics. The main use case for the API is to calculate realtime statistic from the last 60 seconds. There will be two APIs, one of them is called every time a transaction is made. It is also the sole input of this rest API. The other one returns the statistic based of the transactions of the last 60 seconds.

##Requirements

- The API have to be threadsafe with concurrent requests
- The solution has to work without a database (this also applies to in-memory
  databases)
- Service must not store all transactions in memory for all time: Transactions not necessary for correct calculation must be discarded
- Unit tests are mandatory
- `mvn clean install` and `mvn clean integration-test` must complete successfully