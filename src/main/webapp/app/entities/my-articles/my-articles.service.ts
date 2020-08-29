import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from '../../app.constants';
import { createRequestOption } from '../../shared/util/request-util';
import { IMyArticles } from '../../shared/model/my-articles.model';

type EntityResponseType = HttpResponse<IMyArticles>;
type EntityArrayResponseType = HttpResponse<IMyArticles[]>;

@Injectable({ providedIn: 'root' })
export class MyArticlesService {
  public resourceUrl = SERVER_API_URL + 'api/my-articles';

  constructor(protected http: HttpClient) {}

  create(myArticles: IMyArticles): Observable<EntityResponseType> {
    return this.http.post<IMyArticles>(this.resourceUrl, myArticles, { observe: 'response' });
  }

  update(myArticles: IMyArticles): Observable<EntityResponseType> {
    return this.http.put<IMyArticles>(this.resourceUrl, myArticles, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMyArticles>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMyArticles[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
