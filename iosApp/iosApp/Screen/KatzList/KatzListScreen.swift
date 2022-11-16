import SwiftUI
import shared

struct KatzListScreen: View {
    @ObservedObject private var viewModel = KatzListViewModel()

	var body: some View {
        ZStack(alignment: .bottom) {
            if let katz = viewModel.state.kats {
                KatzGrid(katz: katz)
            }
            KatzListBottomBar(
                title: viewModel.state.title,
                breeds: viewModel.state.breeds
            )
        }
        .onAppear { viewModel.onAppear() }
        .onDisappear { viewModel.onDisappear() }
        .frame(maxHeight: .infinity, alignment: .bottom)
        .ignoresSafeArea(edges: .vertical)
    }
}

