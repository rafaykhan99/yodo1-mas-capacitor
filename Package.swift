// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "Yodo1MasCapacitor",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "Yodo1MasCapacitor",
            targets: ["Yodo1MasPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "Yodo1MasPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/Yodo1MasPlugin"),
        .testTarget(
            name: "Yodo1MasPluginTests",
            dependencies: ["Yodo1MasPlugin"],
            path: "ios/Tests/Yodo1MasPluginTests")
    ]
)