package com.example.aquariux.useraccount.actions;

import com.example.aquariux.core.models.entities.Asset;
import com.example.aquariux.core.models.entities.AssetAccount;
import com.example.aquariux.core.models.entities.Wallet;
import com.example.aquariux.core.repositories.AssetAccountRepository;
import com.example.aquariux.core.repositories.AssetRepository;
import com.example.aquariux.core.repositories.WalletRepository;
import com.example.aquariux.exception.InvalidRequestException;
import org.springframework.stereotype.Service;
import com.example.aquariux.useraccount.models.responses.AccountBalanceResponse;

import java.util.List;
import java.util.Optional;

@Service
public class GetAccountBalanceActionImpl implements GetAccountBalanceAction {
    private final WalletRepository walletRepository;
    private final AssetAccountRepository assetAccountRepository;
    private final AssetRepository assetRepository;

    public GetAccountBalanceActionImpl(WalletRepository walletRepository,
                                       AssetAccountRepository assetAccountRepository,
                                       AssetRepository assetRepository) {
        this.walletRepository = walletRepository;
        this.assetAccountRepository = assetAccountRepository;
        this.assetRepository = assetRepository;
    }

    @Override
    public List<AccountBalanceResponse> getAllAccountBalanceInWallet(long userAccountId) {
        Wallet wallet = walletRepository.findByUserAccountId(userAccountId);
        return wallet.getAssetAccounts()
                .stream()
                .map(this::transformToResponse)
                .toList();
    }

    @Override
    public AccountBalanceResponse getAccountBalanceByAssetSymbolAndUserAccountId(String symbol, long userAccountId) {
        Asset asset = assetRepository.findBySymbol(symbol.toUpperCase());
        Optional<AssetAccount> assetAccount = assetAccountRepository.findByUserAccountIdAndAssetId(userAccountId, asset.getAssetId());
        if(assetAccount.isPresent()) {
            return transformToResponse(assetAccount.get());
        } else {
            throw new InvalidRequestException("No account with asset symbol: " + symbol + ", found in the wallet.");
        }
    }

    private AccountBalanceResponse transformToResponse(AssetAccount assetAccount) {
        Optional<Asset> asset = assetRepository.findById(assetAccount.getAssetId());
        return asset.map(value -> AccountBalanceResponse.builder()
                .assetSymbol(value.getSymbol())
                .assetName(value.getName())
                .balance(String.valueOf(assetAccount.getQuantity()))
                .build()).orElse(null);
    }
}
