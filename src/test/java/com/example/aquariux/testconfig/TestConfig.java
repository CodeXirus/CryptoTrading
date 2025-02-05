package com.example.aquariux.testconfig;

import com.example.aquariux.core.models.entities.Asset;
import com.example.aquariux.core.models.entities.Market;
import com.example.aquariux.core.models.markets.MarketType;
import com.example.aquariux.core.repositories.AssetRepository;
import com.example.aquariux.core.repositories.MarketRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

import static org.mockito.Mockito.when;

@TestConfiguration
public class TestConfig {
    @Bean
    public AssetRepository assetRepository() {
        AssetRepository mock = Mockito.mock(AssetRepository.class);
        when(mock.findBySymbol("BTC")).thenReturn(generateAsset("Bitcoin"));
        when(mock.findBySymbol("ETH")).thenReturn(generateAsset("Ethereum"));
        when(mock.findBySymbol("USDT")).thenReturn(generateAsset("Tether"));
        return mock;
    }

    @Bean
    public MarketRepository marketRepository() {
        MarketRepository mock = Mockito.mock(MarketRepository.class);
        when(mock.findBySymbol("BTCUSDT")).thenReturn(Optional.of(generateMarket("BTCUSDT")));
        when(mock.findBySymbol("ETHUSDT")).thenReturn(Optional.of(generateMarket("ETHUSDT")));
        return mock;
    }

    private static Asset generateAsset(String assetName) {
        Asset asset = new Asset();
        asset.setName(assetName);
        switch (assetName) {
            case "Bitcoin" -> {
                asset.setSymbol("BTC");
                asset.setAssetId(1L);
            }
            case "Ethereum" -> {
                asset.setSymbol("ETH");
                asset.setAssetId(2L);
            }
            case "Tether" -> {
                asset.setSymbol("USDT");
                asset.setAssetId(3L);
            }
        }
        return asset;
    }

    private static Market generateMarket(String symbol) {
        Market market = new Market();
        market.setSymbol(symbol);
        market.setMarketType(MarketType.SPOT);
        switch (symbol) {
            case "BTCUSDT" -> {
                market.setBaseAssetId(1L);
                market.setQuoteAssetId(3L);
            }
            case "ETHUSDT" -> {
                market.setBaseAssetId(2L);
                market.setQuoteAssetId(3L);
            }
        }
        return market;
    }
}
