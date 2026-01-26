export interface Yodo1MasPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
