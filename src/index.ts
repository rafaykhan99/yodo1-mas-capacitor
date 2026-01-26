import { registerPlugin } from '@capacitor/core';

import type { Yodo1MasPlugin } from './definitions';

const Yodo1Mas = registerPlugin<Yodo1MasPlugin>('Yodo1Mas', {
  web: () => import('./web').then((m) => new m.Yodo1MasWeb()),
});

export * from './definitions';
export { Yodo1Mas };
