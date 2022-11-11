import SwiftUI
import shared

struct KatzListScreen: View {
    @ObservedObject private var viewModel = KatzListViewModel()

	var body: some View {
        KatzList(katz: viewModel.state)
            .onAppear { viewModel.onAppear() }
            .onDisappear { viewModel.onDisappear() }
	}
}

struct KatzList: View {
    let katz: [String]
    
    var body: some View {
        ForEach(katz, id: \.self) { kat in
            Text(kat)
        }
    }
}

struct KatzList_Previews: PreviewProvider {
	static var previews: some View {
        KatzList(katz: ["I'm a Kat"])
	}
}
