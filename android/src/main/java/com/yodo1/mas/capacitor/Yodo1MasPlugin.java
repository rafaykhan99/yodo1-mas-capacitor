package com.yodo1.mas.capacitor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.Yodo1MasSdkConfiguration;
import com.yodo1.mas.appopenad.Yodo1MasAppOpenAd;
import com.yodo1.mas.appopenad.Yodo1MasAppOpenAdListener;
import com.yodo1.mas.error.Yodo1MasError;
import com.yodo1.mas.interstitial.Yodo1MasInterstitialAd;
import com.yodo1.mas.interstitial.Yodo1MasInterstitialAdListener;
import com.yodo1.mas.reward.Yodo1MasRewardAd;
import com.yodo1.mas.reward.Yodo1MasRewardAdListener;

import java.util.HashMap;
import java.util.Map;

@CapacitorPlugin(name = "Yodo1Mas")
public final class Yodo1MasPlugin extends Plugin {

    private static final String LOG_TAG = "yodo1";

    private volatile boolean isInitialized = false;

    private final Map<String, String> lastPlacementIdByAdType = new HashMap<>();

    private void emit(String eventName, JSObject payload) {
        notifyListeners(eventName, payload);
    }

    private void emitGenericAdEvent(String adType, String eventName, JSObject payload) {
        JSObject genericPayload = payload == null ? new JSObject() : payload;
        genericPayload.put("adType", adType);
        genericPayload.put("event", eventName);
        notifyListeners("adEvent", genericPayload);
    }

    private JSObject basePayload(String adType) {
        JSObject obj = new JSObject();
        String placementId = lastPlacementIdByAdType.get(adType);
        if (placementId != null)
            obj.put("placementId", placementId);
        return obj;
    }

    private JSObject errorPayload(String adType, Yodo1MasError error) {
        JSObject obj = basePayload(adType);
        if (error != null) {
            obj.put("message", error.getMessage());
            try {
                obj.put("errorCode", String.valueOf(error.getCode()));
            } catch (Throwable ignored) {
            }
        }
        return obj;
    }

    private boolean ensureInitialized(PluginCall call) {
        if (!isInitialized) {
            call.reject("MAS not initialized. Call initialize() and wait for success before using ads.");
            return false;
        }
        return true;
    }

    private void ensureListenersInitialized() {
        Yodo1MasAppOpenAd.getInstance().setAdListener(new Yodo1MasAppOpenAdListener() {
            @Override
            public void onAppOpenAdLoaded(Yodo1MasAppOpenAd ad) {
                JSObject payload = basePayload("appopen");
                Log.d(LOG_TAG, "appOpenLoaded " + payload.toString());
                emit("appOpenLoaded", payload);
                emitGenericAdEvent("appopen", "loaded", payload);
            }

            @Override
            public void onAppOpenAdFailedToLoad(Yodo1MasAppOpenAd ad, @NonNull Yodo1MasError error) {
                JSObject payload = errorPayload("appopen", error);
                Log.d(LOG_TAG, "appOpenLoadFailed " + payload.toString());
                emit("appOpenLoadFailed", payload);
                emitGenericAdEvent("appopen", "loadFailed", payload);
            }

            @Override
            public void onAppOpenAdOpened(Yodo1MasAppOpenAd ad) {
                JSObject payload = basePayload("appopen");
                Log.d(LOG_TAG, "appOpenOpened " + payload.toString());
                emit("appOpenOpened", payload);
                emitGenericAdEvent("appopen", "opened", payload);
            }

            @Override
            public void onAppOpenAdFailedToOpen(Yodo1MasAppOpenAd ad, @NonNull Yodo1MasError error) {
                JSObject payload = errorPayload("appopen", error);
                Log.d(LOG_TAG, "appOpenOpenFailed " + payload.toString());
                emit("appOpenOpenFailed", payload);
                emitGenericAdEvent("appopen", "openFailed", payload);
            }

            @Override
            public void onAppOpenAdClosed(Yodo1MasAppOpenAd ad) {
                JSObject payload = basePayload("appopen");
                Log.d(LOG_TAG, "appOpenClosed " + payload.toString());
                emit("appOpenClosed", payload);
                emitGenericAdEvent("appopen", "closed", payload);
            }
        });

        Yodo1MasInterstitialAd.getInstance().setAdListener(new Yodo1MasInterstitialAdListener() {
            @Override
            public void onInterstitialAdLoaded(Yodo1MasInterstitialAd ad) {
                JSObject payload = basePayload("interstitial");
                Log.d(LOG_TAG, "interstitialLoaded " + payload.toString());
                emit("interstitialLoaded", payload);
                emitGenericAdEvent("interstitial", "loaded", payload);
            }

            @Override
            public void onInterstitialAdFailedToLoad(Yodo1MasInterstitialAd ad, @NonNull Yodo1MasError error) {
                JSObject payload = errorPayload("interstitial", error);
                Log.d(LOG_TAG, "interstitialLoadFailed " + payload.toString());
                emit("interstitialLoadFailed", payload);
                emitGenericAdEvent("interstitial", "loadFailed", payload);
            }

            @Override
            public void onInterstitialAdOpened(Yodo1MasInterstitialAd ad) {
                JSObject payload = basePayload("interstitial");
                Log.d(LOG_TAG, "interstitialOpened " + payload.toString());
                emit("interstitialOpened", payload);
                emitGenericAdEvent("interstitial", "opened", payload);
            }

            @Override
            public void onInterstitialAdFailedToOpen(Yodo1MasInterstitialAd ad, @NonNull Yodo1MasError error) {
                JSObject payload = errorPayload("interstitial", error);
                Log.d(LOG_TAG, "interstitialOpenFailed " + payload.toString());
                emit("interstitialOpenFailed", payload);
                emitGenericAdEvent("interstitial", "openFailed", payload);
            }

            @Override
            public void onInterstitialAdClosed(Yodo1MasInterstitialAd ad) {
                JSObject payload = basePayload("interstitial");
                Log.d(LOG_TAG, "interstitialClosed " + payload.toString());
                emit("interstitialClosed", payload);
                emitGenericAdEvent("interstitial", "closed", payload);
            }
        });

        Yodo1MasRewardAd.getInstance().setAdListener(new Yodo1MasRewardAdListener() {
            @Override
            public void onRewardAdLoaded(Yodo1MasRewardAd ad) {
                JSObject payload = basePayload("rewarded");
                Log.d(LOG_TAG, "rewardedLoaded " + payload.toString());
                emit("rewardedLoaded", payload);
                emitGenericAdEvent("rewarded", "loaded", payload);
            }

            @Override
            public void onRewardAdFailedToLoad(Yodo1MasRewardAd ad, @NonNull Yodo1MasError error) {
                JSObject payload = errorPayload("rewarded", error);
                Log.d(LOG_TAG, "rewardedLoadFailed " + payload.toString());
                emit("rewardedLoadFailed", payload);
                emitGenericAdEvent("rewarded", "loadFailed", payload);
            }

            @Override
            public void onRewardAdOpened(Yodo1MasRewardAd ad) {
                JSObject payload = basePayload("rewarded");
                Log.d(LOG_TAG, "rewardedOpened " + payload.toString());
                emit("rewardedOpened", payload);
                emitGenericAdEvent("rewarded", "opened", payload);
            }

            @Override
            public void onRewardAdFailedToOpen(Yodo1MasRewardAd ad, @NonNull Yodo1MasError error) {
                JSObject payload = errorPayload("rewarded", error);
                Log.d(LOG_TAG, "rewardedOpenFailed " + payload.toString());
                emit("rewardedOpenFailed", payload);
                emitGenericAdEvent("rewarded", "openFailed", payload);
            }

            @Override
            public void onRewardAdClosed(Yodo1MasRewardAd ad) {
                JSObject payload = basePayload("rewarded");
                Log.d(LOG_TAG, "rewardedClosed " + payload.toString());
                emit("rewardedClosed", payload);
                emitGenericAdEvent("rewarded", "closed", payload);
            }

            @Override
            public void onRewardAdEarned(Yodo1MasRewardAd ad) {
                JSObject payload = basePayload("rewarded");
                Log.d(LOG_TAG, "rewardedEarned " + payload.toString());
                emit("rewardedEarned", payload);
                emitGenericAdEvent("rewarded", "rewardEarned", payload);
            }
        });
    }

    @PluginMethod
    public void initialize(PluginCall call) {
        String appKey = call.getString("appKey");
        boolean coppa = call.getBoolean("coppa", false);
        boolean gdpr = call.getBoolean("gdpr", true);
        boolean ccpa = call.getBoolean("ccpa", false);
        boolean autoDelayIfLoadFail = call.getBoolean("autoDelayIfLoadFail", true);

        if (appKey == null || appKey.trim().isEmpty()) {
            call.reject("Missing required field: appKey");
            return;
        }

        Log.d(LOG_TAG, "initialize called appKey=" + appKey);

        Yodo1Mas.getInstance().setCOPPA(coppa);
        Yodo1Mas.getInstance().setGDPR(gdpr);
        Yodo1Mas.getInstance().setCCPA(ccpa);

        Yodo1MasAppOpenAd.getInstance().autoDelayIfLoadFail = autoDelayIfLoadFail;
        Yodo1MasInterstitialAd.getInstance().autoDelayIfLoadFail = autoDelayIfLoadFail;
        Yodo1MasRewardAd.getInstance().autoDelayIfLoadFail = autoDelayIfLoadFail;

        ensureListenersInitialized();

        Yodo1Mas.getInstance().initMas(getActivity(), appKey, new Yodo1Mas.InitListener() {
            @Override
            public void onMasInitSuccessful() {
                isInitialized = true;
                Log.d(LOG_TAG, "MAS init successful (no config)");

                JSObject payload = new JSObject();
                payload.put("initialized", true);

                emit("initialized", payload);
                emitGenericAdEvent("sdk", "initialized", payload);

                call.resolve(payload);
            }

            @Override
            public void onMasInitSuccessful(@NonNull Yodo1MasSdkConfiguration configuration) {
                isInitialized = true;
                Log.d(LOG_TAG, "MAS init successful (with config)");

                JSObject payload = new JSObject();
                payload.put("initialized", true);

                emit("initialized", payload);
                emitGenericAdEvent("sdk", "initialized", payload);

                call.resolve(payload);
            }

            @Override
            public void onMasInitFailed(@NonNull Yodo1MasError error) {
                isInitialized = false;

                JSObject payload = new JSObject();
                payload.put("initialized", false);
                payload.put("message", error.getMessage());
                try {
                    payload.put("errorCode", String.valueOf(error.getCode()));
                } catch (Throwable ignored) {
                }

                Log.d(LOG_TAG, "MAS init failed " + payload.toString());

                emit("initFailed", payload);
                emitGenericAdEvent("sdk", "initFailed", payload);

                call.reject("MAS init failed: " + error.getMessage());
            }
        });
    }

    @PluginMethod
    public void loadAd(PluginCall call) {
        if (!ensureInitialized(call))
            return;

        String adType = call.getString("adType");
        if (adType == null || adType.trim().isEmpty()) {
            call.reject("Missing required field: adType");
            return;
        }

        Log.d(LOG_TAG, "loadAd called adType=" + adType + " isInitialized=" + isInitialized);

        final String finalAdType = adType;

        getActivity().runOnUiThread(() -> {
            try {
                switch (finalAdType) {
                    case "appopen":
                        Yodo1MasAppOpenAd.getInstance().loadAd(getActivity());
                        break;
                    case "interstitial":
                        Yodo1MasInterstitialAd.getInstance().loadAd(getActivity());
                        break;
                    case "rewarded":
                        Yodo1MasRewardAd.getInstance().loadAd(getActivity());
                        break;
                    default:
                        call.reject("Unsupported adType: " + finalAdType);
                        return;
                }

                JSObject result = new JSObject();
                result.put("ok", true);
                result.put("action", "loadAd");
                result.put("adType", finalAdType);
                call.resolve(result);
            } catch (Exception e) {
                call.reject("loadAd failed: " + e.getMessage());
            }
        });
    }

    @PluginMethod
    public void showAd(PluginCall call) {
        if (!ensureInitialized(call))
            return;

        String adType = call.getString("adType");
        String placementId = call.getString("placementId", "default");

        if (adType == null || adType.trim().isEmpty()) {
            call.reject("Missing required field: adType");
            return;
        }

        if (placementId == null || placementId.trim().isEmpty()) {
            placementId = "default";
        }

        lastPlacementIdByAdType.put(adType, placementId);

        final String finalAdType = adType;
        final String finalPlacementId = placementId;

        Log.d(LOG_TAG, "showAd called adType=" + finalAdType + " placementId=" + finalPlacementId + " isInitialized="
                + isInitialized);

        getActivity().runOnUiThread(() -> {
            try {
                switch (finalAdType) {
                    case "appopen":
                        Yodo1MasAppOpenAd.getInstance().showAd(getActivity(), finalPlacementId);
                        break;
                    case "interstitial":
                        Yodo1MasInterstitialAd.getInstance().showAd(getActivity(), finalPlacementId);
                        break;
                    case "rewarded":
                        Yodo1MasRewardAd.getInstance().showAd(getActivity(), finalPlacementId);
                        break;
                    default:
                        call.reject("Unsupported adType: " + finalAdType);
                        return;
                }

                JSObject result = new JSObject();
                result.put("ok", true);
                result.put("action", "showAd");
                result.put("adType", finalAdType);
                result.put("placementId", finalPlacementId);
                call.resolve(result);
            } catch (Exception e) {
                call.reject("showAd failed: " + e.getMessage());
            }
        });
    }

    @PluginMethod
    public void isAdLoaded(PluginCall call) {
        if (!ensureInitialized(call))
            return;

        String adType = call.getString("adType");
        if (adType == null || adType.trim().isEmpty()) {
            call.reject("Missing required field: adType");
            return;
        }

        boolean isLoaded;

        switch (adType) {
            case "appopen":
                isLoaded = Yodo1MasAppOpenAd.getInstance().isLoaded();
                break;
            case "interstitial":
                isLoaded = Yodo1MasInterstitialAd.getInstance().isLoaded();
                break;
            case "rewarded":
                isLoaded = Yodo1MasRewardAd.getInstance().isLoaded();
                break;
            default:
                call.reject("Unsupported adType: " + adType);
                return;
        }

        JSObject result = new JSObject();
        result.put("isLoaded", isLoaded);
        result.put("adType", adType);
        call.resolve(result);
    }
}
