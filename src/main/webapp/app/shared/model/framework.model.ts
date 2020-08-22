import { IArticle } from '../../shared/model/article.model';

export interface IFramework {
  id?: number;
  nameFramework?: string;
  version?: string;
  articleIds?: IArticle[];
}

export class Framework implements IFramework {
  constructor(public id?: number, public nameFramework?: string, public version?: string, public articleIds?: IArticle[]) {}
}
