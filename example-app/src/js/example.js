import { Capacitor } from '@capacitor/core';
import { Yodo1Mas } from '@yodo1/mas-capacitor';

let coins = 0;

function setText(id, value) {
  const el = document.getElementById(id);
  if (!el) return;
  el.textContent = String(value);
}

function wireClick(id, handler) {
  const el = document.getElementById(id);
  if (!el) return;
  el.onclick = handler;
}

document.addEventListener('DOMContentLoaded', async () => {
  console.log('[MAS] DOM loaded');

  if (!Capacitor.isNativePlatform()) {
    console.log('[MAS] Not native platform');
    return;
  }

  // ─── SDK lifecycle ─────────────────────
  Yodo1Mas.addListener('initialized', () => {
    console.log('[MAS] SDK initialized');
  });

  Yodo1Mas.addListener('initFailed', (e) => {
    console.error('[MAS] SDK init failed', e);
  });

  // ─── App Open ──────────────────────────
  Yodo1Mas.addListener('appOpenLoaded', () => console.log('[MAS] AppOpen loaded'));
  Yodo1Mas.addListener('appOpenOpened', () => console.log('[MAS] AppOpen opened'));
  Yodo1Mas.addListener('appOpenClosed', async () => {
    console.log('[MAS] AppOpen closed → reload');
    await Yodo1Mas.loadAd({ adType: 'appopen' });
  });

  // ─── Interstitial ──────────────────────
  Yodo1Mas.addListener('interstitialLoaded', () => console.log('[MAS] Interstitial loaded'));
  Yodo1Mas.addListener('interstitialOpened', () => console.log('[MAS] Interstitial opened'));
  Yodo1Mas.addListener('interstitialClosed', async () => {
    console.log('[MAS] Interstitial closed → reload');
    await Yodo1Mas.loadAd({ adType: 'interstitial' });
  });

  // ─── Rewarded ──────────────────────────
  Yodo1Mas.addListener('rewardedLoaded', () => console.log('[MAS] Rewarded loaded'));
  Yodo1Mas.addListener('rewardedOpened', () => console.log('[MAS] Rewarded opened'));
  Yodo1Mas.addListener('rewardedEarned', () => {
    console.log('[MAS] 🎁 Reward earned');
    coins += 100;
    setText('coins', coins);
  });
  Yodo1Mas.addListener('rewardedClosed', async () => {
    console.log('[MAS] Rewarded closed → reload');
    await Yodo1Mas.loadAd({ adType: 'rewarded' });
  });

  // ─── Initialize ────────────────────────
  try {
    const result = await Yodo1Mas.initialize({
      appKey: 'm8D1U6ZnYc',
      gdpr: true,
      coppa: false,
      ccpa: false,
      autoDelayIfLoadFail: true,
    });

    console.log('[MAS] initialize result:', result);
    if (!result.initialized) return;
  } catch (e) {
    console.error('[MAS] initialize failed', e);
    return;
  }

  // ─── Load ads ──────────────────────────
  await Yodo1Mas.loadAd({ adType: 'appopen' });
  await Yodo1Mas.loadAd({ adType: 'interstitial' });
  await Yodo1Mas.loadAd({ adType: 'rewarded' });

  // ─── Buttons ───────────────────────────
  wireClick('btnAppOpen', () =>
    Yodo1Mas.showAd({ adType: 'appopen', placementId: 'default' })
  );

  wireClick('btnInterstitial', () =>
    Yodo1Mas.showAd({ adType: 'interstitial', placementId: 'default' })
  );

  wireClick('btnRewarded', () =>
    Yodo1Mas.showAd({ adType: 'rewarded', placementId: 'default' })
  );

  setText('coins', coins);
});
