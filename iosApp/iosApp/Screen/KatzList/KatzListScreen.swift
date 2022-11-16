import SwiftUI
import shared

struct KatzListScreen: View {
    @ObservedObject private var viewModel = KatzListViewModel()
    
    let bottomBarMinHeight: CGFloat = 64.0

	var body: some View {
        ZStack(alignment: .bottom) {
            if let katz = viewModel.state.kats {
                KatzGrid(katz: katz)
                    .padding(.top, UIApplication.shared.keyWindow?.safeAreaInsets.top)
                    .padding(.bottom, (UIApplication.shared.keyWindow?.safeAreaInsets.bottom ?? 0) + bottomBarMinHeight)
            }
            KatzListBottomBar(
                minHeight: bottomBarMinHeight,
                title: viewModel.state.title,
                breeds: viewModel.state.breeds,
                breedDetails: viewModel.state.breedDetails
            )
        }
        .onAppear { viewModel.onAppear() }
        .onDisappear { viewModel.onDisappear() }
        .frame(maxHeight: .infinity, alignment: .bottom)
        .ignoresSafeArea(edges: .vertical)
    }
}

