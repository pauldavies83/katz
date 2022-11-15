A Kotlin Multiplaform Mobile project to retrieve images of cats from [The Cat API](https://thecatapi.com/).

I've started with a simple KMM skeleton, but focused on the Android UI implementation rather than iOS (for now.). All UI-based code is written in Jetpack Compose and SwiftUI. A very lightweight, platform native **ViewModel** is included for each screen, which wraps a **SharedViewModel** containing all the logic required for that screen. As much logic should be kept away from views as possible.

I've used pieces of various well-discussed architectural patterns that I think suit well to a KMM app. The app broadly follows an MVI/Redux pattern, omitting the traditional **Store** from Redux in favour of a **StateFlow** to be consumed in each native platform view. **ViewModel.State** classes are created in the **SharedViewModel** and emitted on the **StateFlow**, with these state classes containing action callbacks to be connected to view components as required, negating the need for public functions exposed on the **SharedViewModel** and ensuring that only appropriate view components can perform actions to avoid errors. 

"Clean Architecture" principles such as separating between layers of **Service/Repository/ViewModel** have been used, and as the app progresses this could be further followed to include **UseCases** to re-use data assembly code as required. 

My approach to delivery was to prioritise getting something _working_ initially, which was to grab a collection of unfiltered/queried images from the API, and then display this. I then further refined this by allowing the data set to be refined to a specific breed, and improved the view code from it's very basic beginnings. 

Performance was then improved by introducing a super simple memory cache before I started to run out of time - I tend to prefer a strategy emitting results as a flow first from local storage and followed by an update from the network - this ensures there is often _something_ to show to the user instead of a loading state. Obviously care should be taken not to do this for sensitive information that always needs to be up-to-date to be shown. 

Very simple error handling has been added - this should be futher expanded to allow the user to retry the failing action. Network response page sizes are hardcoded to 50 items for a trade-off between simplicity and performance - pagination should be added and this hard limit removed. 

Rather than including mocking frameworks for testing, I've extracted an **interface** to allow a **Fake** implementation to be used to allow manipulation during testing. At a wider-scale, a mocking framework may well be more appropriate. 

**master** represents about 4-5 hours of coding effort. I haven't included the intial setup of the KMM project and **Koin** Dependency Injection framework in this time, as that isn't a problem I'd usually be solving day-to-day - I hope that is okay! I'll continue working on this to add iOS view implementation and further refine both platforms, but I'll do this on branches with pull requests.
