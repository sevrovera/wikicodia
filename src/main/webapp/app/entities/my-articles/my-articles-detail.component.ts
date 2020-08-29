import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMyArticles } from '../../shared/model/my-articles.model';
import { IArticle } from '../../shared/model/article.model';

@Component({
  selector: 'jhi-my-articles-detail',
  templateUrl: './my-articles-detail.component.html',
})
export class MyArticlesDetailComponent implements OnInit {
  // myArticles: IMyArticles | null = null;
  myArticles: IArticle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ myArticles }) => (this.myArticles = myArticles));
  }

  previousState(): void {
    window.history.back();
  }
}
