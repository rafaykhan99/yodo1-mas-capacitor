import Foundation
import Capacitor

@objc(Yodo1MasPlugin)
public class Yodo1MasPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "Yodo1MasPlugin"
    public let jsName = "Yodo1Mas"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "initialize", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "loadAd", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "showAd", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "isAdLoaded", returnType: CAPPluginReturnPromise),
    ]

    private let implementation = Yodo1Mas()

    public override func load() {
        implementation.setEventEmitter { [weak self] eventName, payload in
            self?.notifyListeners(eventName, data: payload)
        }
        implementation.attachDelegates()
    }

    @objc func initialize(_ call: CAPPluginCall) {
        let appKey = call.getString("appKey") ?? ""
        if appKey.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
            call.reject("Missing required field: appKey")
            return
        }

        let coppa = call.getBool("coppa") ?? false
        let gdpr = call.getBool("gdpr") ?? true
        let ccpa = call.getBool("ccpa") ?? false
        let autoDelayIfLoadFail = call.getBool("autoDelayIfLoadFail") ?? true

        implementation.initialize(
            appKey: appKey,
            coppa: coppa,
            gdpr: gdpr,
            ccpa: ccpa,
            autoDelayIfLoadFail: autoDelayIfLoadFail
        ) { result in
            call.resolve(result)
        }
    }

    @objc func loadAd(_ call: CAPPluginCall) {
        let adType = call.getString("adType") ?? ""
        if adType.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
            call.reject("Missing required field: adType")
            return
        }

        implementation.loadAd(adType: adType) { result in
            call.resolve(result)
        }
    }

    @objc func showAd(_ call: CAPPluginCall) {
        let adType = call.getString("adType") ?? ""
        if adType.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
            call.reject("Missing required field: adType")
            return
        }

        let placementId = call.getString("placementId") ?? "default"

        implementation.showAd(adType: adType, placementId: placementId) { result in
            call.resolve(result)
        }
    }

    @objc func isAdLoaded(_ call: CAPPluginCall) {
        let adType = call.getString("adType") ?? ""
        if adType.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
            call.reject("Missing required field: adType")
            return
        }

        implementation.isAdLoaded(adType: adType) { result in
            call.resolve(result)
        }
    }
}
