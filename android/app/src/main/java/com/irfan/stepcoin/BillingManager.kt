package com.irfan.stepcoin

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*

/**
 * Handles the one-time "StepCoin Pro" purchase.
 * Pro = remove banner ads + 2x coins.
 *
 * The product id below MUST match the in-app product you create in the
 * Play Console (Monetize -> Products -> In-app products).
 */
class BillingManager(
    private val context: Context,
    private val onProChanged: () -> Unit
) {
    companion object {
        const val PRO_PRODUCT_ID = "stepcoin_pro"
    }

    private var productDetails: ProductDetails? = null

    /** Localised price string from Google (e.g. "$30.00", "AED 110.00"). Null until loaded. */
    var formattedPrice: String? = null
        private set

    private val purchasesUpdatedListener = PurchasesUpdatedListener { result, purchases ->
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (p in purchases) handlePurchase(p)
        }
    }

    private val client: BillingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
        )
        .enableAutoServiceReconnection()
        .build()

    fun start() {
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProduct()
                    restorePurchases()
                }
            }
            override fun onBillingServiceDisconnected() { /* auto-reconnect is enabled */ }
        })
    }

    private fun queryProduct() {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PRO_PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()
        client.queryProductDetailsAsync(params) { result, queryResult ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                val pd = queryResult.productDetailsList.firstOrNull()
                productDetails = pd
                formattedPrice = pd?.oneTimePurchaseOfferDetailsList?.firstOrNull()?.formattedPrice
                onProChanged()
            }
        }
    }

    /** Re-check what the user owns (purchases made on other devices / earlier). */
    fun restorePurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
        client.queryPurchasesAsync(params) { result, purchases ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                for (p in purchases) handlePurchase(p)
            }
        }
    }

    /** Start the Google purchase sheet. Returns false if the product isn't ready yet. */
    fun launchPurchase(activity: Activity): Boolean {
        val pd = productDetails ?: return false
        val offer = pd.oneTimePurchaseOfferDetailsList?.firstOrNull() ?: return false
        val token = offer.offerToken ?: return false
        val pdParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(pd)
            .setOfferToken(token)
            .build()
        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(pdParams))
            .build()
        val r = client.launchBillingFlow(activity, flowParams)
        return r.responseCode == BillingClient.BillingResponseCode.OK
    }

    private fun handlePurchase(purchase: Purchase) {
        if (!purchase.products.contains(PRO_PRODUCT_ID)) return
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return

        if (!Prefs.getBool(context, "pro", false)) {
            Prefs.setBool(context, "pro", true)
        }
        onProChanged()

        // Must acknowledge within 3 days or Google auto-refunds the purchase.
        if (!purchase.isAcknowledged) {
            val ackParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            client.acknowledgePurchase(ackParams) { /* acknowledged */ }
        }
    }

    fun end() {
        try { client.endConnection() } catch (e: Exception) { /* ignore */ }
    }
}
