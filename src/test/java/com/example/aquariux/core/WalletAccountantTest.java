package com.example.aquariux.core;

import com.example.aquariux.core.accountant.WalletAccountantImpl;
import com.example.aquariux.core.models.entities.AssetAccount;
import com.example.aquariux.core.models.entities.Order;
import com.example.aquariux.core.models.entities.Trade;
import com.example.aquariux.core.models.markets.MarketTick;
import com.example.aquariux.core.repositories.AssetAccountRepository;
import com.example.aquariux.core.repositories.WalletRepository;
import com.example.aquariux.exception.InvalidRequestException;
import com.example.aquariux.order.models.OrderSide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletAccountantTest {
    @Mock
    private AssetAccountRepository assetAccountRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletAccountantImpl walletAccountant;

    private Order order;
    private MarketTick marketTick;
    private Trade trade;
    private AssetAccount assetAccount;

    @BeforeEach
    void setUp() {
        order = mock(Order.class);
        marketTick = mock(MarketTick.class);
        trade = mock(Trade.class);
        assetAccount = new AssetAccount();
        assetAccount.setQuantity(100.0);
    }

    @Test
    void test_check_wallet_balance_throw_exception_insufficient_funds_for_buy() {
        when(order.getOrderSide()).thenReturn(OrderSide.BUY);
        when(order.getUserAccountId()).thenReturn(1L);
        when(order.getQuantity()).thenReturn(10.0);
        when(marketTick.getAskPrice()).thenReturn(20.0);
        when(marketTick.getQuoteAssetId()).thenReturn(2L);
        when(assetAccountRepository.findByUserAccountIdAndAssetId(1L, 2L)).thenReturn(Optional.of(assetAccount));

        assertThrows(InvalidRequestException.class, () -> walletAccountant.checkWalletBalance(order, marketTick));
    }

    @Test
    void test_check_wallet_balance_throw_exception_insufficient_funds_for_sell() {
        when(order.getOrderSide()).thenReturn(OrderSide.SELL);
        when(order.getUserAccountId()).thenReturn(1L);
        when(order.getQuantity()).thenReturn(150.0);
        when(marketTick.getBaseAssetId()).thenReturn(3L);
        when(assetAccountRepository.findByUserAccountIdAndAssetId(1L, 3L)).thenReturn(Optional.of(assetAccount));

        assertThrows(InvalidRequestException.class, () -> walletAccountant.checkWalletBalance(order, marketTick));
    }

    @Test
    void test_update_wallet_balance_for_buy_order() {
        when(trade.getOrderSide()).thenReturn(OrderSide.BUY);
        when(trade.getUserAccountId()).thenReturn(1L);
        when(trade.getQuantity()).thenReturn(5.0);
        when(trade.getFilledPrice()).thenReturn(10.0);
        when(marketTick.getBaseAssetId()).thenReturn(4L);
        when(marketTick.getQuoteAssetId()).thenReturn(5L);
        when(assetAccountRepository.findByUserAccountIdAndAssetId(1L, 4L)).thenReturn(Optional.of(assetAccount));
        when(assetAccountRepository.findByUserAccountIdAndAssetId(1L, 5L)).thenReturn(Optional.of(assetAccount));

        walletAccountant.updateWalletBalance(trade, marketTick);

        verify(assetAccountRepository, times(2)).save(any(AssetAccount.class));
    }

    @Test
    void test_update_wallet_balance_for_sell_order() {
        when(trade.getOrderSide()).thenReturn(OrderSide.SELL);
        when(trade.getUserAccountId()).thenReturn(1L);
        when(trade.getQuantity()).thenReturn(5.0);
        when(trade.getFilledPrice()).thenReturn(10.0);
        when(marketTick.getBaseAssetId()).thenReturn(6L);
        when(marketTick.getQuoteAssetId()).thenReturn(7L);
        when(assetAccountRepository.findByUserAccountIdAndAssetId(1L, 6L)).thenReturn(Optional.of(assetAccount));
        when(assetAccountRepository.findByUserAccountIdAndAssetId(1L, 7L)).thenReturn(Optional.of(assetAccount));

        walletAccountant.updateWalletBalance(trade, marketTick);

        verify(assetAccountRepository, times(2)).save(any(AssetAccount.class));
    }
}