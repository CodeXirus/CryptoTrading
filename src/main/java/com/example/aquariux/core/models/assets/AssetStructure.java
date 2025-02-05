package com.example.aquariux.core.models.assets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AssetStructure {
    private long assetId;
    private String symbol;
    private String name;
}
