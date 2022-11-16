import SwiftUI
import shared

struct BreedList: View {
    let breeds: [BreedListItem]
    let onItemSelected: () -> Void
    
    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: 4, pinnedViews: [.sectionHeaders]) {
                Section(header:
                    HStack {
                        Text("Breeds")
                            .font(Font.title2)
                            .foregroundColor(Color.primary)
                        Spacer()
                        Button(action: { onItemSelected() }) {
                            Image(systemName: "xmark")
                                .foregroundColor(Color.primary)
                                .padding(.horizontal, 16)
                        }
                    }
                    .padding(16)
                    .opacity(1.0)
                    .background(Color.secondary)
                ) {
                    ForEach(breeds, id: \.id) { breed in
                        Button(action: {
                            breed.onClick()
                            onItemSelected()
                        }) {
                            HStack {
                                Text(breed.name)
                                    .foregroundColor(Color.primary)
                                    .padding(16)
                                if (breed.selected) {
                                    Spacer()
                                    Image(systemName: "star.fill")
                                        .foregroundColor(Color.primary)
                                        .padding(16)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
