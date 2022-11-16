import SwiftUI
import shared

struct KatzListBottomBar: View {
    @State private var showBreeds = false
    @State private var showDetails = false
        
    let title: String?
    let breeds: [BreedListItem]?
    let breedDetails: BreedListItem.Details?
    
    var body: some View {
        VStack {
            if (showBreeds) {
                if let breeds = breeds {
                    BreedList(
                        breeds: breeds,
                        onItemSelected: { showBreeds = false }
                    )
                    .frame(maxHeight: (UIScreen.main.bounds.height) / 1.5, alignment: .bottom)
                }
            } else if (showDetails) {
                if let breedDetails = breedDetails {
                    VStack(alignment: .leading, spacing: 8) {
                        if let description = breedDetails.description_ {
                            Text(description)
                        }
                        if let originCountry = breedDetails.originCountry {
                            Text(originCountry)
                        }
                    }
                    .padding()
                }
            }
            HStack {
                Button(action: { showBreeds = !showBreeds }) {
                    Image(systemName: "arrow.up.circle")
                        .foregroundColor(Color.primary)
                }
                Spacer()
                Text(title ?? "Katz")
                Spacer()
                Button(action: { showDetails = !showDetails }) {
                    Image(systemName: "info.circle.fill")
                        .foregroundColor(Color.primary)
                }
            }
            .padding(.horizontal)
        }
        .padding()
        .padding(.bottom, UIApplication.shared.keyWindow?.safeAreaInsets.bottom)
        .background(Color(UIColor.secondarySystemBackground))
        .cornerRadius(24, corners: [.topLeft, .topRight])
        .animation(.interpolatingSpring(
            mass: 0.04,
            stiffness: 15.35,
            damping: 1.32,
            initialVelocity: 8.0
        ), value: showBreeds || showDetails)
    }
}
