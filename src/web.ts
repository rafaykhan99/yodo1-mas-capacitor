import { WebPlugin } from '@capacitor/core';
import type {
  ActionResult,
  InitializeOptions,
  InitializeResult,
  IsAdLoadedOptions,
  IsAdLoadedResult,
  LoadAdOptions,
  ShowAdOptions,
  Yodo1MasPlugin,
} from './definitions';

export class Yodo1MasWeb extends WebPlugin implements Yodo1MasPlugin {
  private throwUnavailable(): never {
    throw new Error('Yodo1Mas is not available on Web. Use Android or iOS.');
  }

  async initialize(_options: InitializeOptions): Promise<InitializeResult> {
    return this.throwUnavailable();
  }

  async loadAd(_options: LoadAdOptions): Promise<ActionResult> {
    return this.throwUnavailable();
  }

  async showAd(_options: ShowAdOptions): Promise<ActionResult> {
    return this.throwUnavailable();
  }

  async isAdLoaded(_options: IsAdLoadedOptions): Promise<IsAdLoadedResult> {
    return this.throwUnavailable();
  }
}
