export interface IProvider {
  id?: number;
  name?: string;
}

export class Provider implements IProvider {
  constructor(public id?: number, public name?: string) {}
}

export function getProviderIdentifier(provider: IProvider): number | undefined {
  return provider.id;
}
