# CryptoTrading
 Aquariux Technical Interview

### Environment
- Java 17
- SpringBoot version 3.4.2
- Gradle

### Start up
Run on command line:
```
./gradlew Bootrun
```
It will take a few seconds for the system to start up, populate the database, and
start polling for the Market Tick.

These are the data that will be populated into the database upon start up.

Asset
```
BTC, Bitcoin
ETH, Ethereum
USDT, Tether
```
Market
```
BTCUSDT
ETHUSDT
```
Account
```
accountUserId: 1
Name: Bernard Chng
Email: abc@gmail.com
Wallet: 50,000 USDT
```

### System Design
Ideally this crypto trading platform should be built with Microservice architecture.
Each microservice will have their own responsibilities and processes, and will communicate
with the TradingCore via an event/message bus like RabbitMQ or Kafka to send or receive updates.
For example the Scheduler Service will fetch the price and market data from 3rd party source and send it to the TradingCore
for it to construct the OrderBook.

The Market Service will request the OrderBook, Tickers & Candles data from the TradingCore,
and users will be able to fetch the latest market data from the Market service.
![microservice_system_design.jpeg](src%2Fmain%2Fresources%2Fdesign%2Fmicroservice_system_design.jpeg)

However, a monolithic design will more sense here due to the time constraint and the limited capabilities
of the TradingCore for this Test scenario.
![system_design.jpeg](src%2Fmain%2Fresources%2Fdesign%2Fsystem_design.jpeg)

### List of endpoints
Endpoints that require RequestHeader - ```{USER_ACCOUNT_ID:String}``` will be denoted with (*).

Usually we will derive the user account id from the SessionData or JWT Token.
However, we are assuming that the user is already authenticated, hence I am
using this header (USER_ACCOUNT_ID) as a replacement.

#### Market ticker endpoints

```
GET - /api/markets/tick
GET - /api/markets/tick/{symbol}
GET - /api/markets/tick/{symbol}/price
```

#### Order endpoints
```
*POST - /api/orders
*GET - /api/orders
*GET - /api/orders/{orderId}
```

#### Trade endpoints
```
*GET - /api/trades?symbol=BTCUSDT
*GET - /api/trades/{tradeId}
*GET - /api/trades/all
```

#### Wallet endpoints
```
*GET - /api/wallet
*GET - /api/wallet/{symbol}
```
