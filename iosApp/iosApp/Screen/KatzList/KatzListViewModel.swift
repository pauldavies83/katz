import KMPNativeCoroutinesAsync
import shared

@MainActor
class KatzListViewModel: ObservableObject {

    @Injected private var sharedViewModel: KatzListSharedViewModel
    
    @Published var state: KatzListSharedViewModel.State = KatzListSharedViewModel.State.companion.initial()
    
    private var stateTask: Task<(), Never>? = nil
    
    func onAppear() {
        stateTask = Task {
            do {
                let stream = asyncStream(for: sharedViewModel.stateNative)
                for try await sharedState in stream {
                    self.state = sharedState
                }
            } catch {
                // handle error here
            }
        }
    }
    
    func onDisappear() {
        stateTask?.cancel()
    }
}
