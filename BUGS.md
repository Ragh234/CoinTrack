# Bugs

Symptom, cause, fix -- kept as I go.

---

## 1. The project didn't build at all on this machine

**Symptom**

`./gradlew assembleDebug` failed before compiling any app code:

```
Execution failed for task ':app:kaptDebugKotlin'.
> Error while evaluating property 'javacOptions' of task ':app:kaptDebugKotlin'.
   > Failed to calculate the value of task ':app:kaptDebugKotlin' property 'javacOptions'.
      > 25.0.2
```

**Cause**

`kapt` runs its own `javac` subprocess and parses `java.specification.version` to pick
compiler flags. On JDK 21+ that version string's format changed and kapt's parser throws
outright -- nothing to do with Hilt, Room, or any app code, it's a kapt-vs-JDK
incompatibility that hits any project using it.

Fixing that surfaced two more, chained:

- Switching to KSP meant applying the KSP Gradle plugin, which needs a version tied
  exactly to the project's Kotlin version (`1.9.22-1.0.18` for Kotlin 1.9.22).
- Gradle 8.8 itself couldn't parse the build script under JDK 25 either
  (`Unsupported class file major version 69` -- Gradle 8.8 predates JDK 25 support).
  Fixed by pointing `JAVA_HOME` at a JDK 22 install instead of the JDK 25 that Android
  Studio's bundled JBR now ships.
- Hilt's Gradle plugin from 2.60.x requires AGP 9+; this project is on AGP 8.5.2. Landed
  on Hilt 2.51.1, the newest release that still supports KSP without needing AGP 9.
- Room 2.8.4 ships binaries compiled against Kotlin 2.1, which Kotlin 1.9.22 rejects as a
  newer metadata version it can't read. Landed on Room 2.6.1 for the same reason as Hilt.

**Fix**

Migrated `kapt` to KSP for both Hilt and Room (`kotlin-kapt` plugin removed, `com.google.
devtools.ksp` added), pinned to versions that actually satisfy this project's AGP and
Kotlin versions rather than whatever's currently latest. `JAVA_HOME` has to point at a
JDK Gradle 8.8 supports (JDK 17-22) to build this project at all right now.

---

## 2. A portfolio holding whose coin has no cached price showed as a 100% loss

**Symptom**

Add a holding for a coin that isn't in the currently cached market data -- because the
market screen hasn't loaded yet, the device is offline, or the coin has dropped out of
the latest ticker response -- and the Portfolio screen renders it in red at -100% return,
identical to what a real total loss would look like.

**Cause**

`PortfolioUseCases.observeSummary()` looked up each holding's price in the cached market
coins and defaulted to `0.0` when the coin wasn't there:

```kotlin
currentPrice = prices[holding.coinId]?.currentPrice ?: 0.0
```

`PortfolioCalculator.calculateHolding` then computed `currentValue = quantity * 0.0 = 0`,
so `profitLoss = 0 - investedAmount`, which is exactly what a 100% loss looks like. "No
price data yet" and "this coin is worth nothing" collapsed into the same number with no
way to tell them apart downstream.

Reproducible in the normal flow: Portfolio's price data comes entirely from Market's
cache, and there's no independent refresh path for Portfolio itself. Any holding whose
coin isn't in that cache at read time hits this.

**Fix**

`currentPrice` is passed as `Double?` instead of defaulting to `0.0`, and
`PortfolioHoldingSummary` carries a `hasPriceData` flag. `calculateSummary` includes an
unpriced holding's invested amount in the total (the money really was spent) but excludes
it from current value and profit/loss, and reports `unpricedHoldingsCount` so the summary
card can say so. The holding row shows "Price unavailable" in a neutral color instead of
a red percentage.

Verified two ways: added a real holding against live market data on-device and confirmed
the normal priced path still shows the correct value and return (no regression from the
nullable refactor); and pinned the missing-price behavior itself with two new
`PortfolioCalculatorTest` cases, since `PortfolioCalculator` is pure Kotlin with no
Android dependency and doesn't need a device to test. Reproducing the exact live scenario
(a coin missing from the on-device cache) would have needed editing the app's Room
database directly, which turned out to be blocked by WAL-mode complications reading the
raw `.db` file off the device -- the unit tests are the reliable proof instead.

---

## 3. The watchlist had the same missing-price bug as the portfolio

**Symptom**

Same shape as bug #2, different screen: a coin on the watchlist that isn't in the cached
market data at read time -- market screen hasn't loaded yet, offline, or the coin dropped
out of the latest ticker response -- shows ₹0.00 and 0.00% instead of any indication that
its price is simply unknown.

**Cause**

`WatchlistViewModel` combined the watchlist with the cached market coins and, for any
watchlisted coin not found in the cache, built a fallback `Coin` with `currentPrice = 0.0`
and `percentChange24h = 0.0` to satisfy the shared `Coin` type's shape. Once found, this
was the exact pattern from bug #2 in a second place -- I only spotted it because I'd just
fixed the same root cause in `PortfolioCalculator` and knew what to look for.

**Fix**

Extracted the merge logic into `WatchlistDisplayMapper`, a pure function pairing each
watchlist entry with a `hasPriceData` flag, kept local to the watchlist feature rather
than making `Coin.currentPrice` nullable everywhere it's used (Market screen, the
portfolio add-holding dropdown, coin detail). `CoinListItem` -- shared between Market and
Watchlist -- gets an optional `priceUnavailable` flag that defaults to `false`, so
Market's rendering is untouched and only Watchlist opts in to showing "Price unavailable"
in place of a fabricated ₹0.00.

Verified on-device (added Bitcoin, confirmed the real price still renders with no
regression) and pinned the missing-price case itself with 3 new
`WatchlistDisplayMapperTest` cases, for the same reason as bug #2: reproducing a genuinely
delisted coin live isn't something I can trigger without mocking the ticker API.
