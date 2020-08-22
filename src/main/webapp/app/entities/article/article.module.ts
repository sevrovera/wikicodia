import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikicodiaSharedModule } from 'app/shared/shared.module';
import { ArticleComponent } from './article.component';
import { ArticleDetailComponent } from './article-detail.component';
import { ArticleUpdateComponent } from './article-update.component';
import { ArticleDeleteDialogComponent } from './article-delete-dialog.component';
import { articleRoute } from './article.route';

@NgModule({
  imports: [WikicodiaSharedModule, RouterModule.forChild(articleRoute)],
  declarations: [ArticleComponent, ArticleDetailComponent, ArticleUpdateComponent, ArticleDeleteDialogComponent],
  entryComponents: [ArticleDeleteDialogComponent],
})
export class WikicodiaArticleModule {}
