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

    @Override
    public void checkWalletBalance(Order order, MarketTick marketTick) {
        long userAccountId = order.getUserAccountId();
        Optional<AssetAccount> assetAccount;
        double totalOrderPrice;
        if (order.getOrderSide() == OrderSide.BUY) {
            assetAccount = assetAccountRepository.findByUserAccountIdAndAssetId(userAccountId, marketTick.getQuoteAssetId());
            totalOrderPrice = marketTick.getAskPrice() * order.getQuantity();
            if (assetAccount.isEmpty() || assetAccount.get().getQuantity() < totalOrderPrice) {
                throw new InvalidRequestException("Insufficient fund in wallet.");
            }
        } else {
            assetAccount = assetAccountRepository.findByUserAccountIdAndAssetId(userAccountId, marketTick.getBaseAssetId());
            if (assetAccount.isEmpty() || assetAccount.get().getQuantity() < order.getQuantity()) {
                throw new InvalidRequestException("Insufficient fund in wallet.");
            }
        }
    }

    @Override
    public void updateWalletBalance(Trade trade, MarketTick marketTick) {
        long userAccountId = trade.getUserAccountId();
        Optional<AssetAccount> optionalBaseAssetAccount = assetAccountRepository.findByUserAccountIdAndAssetId(userAccountId, marketTick.getBaseAssetId());
        AssetAccount baseAssetAccount;
        Optional<AssetAccount> optionalQuoteAssetAccount = assetAccountRepository.findByUserAccountIdAndAssetId(userAccountId, marketTick.getQuoteAssetId());
        AssetAccount quoteAssetAccount;
        if (trade.getOrderSide() == OrderSide.BUY) {
            if (optionalBaseAssetAccount.isEmpty()) {
                Wallet wallet = walletRepository.findByUserAccountId(userAccountId);
                baseAssetAccount = new AssetAccount();
                baseAssetAccount.setUserAccountId(userAccountId);
                baseAssetAccount.setAssetId(marketTick.getBaseAssetId());
                baseAssetAccount.setQuantity(trade.getQuantity());
                baseAssetAccount.setWallet(wallet);
                assetAccountRepository.save(baseAssetAccount);
            }
            else {
                baseAssetAccount = optionalBaseAssetAccount.get();
                baseAssetAccount.setQuantity(baseAssetAccount.getQuantity() + trade.getQuantity());
                assetAccountRepository.save(baseAssetAccount);
            }
            quoteAssetAccount = optionalQuoteAssetAccount.get();
            double price = trade.getFilledPrice() * trade.getQuantity();
            quoteAssetAccount.setQuantity(quoteAssetAccount.getQuantity() - price);
            assetAccountRepository.save(quoteAssetAccount);
        } else {
            if (optionalQuoteAssetAccount.isEmpty()) {
                Wallet wallet = walletRepository.findByUserAccountId(userAccountId);
                quoteAssetAccount = new AssetAccount();
                quoteAssetAccount.setUserAccountId(userAccountId);
                quoteAssetAccount.setAssetId(marketTick.getQuoteAssetId());
                quoteAssetAccount.setQuantity(trade.getQuantity() * trade.getFilledPrice());
                quoteAssetAccount.setWallet(wallet);
                assetAccountRepository.save(quoteAssetAccount);
            }
            else {
                quoteAssetAccount = optionalQuoteAssetAccount.get();
                double quantity = trade.getFilledPrice() * trade.getQuantity();
                quoteAssetAccount.setQuantity(quoteAssetAccount.getQuantity() + quantity);
                assetAccountRepository.save(quoteAssetAccount);
            }
            baseAssetAccount = optionalBaseAssetAccount.get();
            baseAssetAccount.setQuantity(baseAssetAccount.getQuantity() - trade.getQuantity());
            assetAccountRepository.save(baseAssetAccount);
        }
    }
}
