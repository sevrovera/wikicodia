import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikicodiaSharedModule } from 'app/shared/shared.module';
import { MyArticlesComponent } from './my-articles.component';
import { MyArticlesDetailComponent } from './my-articles-detail.component';
import { MyArticlesUpdateComponent } from './my-articles-update.component';
import { MyArticlesDeleteDialogComponent } from './my-articles-delete-dialog.component';
import { myArticlesRoute } from './my-articles.route';

@NgModule({
  imports: [WikicodiaSharedModule, RouterModule.forChild(myArticlesRoute)],
  declarations: [MyArticlesComponent, MyArticlesDetailComponent, MyArticlesUpdateComponent, MyArticlesDeleteDialogComponent],
  entryComponents: [MyArticlesDeleteDialogComponent],
})
export class WikicodiaMyArticlesModule {}
