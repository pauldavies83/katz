import shared
import SwiftUI

struct KatzListBottomBar: View {
    @State private var showBreeds = false
    @State private var showInfo = false
        
    let title: String?
    let breeds: [BreedDrawerItem]?
    
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
            } else if (showInfo) {
                VStack {}
            }
            HStack {
                Button(action: { showBreeds = !showBreeds }) {
                    Image(systemName: "arrow.up.circle")
                        .foregroundColor(Color.primary)
                }
                Spacer()
                Text(title ?? "Katz")
                Spacer()
                Button(action: { showInfo = !showInfo }) {
                    Image(systemName: "info.circle.fill")
                        .foregroundColor(Color.primary)
                }
            }
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
        ), value: showBreeds)
    }
}
