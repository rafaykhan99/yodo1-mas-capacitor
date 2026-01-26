import Foundation

@objc public class Yodo1Mas: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
