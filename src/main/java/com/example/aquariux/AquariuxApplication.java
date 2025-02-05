package com.example.aquariux;

import com.example.aquariux.core.models.entities.*;
import com.example.aquariux.core.models.markets.MarketType;
import com.example.aquariux.core.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class AquariuxApplication {

	public static void main(String[] args) {
		SpringApplication.run(AquariuxApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(AssetRepository assetRepository,
								   MarketRepository marketRepository,
								   UserAccountRepository userAccountRepository,
								   WalletRepository walletRepository,
								   AssetAccountRepository assetAccountRepository) {
		return args -> {
			List<Asset> assetList = generateTestAssets();
			for (Asset asset : assetList) {
				assetRepository.save(asset);
			}

			List<Market> marketList = generateTestMarkets(assetRepository);
			for (Market market : marketList) {
				marketRepository.save(market);
			}
			Asset usdt = assetRepository.findBySymbol("USDT");
			UserAccount user = new UserAccount();
			user.setName("Bernard Chng");
			user.setEmail("abc@gmail.com");
			UserAccount successfulUser = userAccountRepository.save(user);

			Wallet wallet = new Wallet();
			wallet.setUserAccountId(successfulUser.getUserId());
			wallet.setUserAccount(user);
			walletRepository.save(wallet);

			AssetAccount usdtAccount = new AssetAccount();
			usdtAccount.setQuantity(50000);
			usdtAccount.setAssetId(usdt.getAssetId());
			usdtAccount.setUserAccountId(successfulUser.getUserId());
			usdtAccount.setWallet(wallet);
			assetAccountRepository.save(usdtAccount);
		};
	}

	private static List<Asset> generateTestAssets() {
		List<Asset> assetList = new ArrayList<>();
		assetList.add(generateAsset("Bitcoin"));
		assetList.add(generateAsset("Ethereum"));
		assetList.add(generateAsset("Tether"));
		return assetList;
	}

	private static Asset generateAsset(String assetName) {
		Asset asset = new Asset();
		asset.setName(assetName);
		switch (assetName) {
			case "Bitcoin" -> {
				asset.setSymbol("BTC");
			}
			case "Ethereum" -> {
				asset.setSymbol("ETH");
			}
			case "Tether" -> {
				asset.setSymbol("USDT");
			}
		}
		return asset;
	}

	private static List<Market> generateTestMarkets(AssetRepository assetRepository) {
		List<Market> marketList = new ArrayList<>();
		marketList.add(generateMarket("BTCUSDT", assetRepository));
		marketList.add(generateMarket("ETHUSDT", assetRepository));
		return marketList;
	}

	private static Market generateMarket(String symbol, AssetRepository assetRepository) {
		Market market = new Market();
		market.setSymbol(symbol);
		market.setMarketType(MarketType.SPOT);
		switch (symbol) {
			case "BTCUSDT" -> {
				Asset baseAsset = assetRepository.findBySymbol("BTC");
				Asset quoteAsset = assetRepository.findBySymbol("USDT");
				market.setBaseAssetId(baseAsset.getAssetId());
				market.setQuoteAssetId(quoteAsset.getAssetId());
			}
			case "ETHUSDT" -> {
				Asset baseAsset = assetRepository.findBySymbol("ETH");
				Asset quoteAsset = assetRepository.findBySymbol("USDT");
				market.setBaseAssetId(baseAsset.getAssetId());
				market.setQuoteAssetId(quoteAsset.getAssetId());
			}
		}
		return market;
	}
}
