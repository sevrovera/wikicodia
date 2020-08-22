export interface ICategory {
  id?: number;
  nameCategory?: string;
}

export class Category implements ICategory {
  constructor(public id?: number, public nameCategory?: string) {}
}
