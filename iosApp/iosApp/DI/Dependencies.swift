import shared

typealias Dependencies = Koin_coreKoin

let dependencies: Dependencies = {
    KoinKt.startKoin().koin
}()

extension Dependencies {
    
    func get<Dependency>() -> Dependency where Dependency: AnyObject {
        get(objCClass: Dependency.self) as! Dependency
    }
    
    func get<Dependency>(parameters: Any?...) -> Dependency where Dependency: AnyObject {
        return get(objCClass: Dependency.self, parameters: parameters as [Any]) as! Dependency
    }
}

/// A property wrapper that immediately injects a dependency.
@propertyWrapper
struct Injected<Dependency> where Dependency: AnyObject {
    let wrappedValue: Dependency = dependencies.get()
    init() {}
}
