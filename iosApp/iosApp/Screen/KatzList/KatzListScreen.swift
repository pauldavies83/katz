import SwiftUI
import shared

struct KatzListScreen: View {
    @ObservedObject private var viewModel = KatzListViewModel()

	var body: some View {
        ZStack {
            if let katz = viewModel.state.kats {
                KatzGrid(katz: katz)
            }
        }
        .onAppear { viewModel.onAppear() }
        .onDisappear { viewModel.onDisappear() }
    }
}

struct KatzList_Previews: PreviewProvider {
	static var previews: some View {
        KatzGrid(katz: ["I'm a Kat"])
	}
}
