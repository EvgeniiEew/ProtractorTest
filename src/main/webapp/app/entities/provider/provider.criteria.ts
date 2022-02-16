import * as dayjs from 'dayjs';

export interface IProviderCriteria {
  id?: number;
  name?: string;
}

export class ProviderCriteria implements IProviderCriteria {
  constructor(public id?: number, public name?: string) {}
}
