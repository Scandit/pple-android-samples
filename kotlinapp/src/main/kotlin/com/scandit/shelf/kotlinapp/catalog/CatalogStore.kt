package com.scandit.shelf.kotlinapp.catalog

import com.scandit.shelf.sdk.catalog.ProductCatalog

/**
 * A singleton class that stores a ProductCatalog instance.
 */
object CatalogStore {
    var productCatalog: ProductCatalog? = null
}
