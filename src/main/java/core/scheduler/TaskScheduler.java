package core.scheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TaskScheduler {
    private static final String BINANCE_MARKET_TICKER_URL = "https://api.binance.com/api/v3/ticker/bookTicker";
    private static final String HUOBI_MARKET_TICKER_URL = "https://api.huobi.pro/market/tickers";

    @Scheduled(fixedRate = 10000)
    public void pollMarketTicks() {

    }
}
