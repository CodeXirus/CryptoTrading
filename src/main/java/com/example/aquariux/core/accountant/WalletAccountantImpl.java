package com.example.aquariux.core.accountant;

import com.example.aquariux.core.models.entities.AssetAccount;
import com.example.aquariux.core.models.entities.Order;
import com.example.aquariux.core.models.entities.Trade;
import com.example.aquariux.core.models.entities.Wallet;
import com.example.aquariux.core.models.markets.MarketTick;
import com.example.aquariux.core.repositories.AssetAccountRepository;
import com.example.aquariux.core.repositories.WalletRepository;
import com.example.aquariux.exception.InvalidRequestException;
import com.example.aquariux.order.models.OrderSide;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletAccountantImpl implements WalletAccountant {
    private final AssetAccountRepository assetAccountRepository;
    private final WalletRepository walletRepository;

    public WalletAccountantImpl(AssetAccountRepository assetAccountRepository,
                                WalletRepository walletRepository) {
        this.assetAccountRepository = assetAccountRepository;
        this.walletRepository = walletRepository;
    }

    /*
        Check wallet balance based on order side (BUY/SELL)
        For Buy order, check quote asset account.
        For Sell order, check base asset account.
     */
    @Override
    public void checkWalletBalance(Order order, MarketTick marketTick) {
        long userAccountId = order.getUserAccountId();
        Optional<AssetAccount> assetAccount;
        if (order.getOrderSide() == OrderSide.BUY) {
            assetAccount = assetAccountRepository.findByUserAccountIdAndAssetId(userAccountId, marketTick.getQuoteAssetId());
            checkAssetBalanceForBuyOrder(assetAccount, marketTick, order);
        } else {
            assetAccount = assetAccountRepository.findByUserAccountIdAndAssetId(userAccountId, marketTick.getBaseAssetId());
            checkAssetBalanceForSellOrder(assetAccount, order);
        }
    }

    private void checkAssetBalanceForBuyOrder(Optional<AssetAccount> assetAccount, MarketTick marketTick, Order order) {
        double totalOrderPrice = marketTick.getAskPrice() * order.getQuantity();
        if (assetAccount.isEmpty() || assetAccount.get().getQuantity() < totalOrderPrice) {
            throw new InvalidRequestException("Insufficient fund in wallet.");
        }
    }

    private void checkAssetBalanceForSellOrder(Optional<AssetAccount> assetAccount, Order order) {
        if (assetAccount.isEmpty() || assetAccount.get().getQuantity() < order.getQuantity()) {
            throw new InvalidRequestException("Insufficient fund in wallet.");
        }
    }

    /*
        Update wallet balance based on order side.
        If Buy order, add trade quantity to base asset account; subtract (trade price * trade quantity) from quote asset account.
        If Sell order, add (trade price * trade quantity) to quote asset account; subtract trade quantity from base asset account.
        If user does not have AssetAccount when adding fund to asset, we will create new AssetAccount for the asset
        and tagged it to their wallet.
     */
    @Override
    public void updateWalletBalance(Trade trade, MarketTick marketTick) {
        long userAccountId = trade.getUserAccountId();
        Optional<AssetAccount> optionalBaseAssetAccount = assetAccountRepository.findByUserAccountIdAndAssetId(userAccountId, marketTick.getBaseAssetId());
        Optional<AssetAccount> optionalQuoteAssetAccount = assetAccountRepository.findByUserAccountIdAndAssetId(userAccountId, marketTick.getQuoteAssetId());
        if (trade.getOrderSide() == OrderSide.BUY) {
            if (optionalBaseAssetAccount.isEmpty()) {
                createNewWalletForBaseAsset(marketTick, trade, userAccountId);
            }
            else {
                updateBaseAssetForBuyOrder(optionalBaseAssetAccount.get(), trade);
            }
            updateQuoteAssetForBuyOrder(optionalQuoteAssetAccount.get(), trade);
        }
        else {
            if (optionalQuoteAssetAccount.isEmpty()) {
                createNewWalletForQuoteAsset(marketTick, trade, userAccountId);
            }
            else {
                updateQuoteAssetForSellOrder(optionalQuoteAssetAccount.get(), trade);
            }
            updateBaseAssetForSellOrder(optionalBaseAssetAccount.get(), trade);
        }
    }

    private void createNewWalletForBaseAsset(MarketTick marketTick, Trade trade, long userAccountId) {
        Wallet wallet = walletRepository.findByUserAccountId(userAccountId);
        AssetAccount baseAssetAccount = new AssetAccount();
        baseAssetAccount.setUserAccountId(userAccountId);
        baseAssetAccount.setAssetId(marketTick.getBaseAssetId());
        baseAssetAccount.setQuantity(trade.getQuantity());
        baseAssetAccount.setWallet(wallet);
        assetAccountRepository.save(baseAssetAccount);
    }

    private void createNewWalletForQuoteAsset(MarketTick marketTick, Trade trade, long userAccountId) {
        Wallet wallet = walletRepository.findByUserAccountId(userAccountId);
        AssetAccount quoteAssetAccount = new AssetAccount();
        quoteAssetAccount.setUserAccountId(userAccountId);
        quoteAssetAccount.setAssetId(marketTick.getQuoteAssetId());
        quoteAssetAccount.setQuantity(trade.getQuantity() * trade.getFilledPrice());
        quoteAssetAccount.setWallet(wallet);
        assetAccountRepository.save(quoteAssetAccount);
    }

    private void updateBaseAssetForBuyOrder(AssetAccount baseAssetAccount, Trade trade) {
        baseAssetAccount.setQuantity(baseAssetAccount.getQuantity() + trade.getQuantity());
        assetAccountRepository.save(baseAssetAccount);
    }

    private void updateQuoteAssetForBuyOrder(AssetAccount quoteAssetAccount, Trade trade) {
        double price = trade.getFilledPrice() * trade.getQuantity();
        quoteAssetAccount.setQuantity(quoteAssetAccount.getQuantity() - price);
        assetAccountRepository.save(quoteAssetAccount);
    }

    private void updateQuoteAssetForSellOrder(AssetAccount quoteAssetAccount, Trade trade) {
        double quantity = trade.getFilledPrice() * trade.getQuantity();
        quoteAssetAccount.setQuantity(quoteAssetAccount.getQuantity() + quantity);
        assetAccountRepository.save(quoteAssetAccount);
    }

    private void updateBaseAssetForSellOrder(AssetAccount baseAssetAccount, Trade trade) {
        baseAssetAccount.setQuantity(baseAssetAccount.getQuantity() - trade.getQuantity());
        assetAccountRepository.save(baseAssetAccount);
    }
}
