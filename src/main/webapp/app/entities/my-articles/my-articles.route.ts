import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMyArticles, MyArticles } from 'app/shared/model/my-articles.model';
import { MyArticlesService } from './my-articles.service';
import { MyArticlesComponent } from './my-articles.component';
import { MyArticlesDetailComponent } from './my-articles-detail.component';
import { MyArticlesUpdateComponent } from './my-articles-update.component';

@Injectable({ providedIn: 'root' })
export class MyArticlesResolve implements Resolve<IMyArticles> {
  constructor(private service: MyArticlesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMyArticles> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((myArticles: HttpResponse<MyArticles>) => {
          if (myArticles.body) {
            return of(myArticles.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MyArticles());
  }
}

export const myArticlesRoute: Routes = [
  {
    path: '',
    component: MyArticlesComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'MyArticles',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MyArticlesDetailComponent,
    resolve: {
      myArticles: MyArticlesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'MyArticles',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MyArticlesUpdateComponent,
    resolve: {
      myArticles: MyArticlesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'MyArticles',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MyArticlesUpdateComponent,
    resolve: {
      myArticles: MyArticlesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'MyArticles',
    },
    canActivate: [UserRouteAccessService],
  },
];
