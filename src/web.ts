import { WebPlugin } from '@capacitor/core';

import type { Yodo1MasPlugin } from './definitions';

export class Yodo1MasWeb extends WebPlugin implements Yodo1MasPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
