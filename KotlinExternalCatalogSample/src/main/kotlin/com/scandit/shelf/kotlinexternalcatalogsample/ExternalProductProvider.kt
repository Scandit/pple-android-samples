/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scandit.shelf.kotlinexternalcatalogsample

import com.scandit.shelf.sdk.catalog.Product
import com.scandit.shelf.sdk.catalog.ProductProvider
import com.scandit.shelf.sdk.catalog.Store

/**
 * Dummy implementation of the ProductProvider interface.
 *
 * Bear in mind that the findProduct(...) method of your custom ProductProvider should be fast,
 * as it might need to be called on every camera frame.
 * Therefore it is advised to cache the products in-memory or local database, rather than to
 * request them on the fly, every time the findProduct(...) method is called.
 */
class ExternalProductProvider(store: Store) : ProductProvider {
    private val map = mapOf(
        "7846987774588" to Product(0, store.id, "7846987774588", null, "Fair Trade Espresso", 7.99f, null),
        "7654782245697" to Product(1, store.id, "7654782245697", null, "Classic Espresso", 5.99f, null),
        "7853105120547" to Product(2, store.id, "7853105120547", null, "Honey & Nut Muesli", 6.99f, null)
    )

    override fun findProduct(code: String): Product? = map[code]
}
