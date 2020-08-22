import { IUser } from '../../core/user/user.model';
import { IArticle } from '../../shared/model/article.model';

export interface IVote {
  id?: number;
  isLiked?: boolean;
  comment?: string;
  author?: IUser;
  articleIds?: IArticle[];
}

export class Vote implements IVote {
  constructor(
    public id?: number,
    public isLiked?: boolean,
    public comment?: string,
    public author?: IUser,
    public articleIds?: IArticle[]
  ) {
    this.isLiked = this.isLiked || false;
  }
}
