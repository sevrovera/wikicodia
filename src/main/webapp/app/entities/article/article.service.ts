import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from '../../shared/constants/input.constants';
import { SERVER_API_URL } from '../../app.constants';
import { createRequestOption } from '../../shared/util/request-util';
import { IArticle } from '../../shared/model/article.model';

import { Global } from '../../global';

type EntityResponseType = HttpResponse<IArticle>;
type EntityArrayResponseType = HttpResponse<IArticle[]>;

@Injectable({ providedIn: 'root' })
export class ArticleService {
  public resourceUrl = SERVER_API_URL + 'api/articles';
  public resourceUrlMyArticles = SERVER_API_URL + 'api/articles/my-articles/user';
  public resourceUrlAwaitingValidation = SERVER_API_URL + 'api/articles/awaiting validation';
  global: Global;

  constructor(protected http: HttpClient, global: Global) {
    this.global = global;
  }

  create(article: IArticle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(article);
    return this.http
      .post<IArticle>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(article: IArticle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(article);
    return this.http
      .put<IArticle>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IArticle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    alert(this.global.menuItemClicked);
    if (this.global.menuItemClicked === 'Library') {
      return this.http
        .get<IArticle[]>(this.resourceUrl, { params: options, observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    } else if (this.global.menuItemClicked === 'MyArticles') {
      return this.http
        .get<IArticle[]>(this.resourceUrlMyArticles, { params: options, observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    } else if (this.global.menuItemClicked === 'AwaitingValidation') {
      return this.http
        .get<IArticle[]>(this.resourceUrlMyArticles, { params: options, observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(article: IArticle): IArticle {
    const copy: IArticle = Object.assign({}, article, {
      creationDate: article.creationDate && article.creationDate.isValid() ? article.creationDate.format(DATE_FORMAT) : undefined,
      lastEditDate: article.lastEditDate && article.lastEditDate.isValid() ? article.lastEditDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? moment(res.body.creationDate) : undefined;
      res.body.lastEditDate = res.body.lastEditDate ? moment(res.body.lastEditDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((article: IArticle) => {
        article.creationDate = article.creationDate ? moment(article.creationDate) : undefined;
        article.lastEditDate = article.lastEditDate ? moment(article.lastEditDate) : undefined;
      });
    }
    return res;
  }
}
