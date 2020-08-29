import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMyArticles } from '../../shared/model/my-articles.model';
import { IArticle } from '../../shared/model/article.model';
import { MyArticlesService } from './my-articles.service';

@Component({
  templateUrl: './my-articles-delete-dialog.component.html',
})
export class MyArticlesDeleteDialogComponent {
  //myArticles?: IMyArticles;
  myArticles?: IArticle;

  constructor(
    protected myArticlesService: MyArticlesService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.myArticlesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('myArticlesListModification');
      this.activeModal.close();
    });
  }
}
