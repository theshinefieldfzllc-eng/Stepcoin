# StepCoin

Ek fitness app: step counter + virtual coins + rewards, AdMob ads se monetize.

## Features (v1)
- 👣 Daily step counter (phone ke step sensor se) + progress ring
- 🪙 Virtual coins: 1,000 steps = 10 coins, daily goal = +100 coins
- 🔥 Daily streak
- 📺 Rewarded ad: "Watch & Earn +50 coins"
- 🎁 Daily reward: +25 coins
- 📊 Distance + calories estimate
- Banner + rewarded ads (abhi **TEST** ids)

## Ads — real kamai ke liye
Abhi Google ke **test ad ids** lage hain (test ads, kamai 0). Real kamai ke liye:
1. [admob.google.com](https://admob.google.com) pe account banao.
2. App + ad units banao → **App ID**, **Banner id**, **Rewarded id** milenge.
3. Replace karo:
   - `AndroidManifest.xml` → `com.google.android.gms.ads.APPLICATION_ID`
   - `activity_main.xml` → banner `adUnitId`
   - `MainActivity.kt` → `REWARDED_ID`

## APK kaise bane
GitHub pe push → **Actions** → "Build APK" → Artifacts → `stepcoin-debug-apk`.

## Phase 2 (baad me)
- Pro (ad-free) — Google Play Billing
- More tools (BMI, water reminder, weekly chart)
- Reward redemption (voucher) + backend + anti-cheat
