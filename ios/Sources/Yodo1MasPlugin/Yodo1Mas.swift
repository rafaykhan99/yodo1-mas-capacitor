import Foundation
import Yodo1MasCore

@objc public class Yodo1Mas: NSObject,
                             Yodo1MasAppOpenAdDelegate,
                             Yodo1MasInterstitialDelegate,
                             Yodo1MasRewardDelegate {

    private var isInitialized = false
    private var initCallbackFired = false

    private var emitEvent: ((String, [String: Any]) -> Void)?

    private var lastPlacementIdByAdType: [String: String] = [
        "appopen": "default",
        "interstitial": "default",
        "rewarded": "default"
    ]

    public func setEventEmitter(_ emitter: @escaping (String, [String: Any]) -> Void) {
        emitEvent = emitter
    }

    public func attachDelegates() {
        DispatchQueue.main.async {
            Yodo1MasCore.Yodo1MasAppOpenAd.sharedInstance().adDelegate = self
            Yodo1MasCore.Yodo1MasInterstitialAd.sharedInstance().adDelegate = self
            Yodo1MasCore.Yodo1MasRewardAd.sharedInstance().adDelegate = self
        }
    }

    public func initialize(
        appKey: String,
        coppa: Bool,
        gdpr: Bool,
        ccpa: Bool,
        autoDelayIfLoadFail: Bool,
        completion: @escaping ([String: Any]) -> Void
    ) {
        DispatchQueue.main.async {
            Yodo1MasCore.Yodo1Mas.sharedInstance().isCOPPAAgeRestricted = coppa
            Yodo1MasCore.Yodo1Mas.sharedInstance().isGDPRUserConsent = gdpr
            Yodo1MasCore.Yodo1Mas.sharedInstance().isCCPADoNotSell = ccpa

            Yodo1MasAppOpenAd.sharedInstance().autoDelayIfLoadFail = autoDelayIfLoadFail
            Yodo1MasRewardAd.sharedInstance().autoDelayIfLoadFail = autoDelayIfLoadFail
            Yodo1MasInterstitialAd.sharedInstance().autoDelayIfLoadFail = autoDelayIfLoadFail

            if self.isInitialized {
                self.emitEvent?("initialized", [:])
                completion(["initialized": true])
                return
            }

            self.initCallbackFired = false

            Yodo1MasCore.Yodo1Mas.sharedInstance().initMas(withAppKey: appKey) {
                if self.initCallbackFired { return }
                self.initCallbackFired = true
                self.isInitialized = true

                self.emitEvent?("initialized", [:])
                completion(["initialized": true])
            } fail: { error in
                if self.initCallbackFired { return }
                self.initCallbackFired = true
                self.isInitialized = false

                let payload: [String: Any] = [
                    "errorCode": "\(error.code)",
                    "message": error.description ?? "MAS init failed"
                ]
                self.emitEvent?("initFailed", payload)
                completion(payload.merging(["initialized": false]) { (_, old) in old })
            }
        }
    }

    public func loadAd(adType: String, completion: @escaping ([String: Any]) -> Void) {
        guard isInitialized else {
            completion(["ok": false, "action": "loadAd", "adType": adType, "message": "MAS is not initialized"])
            return
        }

        DispatchQueue.main.async {
            switch adType {
            case "appopen":
                Yodo1MasAppOpenAd.sharedInstance().load()
            case "interstitial":
                Yodo1MasInterstitialAd.sharedInstance().load()
            case "rewarded":
                Yodo1MasRewardAd.sharedInstance().load()
            default:
                completion(["ok": false, "action": "loadAd", "adType": adType, "message": "Unsupported adType"])
                return
            }

            completion(["ok": true, "action": "loadAd", "adType": adType])
        }
    }

    public func showAd(adType: String, placementId: String, completion: @escaping ([String: Any]) -> Void) {
        guard isInitialized else {
            completion(["ok": false, "action": "showAd", "adType": adType, "message": "MAS is not initialized"])
            return
        }

        let trimmed = placementId.trimmingCharacters(in: .whitespacesAndNewlines)
        let safePlacementId = trimmed.isEmpty ? "default" : trimmed
        lastPlacementIdByAdType[adType] = safePlacementId

        DispatchQueue.main.async {
            switch adType {
            case "appopen":
                if Yodo1MasAppOpenAd.sharedInstance().isLoaded() {
                    Yodo1MasAppOpenAd.sharedInstance().show(withPlacement: safePlacementId)
                } else {
                    self.emitEvent?("appOpenOpenFailed", ["placementId": safePlacementId, "message": "AppOpen ad not loaded"])
                }

            case "interstitial":
                if Yodo1MasInterstitialAd.sharedInstance().isLoaded() {
                    Yodo1MasInterstitialAd.sharedInstance().show(withPlacement: safePlacementId)
                } else {
                    self.emitEvent?("interstitialOpenFailed", ["placementId": safePlacementId, "message": "Interstitial ad not loaded"])
                }

            case "rewarded":
                if Yodo1MasRewardAd.sharedInstance().isLoaded() {
                    Yodo1MasRewardAd.sharedInstance().show(withPlacement: safePlacementId)
                } else {
                    self.emitEvent?("rewardedOpenFailed", ["placementId": safePlacementId, "message": "Rewarded ad not loaded"])
                }

            default:
                completion(["ok": false, "action": "showAd", "adType": adType, "placementId": safePlacementId, "message": "Unsupported adType"])
                return
            }

            completion(["ok": true, "action": "showAd", "adType": adType, "placementId": safePlacementId])
        }
    }

    public func isAdLoaded(adType: String, completion: @escaping ([String: Any]) -> Void) {
        guard isInitialized else {
            completion(["isLoaded": false, "adType": adType])
            return
        }

        DispatchQueue.main.async {
            let loaded: Bool
            switch adType {
            case "appopen":
                loaded = Yodo1MasAppOpenAd.sharedInstance().isLoaded()
            case "interstitial":
                loaded = Yodo1MasInterstitialAd.sharedInstance().isLoaded()
            case "rewarded":
                loaded = Yodo1MasRewardAd.sharedInstance().isLoaded()
            default:
                completion(["isLoaded": false, "adType": adType])
                return
            }

            completion(["isLoaded": loaded, "adType": adType])
        }
    }

    private func placementId(for adType: String) -> String {
        return lastPlacementIdByAdType[adType] ?? "default"
    }

    // MARK: - AppOpen events

    public func onAppOpenAdLoaded(_ ad: Yodo1MasAppOpenAd) {
        emitEvent?("appOpenLoaded", ["placementId": placementId(for: "appopen")])
    }

    public func onAppOpenAdFailedToLoad(_ ad: Yodo1MasAppOpenAd, _ error: Yodo1MasError) {
        emitEvent?("appOpenLoadFailed", ["placementId": placementId(for: "appopen"), "errorCode": "\(error.code)", "message": error.description ?? ""])
    }

    public func onAppOpenAdOpened(_ ad: Yodo1MasAppOpenAd) {
        emitEvent?("appOpenOpened", ["placementId": placementId(for: "appopen")])
    }

    public func onAppOpenAdFailedToOpen(_ ad: Yodo1MasAppOpenAd, _ error: Yodo1MasError) {
        emitEvent?("appOpenOpenFailed", ["placementId": placementId(for: "appopen"), "errorCode": "\(error.code)", "message": error.description ?? ""])
    }

    public func onAppOpenAdClosed(_ ad: Yodo1MasAppOpenAd) {
        emitEvent?("appOpenClosed", ["placementId": placementId(for: "appopen")])
    }

    // MARK: - Interstitial events

    public func onInterstitialAdLoaded(_ ad: Yodo1MasInterstitialAd) {
        emitEvent?("interstitialLoaded", ["placementId": placementId(for: "interstitial")])
    }

    public func onInterstitialAdFailedToLoad(_ ad: Yodo1MasInterstitialAd, _ error: Yodo1MasError) {
        emitEvent?("interstitialLoadFailed", ["placementId": placementId(for: "interstitial"), "errorCode": "\(error.code)", "message": error.description ?? ""])
    }

    public func onInterstitialAdOpened(_ ad: Yodo1MasInterstitialAd) {
        emitEvent?("interstitialOpened", ["placementId": placementId(for: "interstitial")])
    }

    public func onInterstitialAdFailedToOpen(_ ad: Yodo1MasInterstitialAd, _ error: Yodo1MasError) {
        emitEvent?("interstitialOpenFailed", ["placementId": placementId(for: "interstitial"), "errorCode": "\(error.code)", "message": error.description ?? ""])
    }

    public func onInterstitialAdClosed(_ ad: Yodo1MasInterstitialAd) {
        emitEvent?("interstitialClosed", ["placementId": placementId(for: "interstitial")])
    }

    // MARK: - Rewarded events

    public func onRewardAdLoaded(_ ad: Yodo1MasRewardAd) {
        emitEvent?("rewardedLoaded", ["placementId": placementId(for: "rewarded")])
    }

    public func onRewardAdFailedToLoad(_ ad: Yodo1MasRewardAd, _ error: Yodo1MasError) {
        emitEvent?("rewardedLoadFailed", ["placementId": placementId(for: "rewarded"), "errorCode": "\(error.code)", "message": error.description ?? ""])
    }

    public func onRewardAdOpened(_ ad: Yodo1MasRewardAd) {
        emitEvent?("rewardedOpened", ["placementId": placementId(for: "rewarded")])
    }

    public func onRewardAdFailedToOpen(_ ad: Yodo1MasRewardAd, _ error: Yodo1MasError) {
        emitEvent?("rewardedOpenFailed", ["placementId": placementId(for: "rewarded"), "errorCode": "\(error.code)", "message": error.description ?? ""])
    }

    public func onRewardAdClosed(_ ad: Yodo1MasRewardAd) {
        emitEvent?("rewardedClosed", ["placementId": placementId(for: "rewarded")])
    }

    public func onRewardAdEarned(_ ad: Yodo1MasRewardAd) {
        emitEvent?("rewardedEarned", ["placementId": placementId(for: "rewarded")])
    }
}
