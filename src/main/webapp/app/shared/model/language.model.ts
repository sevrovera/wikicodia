import { IArticle } from '../../shared/model/article.model';

export interface ILanguage {
  id?: number;
  nameLanguage?: string;
  version?: string;
  articleIds?: IArticle[];
}

export class Language implements ILanguage {
  constructor(public id?: number, public nameLanguage?: string, public version?: string, public articleIds?: IArticle[]) {}
}
