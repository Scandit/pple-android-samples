package com.scandit.shelf.javaapp.catalog;

import com.scandit.shelf.sdk.catalog.ProductCatalog;

/**
 * A singleton class that stores a ProductCatalog instance.
 */
public class CatalogStore {

    private static CatalogStore INSTANCE;

    private ProductCatalog catalog = null;

    private CatalogStore() {}

    public static synchronized CatalogStore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CatalogStore();
        }
        return INSTANCE;
    }

    public ProductCatalog getProductCatalog() {
        return catalog;
    }

    public void setProductCatalog(ProductCatalog catalog) {
        this.catalog = catalog;
    }
}
