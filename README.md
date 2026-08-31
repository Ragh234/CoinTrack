# CoinTrack

CoinTrack is a simple cryptocurrency market and portfolio tracker built with Kotlin and Jetpack Compose.

## Features

- Cryptocurrency market tracking with INR prices where supported by the API
- Local search by coin name or symbol
- Coin details with current price, 24-hour change, market cap, logo, and a simple price chart
- Persistent watchlist
- Local portfolio holdings with invested value, current value, profit/loss, and return percentage
- Offline cache for the latest loaded market data

## Tech Stack

- Kotlin
- Jetpack Compose
- MVVM
- Clean Architecture
- Retrofit
- Room
- Hilt
- Coroutines and Flow
- Coil
- CoinPaprika API
- JUnit

## Architecture

```text
Compose UI
    -> ViewModel
    -> Use Cases
    -> Repository
       -> API
       -> Room
```

The app keeps UI state in ViewModels, business operations in use cases, and data access behind repository interfaces. Room stores cached market coins, watchlist coins, and portfolio holdings.

## Future Improvements

- More detailed charts when a richer API plan is available
- Editable portfolio holdings
- Sorting options for market and watchlist screens
- Better coin image coverage if the market endpoint exposes logos directly
