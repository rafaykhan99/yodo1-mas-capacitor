export type AdType = 'appopen' | 'interstitial' | 'rewarded';

/* ───────────────────────────
 * Initialization
 * ─────────────────────────── */

export interface InitializeOptions {
  appKey: string;
  coppa?: boolean;
  gdpr?: boolean;
  ccpa?: boolean;
  autoDelayIfLoadFail?: boolean;
}

export interface InitializeResult {
  initialized: boolean;
  errorCode?: string;
  message?: string;
}

/* ───────────────────────────
 * Ad actions
 * ─────────────────────────── */

export interface LoadAdOptions {
  adType: AdType;
}

export interface ShowAdOptions {
  adType: AdType;
  placementId?: string;
}

export interface ActionResult {
  ok: boolean;
  action: 'loadAd' | 'showAd';
  adType: AdType;
  placementId?: string;
}

/* ───────────────────────────
 * State checks
 * ─────────────────────────── */

export interface IsAdLoadedOptions {
  adType: AdType;
}

export interface IsAdLoadedResult {
  isLoaded: boolean;
  adType: AdType;
}

/* ───────────────────────────
 * Plugin API
 * ─────────────────────────── */

export interface Yodo1MasPlugin {
  initialize(options: InitializeOptions): Promise<InitializeResult>;

  loadAd(options: LoadAdOptions): Promise<ActionResult>;

  showAd(options: ShowAdOptions): Promise<ActionResult>;

  isAdLoaded(options: IsAdLoadedOptions): Promise<IsAdLoadedResult>;
}
