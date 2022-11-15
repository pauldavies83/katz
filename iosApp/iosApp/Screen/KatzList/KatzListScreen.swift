import SwiftUI
import shared

struct KatzListScreen: View {
    @ObservedObject private var viewModel = KatzListViewModel()
    @State private var showSheet = false

	var body: some View {
        ZStack {
            if let katz = viewModel.state.kats {
                KatzGrid(katz: katz)
            }
        }
        .onAppear { viewModel.onAppear() }
        .onDisappear { viewModel.onDisappear() }
        .sheet(isPresented: $showSheet) {
            if let breeds = viewModel.state.breeds {
                BreedList(
                    breeds: breeds,
                    onItemSelected: { showSheet = false }
                )
            }
        }
        .toolbar {
            ToolbarItemGroup(placement: .bottomBar) {
                HStack {
                    Button(action: { showSheet = !showSheet }) {
                        Image(systemName: "arrow.up.circle")
                            .foregroundColor(Color.primary)
                    }
                    Spacer()
                    Text(viewModel.state.title ?? "Katz")
                    Spacer()
                    HStack {}
                }
            }
        }
    }
}

struct KatzList_Previews: PreviewProvider {
	static var previews: some View {
        KatzGrid(katz: ["I'm a Kat"])
	}
}
