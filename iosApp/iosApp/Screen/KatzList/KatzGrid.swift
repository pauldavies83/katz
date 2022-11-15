import SwiftUI

struct KatzGrid: View {
    let katz: [String]
    
    var body: some View {
        let columnWidth = (UIScreen.main.bounds.width) / 2
        let columns = [GridItem(.fixed(columnWidth)), GridItem(.fixed(columnWidth))]
        
        ScrollView {
            LazyVGrid(columns: columns, alignment: .center) {
                ForEach(katz, id: \.self) { imageUrl in
                    ZStack {
                        AsyncImage(
                            url: URL(string: imageUrl),
                            scale: 1,
                            content: { image in
                                image.centerCropped()
                            },
                            placeholder: {
                                Image("Pokeball")
                                    .resizable()
                                    .foregroundColor(Color.gray)
                                    .frame(width: 64, height: 64)
                            }
                        )
                    }
                    .frame(width: columnWidth, height: 128)
                }
            }
        }
    }
}
