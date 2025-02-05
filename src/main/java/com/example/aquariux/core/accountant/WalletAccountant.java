package com.example.aquariux.core.accountant;

import com.example.aquariux.core.models.entities.Order;
import com.example.aquariux.core.models.entities.Trade;
import com.example.aquariux.core.models.markets.MarketTick;

public interface WalletAccountant {
    void checkWalletBalance(Order order, MarketTick marketTick);
    void updateWalletBalance(Trade trade, MarketTick marketTick);
}
