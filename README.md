# Swipe Android App
The Swipe Android App is an Android application designed for product management. It facilitates a smooth user experience, allowing users to easily view and manage products. Key features include product search, user-friendly navigation, and efficient data entry with image capabilities.

## 1. Product Listing Screen(Host Fragment)
### Features:
- Enable a user-friendly search feature for products.
- Facilitate quick navigation to the Add Product screen via a dedicated button.
- Present an extensive catalog of products obtained from the Swipe API endpoint.
- Provide seamless scrolling through the product list.
- Implement dynamic loading of product images from provided URLs or default images if URLs are empty.
- Offer visual cues to users in case of network disconnection.

## 2. Add Product Screen (BottomSheetDialogFragment)
### Features:
- Efficient addition of new products with a streamlined process.
- Comprehensive data entry through input fields for product name, selling price, tax rate and attach image.
- Validation ensures accurate data entry, covering product type selection, non-empty product name, and proper formatting for selling price and tax.
- Choose the product type from a curated list of options.

### API Integration:
- GET Request: https://app.getswipe.in/api/public/get
- POST Request: https://app.getswipe.in/api/public/add

## 3. Tech Stacks:
- Single Activity and Multiple Fragment
- Jetpack Navigation
- MVVM Architecture
- Sealed class used
- Singleton pattern
- Hilt (Dependency Injection)
- Retrofit (API Calling)
- DataBinding
- LiveData (Observer Pattern)
- enum class (to define product types)
- Coroutines (Background tasks)
- Generic class (to handle network failure)
- Recyclerview
- XML (for ui)


### Screenshots
<div style="dispaly:flex">
    <img src="https://github.com/amitYadavDev/SwipeApp/assets/45551012/99168dee-75e7-4d28-aff4-3f67f250e396" width="24%">
    <img src="https://github.com/amitYadavDev/SwipeApp/assets/45551012/ce79b64c-fa14-4487-9b17-c1027c5156e9" width="24%">
    <img src="https://github.com/amitYadavDev/SwipeApp/assets/45551012/577c1c7f-313d-4ee8-8d59-79671af9aa1c" width="24%">
    <img src="https://github.com/amitYadavDev/SwipeApp/assets/45551012/9c19b840-08ea-4a3e-a727-66ac77f21b5b" width="24%">
    <img src="https://github.com/amitYadavDev/SwipeApp/assets/45551012/08d79216-c699-47ba-a840-5221d8b4ac5b" width="24%">
</div>

