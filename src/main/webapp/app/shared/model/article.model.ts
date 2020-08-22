import { Moment } from 'moment';
import { ICategory } from '../../shared/model/category.model';
import { IUser } from '../../core/user/user.model';
import { IVote } from '../../shared/model/vote.model';
import { ILanguage } from '../../shared/model/language.model';
import { IFramework } from '../../shared/model/framework.model';
import { ArticleType } from '../../shared/model/enumerations/article-type.model';

export interface IArticle {
  id?: number;
  title?: string;
  creationDate?: Moment;
  lastEditDate?: Moment;
  description?: string;
  isPublished?: boolean;
  isValidated?: boolean;
  isPromoted?: boolean;
  articleType?: ArticleType;
  category?: ICategory;
  author?: IUser;
  ratings?: IVote[];
  languages?: ILanguage[];
  frameworks?: IFramework[];
}

export class Article implements IArticle {
  constructor(
    public id?: number,
    public title?: string,
    public creationDate?: Moment,
    public lastEditDate?: Moment,
    public description?: string,
    public isPublished?: boolean,
    public isValidated?: boolean,
    public isPromoted?: boolean,
    public articleType?: ArticleType,
    public category?: ICategory,
    public author?: IUser,
    public ratings?: IVote[],
    public languages?: ILanguage[],
    public frameworks?: IFramework[]
  ) {
    this.isPublished = this.isPublished || false;
    this.isValidated = this.isValidated || false;
    this.isPromoted = this.isPromoted || false;
  }
}
